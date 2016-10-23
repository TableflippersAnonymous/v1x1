package tv.v1x1.modules.channel.hello_world;

import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.messages.events.ChatMessageEvent;
import tv.v1x1.common.dto.messages.events.SchedulerNotifyEvent;
import tv.v1x1.common.dto.messages.requests.SendMessageRequest;
import tv.v1x1.common.dto.proto.core.ChannelOuterClass;
import tv.v1x1.common.i18n.I18n;
import tv.v1x1.common.i18n.Language;
import tv.v1x1.common.modules.DefaultModule;
import tv.v1x1.common.rpc.client.ChatRouterServiceClient;
import tv.v1x1.common.rpc.client.SchedulerServiceClient;
import tv.v1x1.common.util.commands.CommandDelegator;
import tv.v1x1.common.util.data.CompositeKey;

import java.lang.invoke.MethodHandles;

/**
 * Created by Josh on 2016-10-06.
 */
public class HelloWorld extends DefaultModule<HelloWorldSettings, HelloWorldGlobalConfiguration, HelloWorldTenantConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static {
        final Module module = new Module("hello_world");
        I18n.registerDefault(module, "hello", "Hi there, %user%!");
    }

    CommandDelegator delegator;
    ChatRouterServiceClient crsc;
    SchedulerServiceClient ssc;
    Language language;

    public static void main(final String[] args) throws Exception {
        new HelloWorld().entryPoint(args);
    }

    @Override
    public String getName() {
        return "hello_world";
    }

    @Override
    protected void initialize() {
        super.initialize();
        delegator = new CommandDelegator("!");
        delegator.registerCommand(new HelloWorldCommand(this));
        delegator.registerCommand(new DelayCommand(this));
        delegator.registerCommand(new RepeatCommand(this));
        crsc = getServiceClient(ChatRouterServiceClient.class);
        ssc = getServiceClient(SchedulerServiceClient.class);
        language = getI18n().getLanguage(null);
    }

    @Override
    protected void processChatMessageEvent(final ChatMessageEvent chatMessageEvent) {
        super.processChatMessageEvent(chatMessageEvent);
        LOG.debug("Got chat message: {}", chatMessageEvent.getChatMessage().getText());
        if(!getTenantConfiguration(chatMessageEvent.getChatMessage().getChannel().getTenant()).isEnabled())
            return;
        delegator.handleChatMessage(chatMessageEvent);
    }

    @Override
    protected void processSchedulerNotifyEvent(final SchedulerNotifyEvent event) {
        super.processSchedulerNotifyEvent(event);
        LOG.debug("Got scheduler notify event: {} for {}", event.getId(), event.getModule().getName());
        if(!event.getModule().equals(toDto()))
            return;
        final byte[][] bytes = CompositeKey.getKeys(event.getPayload());
        try {
            final Channel channel = Channel.fromProto(ChannelOuterClass.Channel.parseFrom(bytes[0]));
            final String text = new String(bytes[1]);
            crsc.sendMessage(channel, text);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }
}
