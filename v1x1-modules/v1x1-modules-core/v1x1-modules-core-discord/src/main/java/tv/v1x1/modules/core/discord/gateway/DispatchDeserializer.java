package tv.v1x1.modules.core.discord.gateway;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import tv.v1x1.modules.core.discord.gateway.events.ChannelCreateEvent;
import tv.v1x1.modules.core.discord.gateway.events.ChannelDeleteEvent;
import tv.v1x1.modules.core.discord.gateway.events.ChannelPinsUpdateEvent;
import tv.v1x1.modules.core.discord.gateway.events.ChannelUpdateEvent;
import tv.v1x1.modules.core.discord.gateway.events.GuildBanAddEvent;
import tv.v1x1.modules.core.discord.gateway.events.GuildBanRemoveEvent;
import tv.v1x1.modules.core.discord.gateway.events.GuildCreateEvent;
import tv.v1x1.modules.core.discord.gateway.events.GuildDeleteEvent;
import tv.v1x1.modules.core.discord.gateway.events.GuildEmojisUpdateEvent;
import tv.v1x1.modules.core.discord.gateway.events.GuildIntegrationsUpdateEvent;
import tv.v1x1.modules.core.discord.gateway.events.GuildMemberAddEvent;
import tv.v1x1.modules.core.discord.gateway.events.GuildMemberRemoveEvent;
import tv.v1x1.modules.core.discord.gateway.events.GuildMemberUpdateEvent;
import tv.v1x1.modules.core.discord.gateway.events.GuildMembersChunkEvent;
import tv.v1x1.modules.core.discord.gateway.events.GuildRoleCreateEvent;
import tv.v1x1.modules.core.discord.gateway.events.GuildRoleDeleteEvent;
import tv.v1x1.modules.core.discord.gateway.events.GuildRoleUpdateEvent;
import tv.v1x1.modules.core.discord.gateway.events.GuildUpdateEvent;
import tv.v1x1.modules.core.discord.gateway.events.MessageCreateEvent;
import tv.v1x1.modules.core.discord.gateway.events.MessageDeleteBulkEvent;
import tv.v1x1.modules.core.discord.gateway.events.MessageDeleteEvent;
import tv.v1x1.modules.core.discord.gateway.events.MessageReactionAddEvent;
import tv.v1x1.modules.core.discord.gateway.events.MessageReactionRemoveAllEvent;
import tv.v1x1.modules.core.discord.gateway.events.MessageReactionRemoveEvent;
import tv.v1x1.modules.core.discord.gateway.events.MessageUpdateEvent;
import tv.v1x1.modules.core.discord.gateway.events.PresenceUpdateEvent;
import tv.v1x1.modules.core.discord.gateway.events.ReadyEvent;
import tv.v1x1.modules.core.discord.gateway.events.ResumedEvent;
import tv.v1x1.modules.core.discord.gateway.events.TypingStartEvent;
import tv.v1x1.modules.core.discord.gateway.events.UserUpdateEvent;
import tv.v1x1.modules.core.discord.gateway.events.VoiceServerUpdateEvent;
import tv.v1x1.modules.core.discord.gateway.events.VoiceStateUpdateEvent;
import tv.v1x1.modules.core.discord.gateway.events.WebhooksUpdateEvent;

import java.io.IOException;

/**
 * Created by naomi on 4/13/2018.
 */
public class DispatchDeserializer extends StdDeserializer<DispatchPayload> {
    public DispatchDeserializer() {
        super(DispatchPayload.class);
    }

    @Override
    public DispatchPayload deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
        final JsonNode node = p.getCodec().readTree(p);

