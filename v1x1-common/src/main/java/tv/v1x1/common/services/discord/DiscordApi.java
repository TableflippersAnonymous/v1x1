package tv.v1x1.common.services.discord;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import tv.v1x1.common.services.discord.resources.AuditLogsResource;
import tv.v1x1.common.services.discord.resources.ChannelsResource;
import tv.v1x1.common.services.discord.resources.EmojisResource;
import tv.v1x1.common.services.discord.resources.GatewayResource;
import tv.v1x1.common.services.discord.resources.GuildsResource;
import tv.v1x1.common.services.discord.resources.InvitesResource;
import tv.v1x1.common.services.discord.resources.OAuth2Resource;
import tv.v1x1.common.services.discord.resources.UsersResource;
import tv.v1x1.common.services.discord.resources.VoiceResource;
import tv.v1x1.common.services.discord.resources.WebhooksResource;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.logging.Level;

/**
 * Created by naomi on 9/10/2017.
 */
public class DiscordApi {
    public static final String ACCEPT = "application/json";
    public static final String BASE_URL = "https://discordapp.com/api/v6";

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String oauthToken;

    private final AuditLogsResource auditLogs;
    private final ChannelsResource channels;
    private final EmojisResource emojis;
    private final GatewayResource gateway;
    private final GuildsResource guilds;
    private final InvitesResource invites;
    private final OAuth2Resource oauth2;
    private final UsersResource users;
    private final VoiceResource voice;
    private final WebhooksResource webhooks;

    public DiscordApi(final String clientId, final String oauthToken, final String clientSecret,
                      final String redirectUri) {
        this(clientId, oauthToken, clientSecret, redirectUri, "Bearer");
    }

    public DiscordApi(final String clientId, final String oauthToken, final String clientSecret,
                      final String redirectUri, final String tokenType) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.oauthToken = oauthToken;
        final DiscordApiRequestFilter discordApiRequestFilter = new DiscordApiRequestFilter(tokenType + " " + oauthToken);
        final Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);
        client.register(discordApiRequestFilter);
        client.register(new LoggingFeature(null, Level.INFO, LoggingFeature.Verbosity.PAYLOAD_ANY, null));
        final WebTarget api = client.target(BASE_URL);
        this.auditLogs = new AuditLogsResource(api.path("guilds"));
        this.channels = new ChannelsResource(api.path("channels"));
        this.emojis = new EmojisResource(api.path("guilds"));
        this.gateway = new GatewayResource(api.path("gateway"));
        this.guilds = new GuildsResource(api.path("guilds"));
        this.invites = new InvitesResource(api.path("invites"));
        this.oauth2 = new OAuth2Resource(api.path("oauth2"), clientId, clientSecret, redirectUri);
        this.users = new UsersResource(api.path("users"));
        this.voice = new VoiceResource(api.path("voice"));
        this.webhooks = new WebhooksResource(api.path("channels"), api.path("guilds"), api.path("webhooks"));
    }

    public DiscordApi withToken(final String oauthToken) {
        return new DiscordApi(clientId, oauthToken, clientSecret, redirectUri);
    }

    public DiscordApi withToken(final String oauthToken, final String type) {
        return new DiscordApi(clientId, oauthToken, clientSecret, redirectUri, type);
    }

    public String getOauthToken() {
        return oauthToken;
    }

    public AuditLogsResource getAuditLogs() {
        return auditLogs;
    }

    public ChannelsResource getChannels() {
        return channels;
    }

    public EmojisResource getEmojis() {
        return emojis;
    }

    public GatewayResource getGateway() {
        return gateway;
    }

    public GuildsResource getGuilds() {
        return guilds;
    }

    public InvitesResource getInvites() {
        return invites;
    }

    public OAuth2Resource getOauth2() {
        return oauth2;
    }

    public UsersResource getUsers() {
        return users;
    }

    public VoiceResource getVoice() {
        return voice;
    }

    public WebhooksResource getWebhooks() {
        return webhooks;
    }
}
