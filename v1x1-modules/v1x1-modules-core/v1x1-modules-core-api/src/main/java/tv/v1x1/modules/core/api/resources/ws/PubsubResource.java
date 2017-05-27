package tv.v1x1.modules.core.api.resources.ws;

import com.google.inject.Inject;
import tv.v1x1.modules.core.api.ApiModule;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * Created by naomi on 4/30/2017.
 */

@ServerEndpoint("/pubsub")
public class PubsubResource {
    @Inject
    private static ApiModule apiModule;

    @OnOpen
    public void onOpen(final Session session) {
        
    }
}
