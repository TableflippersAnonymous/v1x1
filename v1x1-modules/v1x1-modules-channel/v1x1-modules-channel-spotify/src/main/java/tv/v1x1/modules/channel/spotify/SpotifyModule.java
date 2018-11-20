package tv.v1x1.modules.channel.spotify;

import com.google.inject.Injector;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.modules.RegisteredThreadedModule;
import tv.v1x1.common.scanners.i18n.I18nDefault;
import tv.v1x1.common.scanners.i18n.I18nDefaults;
import tv.v1x1.common.scanners.permission.DefaultGroup;
import tv.v1x1.common.scanners.permission.Permissions;
import tv.v1x1.common.scanners.permission.RegisteredPermission;
import tv.v1x1.common.util.commands.AnnotationCommandProvider;
import tv.v1x1.common.util.commands.CommandDelegator;

@Permissions(version = 1, value = {
        @RegisteredPermission(
                node = "spotify.enqueue",
                displayName =  "Use !songrequest/!song request",
                description = "Allows user to request songs.",
                defaultGroups = {DefaultGroup.EVERYONE}
        ),
        @RegisteredPermission(
                node = "spotify.dequeue",
                displayName = "Use !song delete",
                description = "Removes a song from the queue.",
                defaultGroups = {DefaultGroup.OWNER, DefaultGroup.MODS}
        ),
        @RegisteredPermission(
                node = "spotify.purge",
                displayName = "Use !song purge",
                description = "Removes all songs from the queue.",
                defaultGroups = {DefaultGroup.OWNER, DefaultGroup.MODS}
        ),
        @RegisteredPermission(
                node = "spotify.start",
                displayName = "Use !song start",
                description = "Enables song playback.",
                defaultGroups = {DefaultGroup.OWNER, DefaultGroup.MODS}
        ),
        @RegisteredPermission(
                node = "spotify.stop",
                displayName = "Use !song stop",
                description = "Disable song playback.",
                defaultGroups = {DefaultGroup.OWNER, DefaultGroup.MODS}
        )
})
@I18nDefaults(version = 1, value = {
        @I18nDefault(
                key = "song.request.added",
                message = "%commander%, Song added!  %title% by %artist%",
                displayName = "Song Added",
                description = "In response to !song request"
        )
})
public class SpotifyModule extends RegisteredThreadedModule<SpotifyGlobalConfiguration, SpotifyUserConfiguration> {
    private Injector childInjector;

    public static void main(final String[] args) {
        new SpotifyModule().entryPoint(args);
    }

    @Override
    public String getName() {
        return "spotify";
    }

    @Override
    protected void initialize() {
        super.initialize();
        childInjector = getInjector().createChildInjector(new GuiceModule(this));
        final CommandDelegator commandDelegator = new CommandDelegator(new AnnotationCommandProvider(childInjector), "!");
        registerListener(new SpotifyListener(this, commandDelegator));
    }

    boolean isEnabled(final Channel channel) {
        return getConfiguration(channel).isEnabled();
    }

    public Injector getChildInjector() {
        return childInjector;
    }
}
