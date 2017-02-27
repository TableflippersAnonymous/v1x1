package tv.v1x1.common.services.twitch.resources;

import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.services.twitch.dto.communities.Community;
import tv.v1x1.common.services.twitch.dto.communities.CommunityBanList;
import tv.v1x1.common.services.twitch.dto.communities.CommunityId;
import tv.v1x1.common.services.twitch.dto.communities.CommunityModeratorList;
import tv.v1x1.common.services.twitch.dto.communities.CommunityPermissions;
import tv.v1x1.common.services.twitch.dto.communities.CommunityTimeOutUserRequest;
import tv.v1x1.common.services.twitch.dto.communities.CommunityTimedOutUserList;
import tv.v1x1.common.services.twitch.dto.communities.CommunityViolationRequest;
import tv.v1x1.common.services.twitch.dto.communities.CreateCommunityAvatarRequest;
import tv.v1x1.common.services.twitch.dto.communities.CreateCommunityCoverRequest;
import tv.v1x1.common.services.twitch.dto.communities.CreateCommunityRequest;
import tv.v1x1.common.services.twitch.dto.communities.TopCommunityList;
import tv.v1x1.common.services.twitch.dto.communities.UpdateCommunityRequest;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 * Created by cobi on 2/27/2017.
 */
public class CommunitiesResource {
    private final WebTarget communities;

    public CommunitiesResource(final WebTarget communities) {
        this.communities = communities;
    }

    /**
     * Gets a specified community.
     * @param communityName The name of the community. It must be 3-25 characters.
     */
    public Community getCommunityByName(final String communityName) {
        return communities.queryParam("name", communityName).request(TwitchApi.ACCEPT).get().readEntity(Community.class);
    }

    /**
     * Gets a specified community.
     * @param communityId ID of community
     */
    public Community getCommunity(final String communityId) {
        return communities.path(communityId).request(TwitchApi.ACCEPT).get().readEntity(Community.class);
    }

    /**
     * Creates a community.
     * @param name Community name. 3-25 characters, which can be alphanumerics, dashes (-), periods (.), underscores
     *             (_), and tildes (~). Cannot contain spaces.
     * @param summary Short description of the community, shown in search results. Maximum: 160 characters.
     * @param description Long description of the community, shown in the “about this community” box. Markdown syntax
     *                    allowed. Maximum 1,572,864 characters (1.5 MB).
     * @param rules Rules displayed when viewing a community page or searching for a community from the broadcaster
     *              dashboard. Markdown syntax allowed. Maximum 1,572,864 characters (1.5 MB)
     */
    public CommunityId create(final String name, final String summary, final String description, final String rules) {
        return communities.request(TwitchApi.ACCEPT)
                .post(Entity.entity(new CreateCommunityRequest(name, summary, description, rules), MediaType.APPLICATION_JSON))
                .readEntity(CommunityId.class);
    }

    /**
     * Updates a specified community.
     * @param communityID ID of community
     * @param summary Short description of the community, shown in search results. Maximum: 160 characters.
     * @param description Long description of the community, shown in the “about this community” box. Markdown syntax
     *                    allowed. Maximum 1,572,864 characters (1.5 MB).
     * @param rules Rules displayed when viewing a community page or searching for a community from the broadcaster
     *              dashboard. Markdown syntax allowed. Maximum 1,572,864 characters (1.5 MB)
     * @param email Email address of the community owner.
     */
    public void update(final String communityID, final String summary, final String description, final String rules, final String email) {
        communities.path(communityID).request(TwitchApi.ACCEPT)
                .put(Entity.entity(new UpdateCommunityRequest(summary, description, rules, email), MediaType.APPLICATION_JSON));
    }

    /**
     * Gets the top communities by viewer count.
     * @param limit Maximum number of most-recent objects to return. Default: 10. Maximum: 100.
     * @param cursor Tells the server where to start fetching the next set of results in a multi-page response.
     */
    public TopCommunityList getTopCommunities(final long limit, final String cursor) {
        return communities.path("top").queryParam("limit", limit).queryParam("cursor", cursor)
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(TopCommunityList.class);
    }

    /**
     * Gets a list of banned users for a specified community.
     * @param communityId ID of community
     * @param limit Maximum number of most-recent objects to return. Default: 10. Maximum: 100.
     * @param cursor Tells the server where to start fetching the next set of results in a multi-page response.
     */
    public CommunityBanList getCommunityBans(final String communityId, final long limit, final String cursor) {
        return communities.path(communityId).path("bans")
                .queryParam("limit", limit).queryParam("cursor", cursor)
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(CommunityBanList.class);
    }

    /**
     * Adds a specified user to the ban list of a specified community.
     * @param communityId ID of community
     * @param userId ID of user
     */
    public void banUser(final String communityId, final String userId) {
        communities.path(communityId).path("bans").path(userId)
                .request(TwitchApi.ACCEPT)
                .put(null);
    }

