package tv.v1x1.common.services.twitch.resources;

import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.services.twitch.dto.misc.SortDirection;
import tv.v1x1.common.services.twitch.dto.channels.Subscriber;
import tv.v1x1.common.services.twitch.dto.channels.SubscriberList;
import tv.v1x1.common.services.twitch.dto.users.Subscription;

import javax.ws.rs.client.WebTarget;

/**
 * Created by naomi on 10/30/2016.
 */
public class SubscriptionsResource {
    private final WebTarget channels;
    private final WebTarget users;

    public SubscriptionsResource(final WebTarget channels, final WebTarget users) {
        this.channels = channels;
        this.users = users;
    }

    public SubscriberList getSubscribers(final String channel, final Integer limit, final Integer offset, final SortDirection direction) {
        return channels.path(channel).path("subscriptions")
                .queryParam("limit", limit).queryParam("offset", offset).queryParam("direction", direction == null ? null : direction.name().toLowerCase())
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(SubscriberList.class);
    }

    public Subscriber getSubscriber(final String channel, final String user) {
        return channels.path(channel).path("subscriptions").path(user)
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(Subscriber.class);
    }

    public Subscription getSubscription(final String user, final String channel) {
        return users.path(user).path("subscriptions").path(channel)
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(Subscription.class);
    }
}