        switch(node.get("t").asText()) {
            case "READY": return p.getCodec().treeToValue(node, ReadyEvent.class);
            case "RESUMED": return p.getCodec().treeToValue(node, ResumedEvent.class);
            case "CHANNEL_CREATE": return p.getCodec().treeToValue(node, ChannelCreateEvent.class);
            case "CHANNEL_UPDATE": return p.getCodec().treeToValue(node, ChannelUpdateEvent.class);
            case "CHANNEL_DELETE": return p.getCodec().treeToValue(node, ChannelDeleteEvent.class);
            case "CHANNEL_PINS_UPDATE": return p.getCodec().treeToValue(node, ChannelPinsUpdateEvent.class);
            case "GUILD_CREATE": return p.getCodec().treeToValue(node, GuildCreateEvent.class);
            case "GUILD_UPDATE": return p.getCodec().treeToValue(node, GuildUpdateEvent.class);
            case "GUILD_DELETE": return p.getCodec().treeToValue(node, GuildDeleteEvent.class);
            case "GUILD_BAN_ADD": return p.getCodec().treeToValue(node, GuildBanAddEvent.class);
            case "GUILD_BAN_REMOVE": return p.getCodec().treeToValue(node, GuildBanRemoveEvent.class);
            case "GUILD_EMOJIS_UPDATE": return p.getCodec().treeToValue(node, GuildEmojisUpdateEvent.class);
            case "GUILD_INTEGRATIONS_UPDATE": return p.getCodec().treeToValue(node, GuildIntegrationsUpdateEvent.class);
            case "GUILD_MEMBER_ADD": return p.getCodec().treeToValue(node, GuildMemberAddEvent.class);
            case "GUILD_MEMBER_REMOVE": return p.getCodec().treeToValue(node, GuildMemberRemoveEvent.class);
            case "GUILD_MEMBER_UPDATE": return p.getCodec().treeToValue(node, GuildMemberUpdateEvent.class);
            case "GUILD_MEMBERS_CHUNK": return p.getCodec().treeToValue(node, GuildMembersChunkEvent.class);
            case "GUILD_ROLE_CREATE": return p.getCodec().treeToValue(node, GuildRoleCreateEvent.class);
            case "GUILD_ROLE_UPDATE": return p.getCodec().treeToValue(node, GuildRoleUpdateEvent.class);
            case "GUILD_ROLE_DELETE": return p.getCodec().treeToValue(node, GuildRoleDeleteEvent.class);
            case "MESSAGE_CREATE": return p.getCodec().treeToValue(node, MessageCreateEvent.class);
            case "MESSAGE_UPDATE": return p.getCodec().treeToValue(node, MessageUpdateEvent.class);
            case "MESSAGE_DELETE": return p.getCodec().treeToValue(node, MessageDeleteEvent.class);
            case "MESSAGE_DELETE_BULK": return p.getCodec().treeToValue(node, MessageDeleteBulkEvent.class);
            case "MESSAGE_REACTION_ADD": return p.getCodec().treeToValue(node, MessageReactionAddEvent.class);
            case "MESSAGE_REACTION_REMOVE": return p.getCodec().treeToValue(node, MessageReactionRemoveEvent.class);
            case "MESSAGE_REACTION_REMOVE_ALL": return p.getCodec().treeToValue(node, MessageReactionRemoveAllEvent.class);
            case "PRESENCE_UPDATE": return p.getCodec().treeToValue(node, PresenceUpdateEvent.class);
            case "TYPING_START": return p.getCodec().treeToValue(node, TypingStartEvent.class);
            case "USER_UPDATE": return p.getCodec().treeToValue(node, UserUpdateEvent.class);
            case "VOICE_STATE_UPDATE": return p.getCodec().treeToValue(node, VoiceStateUpdateEvent.class);
            case "VOICE_SERVER_UPDATE": return p.getCodec().treeToValue(node, VoiceServerUpdateEvent.class);
            case "WEBHOOKS_UPDATE": return p.getCodec().treeToValue(node, WebhooksUpdateEvent.class);
            default: throw new RuntimeException("Unknown type " + node.get("t").asText());
        }
    }
}
