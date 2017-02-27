package tv.v1x1.common.services.twitch;

import org.glassfish.jersey.jackson.JacksonFeature;
import tv.v1x1.common.services.twitch.resources.ChannelFeedResource;
import tv.v1x1.common.services.twitch.resources.ChannelsResource;
import tv.v1x1.common.services.twitch.resources.ChatResource;
import tv.v1x1.common.services.twitch.resources.ClipsResource;
import tv.v1x1.common.services.twitch.resources.CommunitiesResource;
import tv.v1x1.common.services.twitch.resources.GamesResource;
import tv.v1x1.common.services.twitch.resources.IngestsResource;
import tv.v1x1.common.services.twitch.resources.OAuth2Resource;
import tv.v1x1.common.services.twitch.resources.RootResource;
import tv.v1x1.common.services.twitch.resources.SearchResource;
import tv.v1x1.common.services.twitch.resources.StreamsResource;
import tv.v1x1.common.services.twitch.resources.TeamsResource;
import tv.v1x1.common.services.twitch.resources.UsersResource;
import tv.v1x1.common.services.twitch.resources.VideosResource;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * Created by cobi on 10/28/2016.
 */
public class TwitchApi {
    public static final String ACCEPT_V4 = "application/vnd.twitchtv.v4+json";
    public static final String ACCEPT = "application/vnd.twitchtv.v5+json";
    public static final String BASE_URL = "https://api.twitch.tv/kraken";

    private final ChannelFeedResource channelFeed;
    private final ChannelsResource channels;
    private final ChatResource chat;
    private final ClipsResource clips;
    private final CommunitiesResource communities;
    private final GamesResource games;
    private final IngestsResource ingests;
    private final OAuth2Resource oauth2;
    private final RootResource root;
    private final SearchResource search;
    private final StreamsResource streams;
    private final TeamsResource teams;
    private final UsersResource users;
    private final VideosResource videos;

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    public TwitchApi(final String clientId, final String oauthToken, final String clientSecret, final String redirectUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        final TwitchApiRequestFilter twitchApiRequestFilter = new TwitchApiRequestFilter(clientId, oauthToken);
        final Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);
        client.register(twitchApiRequestFilter);
        final WebTarget api = client.target(BASE_URL);
        channelFeed = new ChannelFeedResource(api.path("feed"));
        channels = new ChannelsResource(api.path("channels"));
        chat = new ChatResource(api.path("chat"));
        clips = new ClipsResource(api.path("clips"));
        communities = new CommunitiesResource(api.path("communities"));
        games = new GamesResource(api.path("games"));
        ingests = new IngestsResource(api.path("ingests"));
        oauth2 = new OAuth2Resource(api.path("oauth2"), clientId, clientSecret, redirectUri);
        root = new RootResource(api);
        search = new SearchResource(api.path("search"));
        streams = new StreamsResource(api.path("streams"));
        teams = new TeamsResource(api.path("teams"));
        users = new UsersResource(api.path("users"), api.path("user"));
        videos = new VideosResource(api.path("videos"));
    }

    public TwitchApi withToken(final String token) {
        return new TwitchApi(clientId, token, clientSecret, redirectUri);
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

    public GamesResource getGames() {
        return games;
    }

    public IngestsResource getIngests() {
        return ingests;
    }

    public OAuth2Resource getOauth2() {
        return oauth2;
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

    public TeamsResource getTeams() {
        return teams;
    }

    public UsersResource getUsers() {
        return users;
    }

    public VideosResource getVideos() {
        return videos;
    }

    public CommunitiesResource getCommunities() {
        return communities;
    }

    public ClipsResource getClips() {
        return clips;
    }
}
