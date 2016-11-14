package tv.v1x1.common.services.twitch.unsupported;

import tv.v1x1.common.services.twitch.unsupported.tmi.TmiApi;

/**
 * Created by cobi on 11/13/2016.
 */
public class TwitchUnsupportedApi {
    private final TmiApi tmiApi;

    public TwitchUnsupportedApi(final String clientId) {
        tmiApi = new TmiApi(clientId);
    }

    public TmiApi getTmiApi() {
        return tmiApi;
    }
}
