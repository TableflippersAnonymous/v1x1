package tv.v1x1.common.services.twitch;

import tv.v1x1.common.services.twitch.resources.BlocksResource;
import tv.v1x1.common.services.twitch.resources.ChannelFeedResource;
import tv.v1x1.common.services.twitch.resources.ChannelsResource;
import tv.v1x1.common.services.twitch.resources.ChatResource;
import tv.v1x1.common.services.twitch.resources.FollowsResource;
import tv.v1x1.common.services.twitch.resources.GamesResource;
import tv.v1x1.common.services.twitch.resources.IngestsResource;
import tv.v1x1.common.services.twitch.resources.RootResource;
import tv.v1x1.common.services.twitch.resources.SearchResource;
import tv.v1x1.common.services.twitch.resources.StreamsResource;
import tv.v1x1.common.services.twitch.resources.SubscriptionsResource;
import tv.v1x1.common.services.twitch.resources.TeamsResource;
import tv.v1x1.common.services.twitch.resources.UsersResource;
import tv.v1x1.common.services.twitch.resources.VideosResource;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * Created by naomi on 10/28/2016.
 */
public class TwitchApi {
    public static final String ACCEPT = "application/vnd.twitchtv.v3+json";
    public static final String BASE_URL = "https://api.twitch.tv/kraken";

    private final BlocksResource blocks;
    private final ChannelFeedResource channelFeed;
    private final ChannelsResource channels;
    private final ChatResource chat;
    private final FollowsResource follows;
    private final GamesResource games;
    private final IngestsResource ingests;
    private final RootResource root;
    private final SearchResource search;
    private final StreamsResource streams;
    private final SubscriptionsResource subscriptions;
    private final TeamsResource teams;
    private final UsersResource users;
    private final VideosResource videos;

    public TwitchApi(final String clientId, final String oauthToken) {
        final TwitchApiRequestFilter twitchApiRequestFilter = new TwitchApiRequestFilter(clientId, oauthToken);
        final Client client = ClientBuilder.newClient();
        client.register(twitchApiRequestFilter);
        final WebTarget api = client.target(BASE_URL);
        blocks = new BlocksResource(api.path("users"));
        channelFeed = new ChannelFeedResource(api.path("feed"));
        channels = new ChannelsResource(api.path("channels"));
        chat = new ChatResource(api.path("chat"));
        follows = new FollowsResource(api.path("channels"), api.path("users"));
        games = new GamesResource(api.path("games"));
        ingests = new IngestsResource(api.path("ingests"));
        root = new RootResource(api);
        search = new SearchResource(api.path("search"));
        streams = new StreamsResource(api.path("streams"));
        subscriptions = new SubscriptionsResource(api.path("channels"), api.path("users"));
        teams = new TeamsResource(api.path("teams"));
        users = new UsersResource(api.path("users"), api.path("user"), api.path("streams"), api.path("videos"));
        videos = new VideosResource(api.path("videos"), api.path("channels"));
    }

    public BlocksResource getBlocks() {
        return blocks;
    }

    public ChannelFeedResource getChannelFeed() {
        return channelFeed;
    }

    public ChannelsResource getChannels() {
        return channels;
    }

    public ChatResource getChat() {
        return chat;
    }

    public FollowsResource getFollows() {
        return follows;
    }

    public GamesResource getGames() {
        return games;
    }

    public IngestsResource getIngests() {
        return ingests;
    }

    public RootResource getRoot() {
        return root;
    }

    public SearchResource getSearch() {
        return search;
    }

    public StreamsResource getStreams() {
        return streams;
    }

    public SubscriptionsResource getSubscriptions() {
        return subscriptions;
    }

    public TeamsResource getTeams() {
        return teams;
    }

    public UsersResource getUsers() {
        return users;
    }

    public VideosResource getVideos() {
        return videos;
    }
}
