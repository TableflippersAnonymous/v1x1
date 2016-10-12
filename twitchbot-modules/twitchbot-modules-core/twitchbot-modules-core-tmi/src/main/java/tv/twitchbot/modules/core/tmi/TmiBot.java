package tv.twitchbot.modules.core.tmi;

import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.core.TwitchBot;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.irc.commands.PingCommand;
import tv.twitchbot.common.dto.irc.commands.PrivmsgCommand;
import tv.twitchbot.common.dto.messages.Event;
import tv.twitchbot.common.dto.messages.events.TwitchRawMessageEvent;
import tv.twitchbot.common.services.coordination.LoadBalancingDistributor;
import tv.twitchbot.common.services.queue.MessageQueue;

import java.io.*;
import java.net.Socket;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by naomi on 10/8/2016.
 */
public class TmiBot implements Runnable {
    private final LoadBalancingDistributor.Listener listener;
    private volatile boolean running;
    private volatile Socket socket;
    private volatile InputStream inputStream;
    private volatile BufferedReader reader;
    private volatile BufferedOutputStream outputStream;
    private final String oauthToken;
    private final String username;
    private final Module module;
    private final TwitchBot bot;
    private final UUID botId = UUID.randomUUID();
    private final LoadBalancingDistributor channelDistributor;
    private final MessageQueue messageQueue;
    private Set<String> channels = new ConcurrentSkipListSet<>();

    public TmiBot(String username, String oauthToken, LoadBalancingDistributor distributor, MessageQueue queue, Module module) {
        this.oauthToken = oauthToken;
        this.username = username;
        this.module = module;
        this.channelDistributor = distributor;
        this.messageQueue = queue;
        this.bot = new TwitchBot(username);
        listener = (instanceId, entries) -> {
            if (botId.equals(instanceId.getValue())) {
                setChannels(entries);
            }
        };
        channelDistributor.addListener(listener);
    }

    @Override
    public void run() {
        running = true;
        while(running) {
            try {
                connect();
                authenticate();
                requestCaps();
                joinChannels();
                for(;;) {
                    String line = getLine();
                    if (line == null)
                        break;
                    processLine(line);
                }
                disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cleanup();
            }
        }
        channelDistributor.removeListener(listener);
    }

    private void cleanup() {
        try {
            channelDistributor.removeInstance(new tv.twitchbot.common.dto.core.UUID(botId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void disconnect() throws IOException {
        try {
            channelDistributor.removeInstance(new tv.twitchbot.common.dto.core.UUID(botId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        quit();
        socket.close();
    }

    private void requestCaps() throws IOException {
        sendLine("CAP REQ :twitch.tv/membership");
        sendLine("CAP REQ :twitch.tv/commands");
        sendLine("CAP REQ :twitch.tv/tags");
    }

    private void processLine(String line) throws IOException {
        IrcStanza stanza = IrcParser.parse(line);
        event(new TwitchRawMessageEvent(module, bot, stanza));
        if(stanza instanceof PingCommand)
            sendLine("PONG :" + ((PingCommand) stanza).getToken());
        if(stanza instanceof PrivmsgCommand) {

        }
    }

    private void event(Event event) {
        messageQueue.add(event);
    }

    private void connect() throws IOException {
        socket = new Socket("irc.chat.twitch.tv", 6667);
        inputStream = socket.getInputStream();
        reader = new BufferedReader(new InputStreamReader(inputStream));
        outputStream = new BufferedOutputStream(socket.getOutputStream());
    }

    private void sendLine(String line) throws IOException {
        outputStream.write((line + "\r\n").getBytes());
        outputStream.flush();
    }

    private void authenticate() throws IOException {
        sendLine("PASS oauth:" + oauthToken);
        sendLine("USER " + username + " \"twitchbot.tv\" \"irc.chat.twitch.tv\" :" + username);
        sendLine("NICK " + username);
    }

    private void joinChannels() throws Exception {
        channelDistributor.addInstance(new tv.twitchbot.common.dto.core.UUID(botId));
    }

    private void join(String channel) throws IOException {
        channels.add(channel);
        sendLine("JOIN " + channel);
    }

    private void quit() throws IOException {
        channels.clear();
        sendLine("QUIT :Disconnecting.");
    }

    private String getLine() throws IOException {
        return reader.readLine();
    }

    public void shutdown() throws IOException {
        running = false;
        quit();
    }

    private void setChannels(Set<String> channels) {

    }
}
