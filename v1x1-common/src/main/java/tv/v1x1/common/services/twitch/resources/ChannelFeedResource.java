package tv.v1x1.common.services.twitch.resources;

import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.services.twitch.dto.feed.Comment;
import tv.v1x1.common.services.twitch.dto.feed.CommentList;
import tv.v1x1.common.services.twitch.dto.feed.CommentRequest;
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

    /**
     * Gets posts from a specified channel feed.
     * @param channelId ID of channel
     * @param limit Maximum number of most-recent objects to return. Default: 10. Maximum: 100.
     */
    public PostList getPosts(final String channelId, final long limit) {
        return feed.path(channelId).path("posts").queryParam("limit", limit).request(TwitchApi.ACCEPT).get(PostList.class);
    }

    /**
     * Gets posts from a specified channel feed.
     * @param channelId ID of channel
     * @param limit Maximum number of most-recent objects to return. Default: 10. Maximum: 100.
     * @param cursor Tells the server where to start fetching the next set of results in a multi-page response.
     */
    public PostList getPosts(final String channelId, final long limit, final String cursor) {
        return feed.path(channelId).path("posts").queryParam("limit", limit).queryParam("cursor", cursor).request(TwitchApi.ACCEPT).get(PostList.class);
    }

    /**
     * Gets a specified post from a specified channel feed.
     * @param channelId ID of channel
     * @param postId ID of post
     */
    public Post getPost(final String channelId, final String postId) {
        return feed.path(channelId).path("posts").path(postId).request(TwitchApi.ACCEPT).get(Post.class);
    }

    /**
     * Creates a post in a specified channel feed.
     * @param channelId ID of channel
     * @param content The content of the post
     * @param share When set to true, the post is shared on the channel’s Twitter feed (if connected), with a link to the post’s URL.
     */
    public PostResponse post(final String channelId, final String content, final boolean share) {
        return feed.path(channelId).path("posts").queryParam("share", share).request(TwitchApi.ACCEPT).post(
                Entity.entity(new PostRequest(content), MediaType.APPLICATION_JSON), PostResponse.class);
    }

    /**
     * Deletes a specified post in a specified channel feed.
     * @param channelId ID of channel
     * @param postId ID of post
     */
    public void delete(final String channelId, final String postId) {
        feed.path(channelId).path("posts").path(postId).request(TwitchApi.ACCEPT).delete();
    }

    /**
     * Creates a reaction to a specified post in a specified channel feed. The reaction is specified by an emote value,
     * which is either an ID (for example, "25" is Kappa) or the string "endorse" (which corresponds to a default face
     * emote).
     * @param channelId ID of channel
     * @param postId ID of post
     * @param emote Either an ID (for example, "25” is Kappa) or the string "endorse" (which corresponds to a default
     *             face emote).
     */
    public ReactionResponse react(final String channelId, final String postId, final String emote) {
        return feed.path(channelId).path("posts").path(postId).path("reactions").queryParam("emote_id", emote)
                .request(TwitchApi.ACCEPT).post(null, ReactionResponse.class);
    }

    /**
     * Deletes a specified reaction to a specified post in a specified channel feed. The reaction is specified by an
     * emote ID (for example, "25" is Kappa) or the string "endorse" (which corresponds to a default face emote).
     * @param channelId ID of channel
     * @param postId ID of post
     * @param emote Either an ID (for example, “25” is Kappa) or the string "endorse" (which corresponds to a default
     *              face emote).
     */
    public void deleteReaction(final String channelId, final String postId, final String emote) {
        feed.path(channelId).path("posts").path(postId).path("reactions").queryParam("emote_id", emote).request(TwitchApi.ACCEPT).delete();
    }

    /**
     * Gets all comments on a specified post in a specified channel feed.
     * @param channelId ID of channel
     * @param postId ID of post
     * @param limit Maximum number of most-recent objects to return. Default: 10. Maximum: 100.
     */
    public CommentList getComments(final String channelId, final String postId, final long limit) {
        return feed.path(channelId).path("posts").path(postId).path("comments").queryParam("limit", limit).request(TwitchApi.ACCEPT).get(CommentList.class);
    }

    /**
     * Gets all comments on a specified post in a specified channel feed.
     * @param channelId ID of channel
     * @param postId ID of post
     * @param limit Maximum number of most-recent objects to return. Default: 10. Maximum: 100.
     * @param cursor Tells the server where to start fetching the next set of results, in a multi-page response.
     */
    public CommentList getComments(final String channelId, final String postId, final long limit, final String cursor) {
        return feed.path(channelId).path("posts").path(postId).path("comments").queryParam("limit", limit).queryParam("cursor", cursor).request(TwitchApi.ACCEPT).get(CommentList.class);
    }

    /**
     * Creates a comment to a specified post in a specified channel feed.
     * @param channelId ID of channel
     * @param postId ID of post
     * @param content The content of the comment
     */
    public Comment comment(final String channelId, final String postId, final String content) {
        return feed.path(channelId).path("posts").path(postId).path("comments").request(TwitchApi.ACCEPT).post(
                Entity.entity(new CommentRequest(content), MediaType.APPLICATION_JSON), Comment.class);
    }

    /**
     * Deletes a specified comment on a specified post in a specified channel feed.
     * @param channelId ID of channel
     * @param postId ID of post
     * @param commentId ID of comment
     */
    public void deleteComment(final String channelId, final String postId, final String commentId) {
        feed.path(channelId).path("posts").path(postId).path("comments").path(commentId).request(TwitchApi.ACCEPT).delete();
    }

    /**
     * Creates a reaction to a specified comment on a specified post in a specified channel feed. The reaction is
     * specified by an emote value, which is either an ID (for example, "25" is Kappa) or the string "endorse" (which
     * corresponds to a default face emote).
     * @param channelId ID of channel
     * @param postId ID of post
     * @param commentId ID of comment
     * @param emote Either an ID (for example, "25" is Kappa) or the string "endorse" (which corresponds to a default
     *             face emote).
     */
    public ReactionResponse reactToComment(final String channelId, final String postId, final String commentId, final String emote) {
        return feed.path(channelId).path("posts").path(postId).path("comments").path(commentId).path("reactions")
                .queryParam("emote_id", emote).request(TwitchApi.ACCEPT).post(null, ReactionResponse.class);
    }

    /**
     * Deletes a reaction to a specified comment on a specified post in a specified channel feed. The reaction is
     * specified by an emote value, which is either an ID (for example, "25" is Kappa) or the string "endorse" (which
     * corresponds to a default face emote).
     * @param channelId ID of channel
     * @param postId ID of post
     * @param commentId ID of comment
     * @param emote Either an ID (for example, "25" is Kappa) or the string "endorse" (which corresponds to a default
     *             face emote).
     */
    public void deleteCommentReaction(final String channelId, final String postId, final String commentId, final String emote) {
        feed.path(channelId).path("posts").path(postId).path("comments").path(commentId).path("reactions").queryParam("emote_id", emote).request(TwitchApi.ACCEPT).delete();
    }
}
