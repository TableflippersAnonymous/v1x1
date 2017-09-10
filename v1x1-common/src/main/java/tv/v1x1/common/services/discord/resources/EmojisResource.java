package tv.v1x1.common.services.discord.resources;

import tv.v1x1.common.services.discord.DiscordApi;
import tv.v1x1.common.services.discord.dto.emoji.CreateGuildEmojiRequest;
import tv.v1x1.common.services.discord.dto.emoji.Emoji;
import tv.v1x1.common.services.discord.dto.emoji.ModifyGuildEmojiRequest;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by cobi on 9/16/2017.
 */
public class EmojisResource {
    private final WebTarget guilds;

    public EmojisResource(final WebTarget guilds) {
        this.guilds = guilds;
    }

    public List<Emoji> listGuildEmojis(final String guildId) {
        return guilds.path(guildId).path("emojis")
                .request(DiscordApi.ACCEPT)
                .get()
                .readEntity(new GenericType<List<Emoji>>() {});
    }

    public Emoji getGuildEmoji(final String guildId, final String emojiId) {
        return guilds.path(guildId).path("emojis").path(emojiId)
                .request(DiscordApi.ACCEPT)
                .get()
                .readEntity(Emoji.class);
    }

    public Emoji createGuildEmoji(final String guildId, final CreateGuildEmojiRequest createGuildEmojiRequest) {
        return guilds.path(guildId).path("emojis")
                .request(DiscordApi.ACCEPT)
                .post(Entity.entity(createGuildEmojiRequest, MediaType.APPLICATION_JSON))
                .readEntity(Emoji.class);
    }

    public Emoji modifyGuildEmoji(final String guildId, final String emojiId,
                                  final ModifyGuildEmojiRequest modifyGuildEmojiRequest) {
        return guilds.path(guildId).path("emojis").path(emojiId)
                .request(DiscordApi.ACCEPT)
                .method("PATCH", Entity.entity(modifyGuildEmojiRequest,  MediaType.APPLICATION_JSON))
                .readEntity(Emoji.class);
    }

    public void deleteGuildEmoji(final String guildId, final String emojiId) {
        guilds.path(guildId).path("emojis").path(emojiId)
                .request(DiscordApi.ACCEPT)
                .delete();
    }
}
