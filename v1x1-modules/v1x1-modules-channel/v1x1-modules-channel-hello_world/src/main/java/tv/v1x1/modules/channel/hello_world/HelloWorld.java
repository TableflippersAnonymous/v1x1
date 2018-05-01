package tv.v1x1.modules.channel.hello_world;

import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.messages.events.ChatMessageEvent;
import tv.v1x1.common.dto.messages.events.SchedulerNotifyEvent;
import tv.v1x1.common.dto.proto.core.ChannelOuterClass;
import tv.v1x1.common.i18n.Language;
import tv.v1x1.common.modules.DefaultModule;
import tv.v1x1.common.rpc.client.ChatRouterServiceClient;
import tv.v1x1.common.rpc.client.SchedulerServiceClient;
import tv.v1x1.common.scanners.i18n.I18nDefault;
import tv.v1x1.common.scanners.i18n.I18nDefaults;
import tv.v1x1.common.scanners.permission.DefaultGroup;
import tv.v1x1.common.scanners.permission.Permissions;
import tv.v1x1.common.scanners.permission.RegisteredPermission;
import tv.v1x1.common.util.commands.CommandDelegator;
import tv.v1x1.common.util.data.CompositeKey;

import java.lang.invoke.MethodHandles;

/**
 * Created by Josh on 2016-10-06.
 */
@Permissions({
        @RegisteredPermission(
                node = "hello.use",
                displayName = "Use Hello",
                description = "This allows you to use the !hello and !lorem commands",
                defaultGroups = {DefaultGroup.OWNER, DefaultGroup.BROADCASTER, DefaultGroup.MODS}
        )
})
@I18nDefaults({
        @I18nDefault(
                key = "hello",
                message = "Hi there, %user%!",
                displayName = "Hello Message",
                description = "Sent in response to !hello"
        )
})
public class HelloWorld extends DefaultModule<HelloWorldGlobalConfiguration, HelloWorldUserConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
        delegator.registerCommand(new LorumCommand(this));
        crsc = getServiceClient(ChatRouterServiceClient.class);
        ssc = getServiceClient(SchedulerServiceClient.class);
        language = getI18n().getLanguage(null);
    }

    @Override
    protected void processChatMessageEvent(final ChatMessageEvent chatMessageEvent) {
        super.processChatMessageEvent(chatMessageEvent);
        LOG.debug("Got chat message: {}", chatMessageEvent.getChatMessage().getText());
        if(!getConfiguration(chatMessageEvent.getChatMessage().getChannel()).isEnabled())
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
        } catch (final InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }
}
