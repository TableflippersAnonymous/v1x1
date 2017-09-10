package tv.v1x1.common.services.discord.dto.guild;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.user.User;

import java.util.List;

/**
 * Created by naomi on 9/10/2017.
 */
public class GuildMemberWithGuildId extends GuildMember {
    @JsonProperty("guild_id")
    private String guildId;

    public GuildMemberWithGuildId() {
    }

    public GuildMemberWithGuildId(final User user, final String nick, final List<String> roleIds, final String joinedAt,
                                  final boolean deaf, final boolean mute, final String guildId) {
        super(user, nick, roleIds, joinedAt, deaf, mute);
        this.guildId = guildId;
    }

    public String getGuildId() {
        return guildId;
    }

    public void setGuildId(final String guildId) {
        this.guildId = guildId;
    }
}
