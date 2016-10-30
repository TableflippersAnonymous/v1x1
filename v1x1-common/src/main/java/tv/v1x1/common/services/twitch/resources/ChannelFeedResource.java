package tv.v1x1.common.services.twitch.resources;

import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.services.twitch.dto.feed.Post;
import tv.v1x1.common.services.twitch.dto.feed.PostList;
import tv.v1x1.common.services.twitch.dto.feed.PostRequest;
import tv.v1x1.common.services.twitch.dto.feed.PostResponse;
import tv.v1x1.common.services.twitch.dto.feed.ReactionResponse;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 * Created by naomi on 10/29/2016.
 */
public class ChannelFeedResource {
    private final WebTarget feed;

    public ChannelFeedResource(final WebTarget feed) {
        this.feed = feed;
    }

    public PostList getPosts(final String channel, final int limit) {
        return feed.path(channel).path("posts").queryParam("limit", limit).request(TwitchApi.ACCEPT).get().readEntity(PostList.class);
    }

    public PostList getPosts(final String channel, final int limit, final String cursor) {
        return feed.path(channel).path("posts").queryParam("limit", limit).queryParam("cursor", cursor).request(TwitchApi.ACCEPT).get().readEntity(PostList.class);
    }

    public PostResponse post(final String channel, final String content, final boolean share) {
        return feed.path(channel).path("posts").request(TwitchApi.ACCEPT).post(Entity.entity(new PostRequest(content, share), MediaType.APPLICATION_JSON)).readEntity(PostResponse.class);
    }

    public Post getPost(final String channel, final String id) {
        return feed.path(channel).path("posts").path(id).request(TwitchApi.ACCEPT).get().readEntity(Post.class);
    }

    public void delete(final String channel, final String id) {
        feed.path(channel).path("posts").path(id).request(TwitchApi.ACCEPT).delete();
    }

    public ReactionResponse react(final String channel, final String id, final String emote) {
        return feed.path(channel).path("posts").path(id).path("reactions").queryParam("emote_id", emote).request(TwitchApi.ACCEPT).post(null).readEntity(ReactionResponse.class);
    }

    public void deleteReaction(final String channel, final String id, final String emote) {
        feed.path(channel).path("posts").path(id).path("reactions").queryParam("emote_id", emote).request(TwitchApi.ACCEPT).delete();
    }
}
