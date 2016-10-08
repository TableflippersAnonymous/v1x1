package tv.twitchbot.modules.core.tmi;

import java.io.*;
import java.net.Socket;
import java.util.Set;

/**
 * Created by cobi on 10/8/2016.
 */
public class TmiBot implements Runnable {
    private volatile boolean running;
    private volatile Socket socket;
    private volatile InputStream inputStream;
    private volatile BufferedReader reader;
    private volatile BufferedOutputStream outputStream;
    private final String oauthToken;
    private final String username;
    private final Set<String> channels;

    public TmiBot(String oauthToken, String username, Set<String> channels) {
        this.oauthToken = oauthToken;
        this.username = username;
        this.channels = channels;
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void disconnect() throws IOException {
        quit();
        socket.close();
    }

    private void requestCaps() throws IOException {
        sendLine("CAP REQ :twitch.tv/membership");
        sendLine("CAP REQ :twitch.tv/commands");
        sendLine("CAP REQ :twitch.tv/tags");
    }

    private void processLine(String line) {
        /* [tags] <source> <command> [args] */
        String tags = null;
        if(line.startsWith("@")) {
            String[] parts = line.split(" ", 2);
            tags = parts[0];
            line = parts[1];
        }
        if(!line.startsWith(":"))
            throw new RuntimeException("Unparsable TMI line: " + line);
        String[] parts = line.split(" ", 3);
        String source = parts[0];
        String command = parts[1];
        String args = parts[2];
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

    private void joinChannels() throws IOException {
        for(String channel : channels)
            join(channel);
    }

    private void join(String channel) throws IOException {
        channels.add(channel);
        sendLine("JOIN " + channel);
    }

    private void quit() throws IOException {
        sendLine("QUIT :Disconnecting.");
    }

    private String getLine() throws IOException {
        return reader.readLine();
    }

    public void shutdown() throws IOException {
        running = false;
        quit();
    }

}