    /**
     * Deletes a specified user from the ban list of a specified community.
     * @param communityId ID of community
     * @param userId ID of user
     */
    public void unbanUser(final String communityId, final String userId) {
        communities.path(communityId).path("bans").path(userId)
                .request(TwitchApi.ACCEPT)
                .delete();
    }

    /**
     * Adds a specified image as the avatar of a specified community.
     * @param communityId ID of community
     * @param image A base-64 encoded representation of the avatar image. Avatar images must be 600x800 pixels.
     */
    public void createAvatar(final String communityId, final String image) {
        communities.path(communityId).path("images").path("avatar")
                .request(TwitchApi.ACCEPT)
                .post(Entity.entity(new CreateCommunityAvatarRequest(image), MediaType.APPLICATION_JSON));
    }

    /**
     * Deletes the avatar image of a specified community.
     * @param communityId ID of community
     */
    public void deleteAvatar(final String communityId) {
        communities.path(communityId).path("images").path("avatar")
                .request(TwitchApi.ACCEPT)
                .delete();
    }

    /**
     * Adds a specified image as the cover image of a specified community.
     * @param communityId ID of community
     * @param image A base-64 encoded representation of the cover image. Cover images must be 1200x180 pixels.
     */
    public void createCoverImage(final String communityId, final String image) {
        communities.path(communityId).path("images").path("cover")
                .request(TwitchApi.ACCEPT)
                .post(Entity.entity(new CreateCommunityCoverRequest(image), MediaType.APPLICATION_JSON));
    }

    /**
     * Deletes the cover image of a specified community.
     * @param communityId ID of community
     */
    public void deleteCoverImage(final String communityId) {
        communities.path(communityId).path("images").path("cover")
                .request(TwitchApi.ACCEPT)
                .delete();
    }

    /**
     * Gets a list of moderators of a specified community.
     * @param communityId ID of community
     */
    public CommunityModeratorList getModerators(final String communityId) {
        return communities.path(communityId).path("moderators")
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(CommunityModeratorList.class);
    }

    /**
     * Adds a specified user to the list of moderators of a specified community.
     * @param communityId ID of community
     * @param userId ID of user
     */
    public void addModerator(final String communityId, final String userId) {
        communities.path(communityId).path("moderators").path(userId)
                .request(TwitchApi.ACCEPT)
                .put(null);
    }

    /**
     * Deletes a specified user from the list of moderators of a specified community.
     * @param communityId ID of community
     * @param userId ID of user
     */
    public void removeModerator(final String communityId, final String userId) {
        communities.path(communityId).path("moderators").path(userId)
                .request(TwitchApi.ACCEPT)
                .delete();
    }

    /**
     * Gets a list of actions users can perform in a specified community.
     * @param communityId ID of community
     */
    public CommunityPermissions getPermissions(final String communityId) {
        return communities.path(communityId).path("permissions")
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(CommunityPermissions.class);
    }

    /**
     * Reports a specified channel for violating the rules of a specified community.
     * @param communityId ID of community
     * @param channelId ID of channel
     */
    public void reportViolation(final String communityId, final String channelId) {
        communities.path(communityId).path("report_channel")
                .request(TwitchApi.ACCEPT)
                .post(Entity.entity(new CommunityViolationRequest(channelId), MediaType.APPLICATION_JSON));
    }

    /**
     * Gets a list of users who are timed out in a specified community.
     * @param communityId ID of community
     * @param limit Maximum number of most-recent objects to return. Default: 10. Maximum: 100.
     * @param cursor Tells the server where to start fetching the next set of results in a multi-page response.
     */
    public CommunityTimedOutUserList getTimedOutUsers(final String communityId, final long limit, final String cursor) {
        return communities.path(communityId).path("timeouts")
                .queryParam("limit", limit).queryParam("cursor", cursor)
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(CommunityTimedOutUserList.class);
    }

    /**
     * Adds a specified user to the timeout list of a specified community.
     * @param communityId ID of community
     * @param userId ID of user
     * @param duration Length of the timeout, in hours.
     * @param reason Reason for the timeout. (optional)
     */
    public void timeOutUser(final String communityId, final String userId, final int duration, final String reason) {
        communities.path(communityId).path("timeouts").path(userId)
                .request(TwitchApi.ACCEPT)
                .put(Entity.entity(new CommunityTimeOutUserRequest(duration, reason), MediaType.APPLICATION_JSON));
    }

    /**
     * Deletes a specified user from the timeout list of a specified community.
     * @param communityId ID of community
     * @param userId ID of user
     */
    public void untimeOutUser(final String communityId, final String userId) {
        communities.path(communityId).path("timeouts").path(userId)
                .request(TwitchApi.ACCEPT)
                .delete();
    }
}
