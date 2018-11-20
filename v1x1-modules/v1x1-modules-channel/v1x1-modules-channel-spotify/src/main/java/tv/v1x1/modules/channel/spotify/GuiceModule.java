package tv.v1x1.modules.channel.spotify;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class GuiceModule extends AbstractModule {
    private final SpotifyModule module;

    public GuiceModule(final SpotifyModule module) {
        this.module = module;
    }

    @Override
    protected void configure() {}

    @Provides
    public SpotifyModule getModule() {
        return module;
    }
}
