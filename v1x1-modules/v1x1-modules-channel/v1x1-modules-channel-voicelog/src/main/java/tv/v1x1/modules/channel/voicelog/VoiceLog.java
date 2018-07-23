package tv.v1x1.modules.channel.voicelog;

import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChannelRef;
import tv.v1x1.common.modules.RegisteredThreadedModule;
import tv.v1x1.common.scanners.i18n.I18nDefault;
import tv.v1x1.common.scanners.i18n.I18nDefaults;
import tv.v1x1.common.scanners.permission.DefaultGroup;
import tv.v1x1.common.scanners.permission.Permissions;
import tv.v1x1.common.scanners.permission.RegisteredPermission;
import tv.v1x1.common.util.commands.CommandDelegator;
import tv.v1x1.modules.channel.voicelog.commands.VoiceLogCommand;

/**
 * @author Josh
 */

@Permissions(version = 1, value = {
        @RegisteredPermission(
                node = "voicelog.set",
                displayName =  "Set voice log",
                description = "Sets and unset the voice log channel",
                defaultGroups = {DefaultGroup.OWNER, DefaultGroup.MODS}
        ),
        @RegisteredPermission(
                node = "voicelog.hidden",
                displayName = "Hidden from voice log",
                description = "Hides the join/part messages from voice log, even when the module is enabled"
        )
})
@I18nDefaults(version = 1, value = {
        @I18nDefault(
                key = "help.blurb",
                message = "Voice Log allows you to send all voice channel joins to a channel",
                displayName = "Help Blurb",
                description = "In response to !voicelog help"
        ),
        @I18nDefault(
                key = "set",
                message = "%commander%, voice channel activity will now be sent to %target%!",
                displayName = "Set message",
                description = "New voice log channel is set"
        ),
        @I18nDefault(
                key = "reset",
                message = "%commander%, voice channel activity will now be sent to %target% instead of %oldtarget%",
                displayName = "Reset message",
                description = "New voice log channel is set over another one"
        ),
        @I18nDefault(
                key = "targetnotfound",
                message = "%commander%, I cannot find that channel.",
                displayName = "Unknown target",
                description = "Voice log gets a request to set set, but target channel not found"
        ),
        @I18nDefault(
                key = "invalidargs",
                message = "%commander%, I don't understand what you want. Type !%alias% %syntax%",
                displayName = "Invalid args blurb",
                description = "Voice log gets a command it doesn't understand"
        ),
        @I18nDefault(
                key = "notset",
                message = "%commander%, the voice log channel is not set!",
                displayName = "Not set",
                description = "Someone asks for the voice log channel, but it's not set"
        ),
        @I18nDefault(
                key = "get",
                message = "%commander%, voice activity logs are sent to %target%",
                displayName = "Get channel",
                description = "Someone asks for the voice log channel"
        ),
        @I18nDefault(
                key = "clear",
                message = "%commander%, the voice activity log channel has been cleared! It was set to %oldtarget%",
                displayName = "Clear blurb",
                description = "Someone cleared the voice log channel setting"
        ),
        @I18nDefault(
                key = "alreadyset",
                message = "%commander%, %target% is already getting voice activity ... I guess I'll just set it again for you...",
                displayName = "Already set blurb",
                description = "Someone set the voice activity channel to the same channel"
        ),
        @I18nDefault(
                key = "logline.join",
                message = "%username% joined %channel%",
                displayName = "Voice connect log line",
                description = "The message sent when someone joins/leaves a channel"
        ),
        @I18nDefault(
                key = "logline.part",
                message = "%username% left %channel%",
                displayName = "Voice disconnect log line",
                description = "The message sent when someone joins/leaves a channel"
        ),
        @I18nDefault(
                key = "logline.move",
                message = "%username% moved to %newchannel%",
                displayName = "Move log line",
                description = "The message sent when someone moved from a channel to another"
        )
})
public class VoiceLog extends RegisteredThreadedModule<VoiceLogGlobalConfiguration, VoiceLogUserConfiguration> {

    /* pkg-private */ CommandDelegator delegator;

    public static void main(final String[] args) throws Exception {
        new VoiceLog().entryPoint(args);
    }
    
    @Override
    public String getName() {
        return "voicelog";
    }

    @Override
    public void initialize() {
        super.initialize();
        delegator = new CommandDelegator("!");
        delegator.registerCommand(new VoiceLogCommand(this));
        registerListener(new VoiceLogListener(this));
    }

    boolean isEnabled(final Channel channel) {
        return getConfiguration(channel).isEnabled();
    }

    public Channel setChannel(final Channel context, final Channel target) {
        final Channel oldChannel = getChannel(context);
        final VoiceLogUserConfiguration config = getUserConfigProvider().getConfiguration(context.getChannelGroup());
        config.setChannel(target);
        getUserConfigProvider().save(context.getTenant(), config);
        return oldChannel;
    }

    public Channel getChannel(final Channel context) {
        final ChannelRef channelRef = getUserConfigProvider().getConfiguration(context.getChannelGroup()).getChannel();
        if(channelRef != null)
            return context.getChannelGroup().getChannel(channelRef.getId()).orElse(null);
        return null;
    }
}
