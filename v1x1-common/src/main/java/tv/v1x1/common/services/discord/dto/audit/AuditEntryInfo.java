package tv.v1x1.common.services.discord.dto.audit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import tv.v1x1.common.services.discord.dto.channel.OverwriteType;

/**
 * Created by naomi on 9/16/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuditEntryInfo {
    @JsonProperty("delete_member_days")
    @JsonDeserialize(converter = StringToIntegerConverter.class)
    @JsonSerialize(converter = IntegerToStringConverter.class)
    private Integer deleteMemberDays;
    @JsonProperty("members_removed")
    @JsonDeserialize(converter = StringToIntegerConverter.class)
    @JsonSerialize(converter = IntegerToStringConverter.class)
    private Integer membersRemoved;
    @JsonProperty("channel_id")
    private String channelId;
    @JsonProperty
    @JsonDeserialize(converter = StringToIntegerConverter.class)
    @JsonSerialize(converter = IntegerToStringConverter.class)
    private Integer count;
    @JsonProperty
    private String id;
    @JsonProperty
    private OverwriteType type;
    @JsonProperty("role_name")
    private String roleName;

    public AuditEntryInfo() {
    }

    public AuditEntryInfo(final Integer deleteMemberDays, final Integer membersRemoved, final String channelId,
                          final Integer count, final String id, final OverwriteType type, final String roleName) {
        this.deleteMemberDays = deleteMemberDays;
        this.membersRemoved = membersRemoved;
        this.channelId = channelId;
        this.count = count;
        this.id = id;
        this.type = type;
        this.roleName = roleName;
    }

    public Integer getDeleteMemberDays() {
        return deleteMemberDays;
    }

    public void setDeleteMemberDays(final Integer deleteMemberDays) {
        this.deleteMemberDays = deleteMemberDays;
    }

    public Integer getMembersRemoved() {
        return membersRemoved;
    }

    public void setMembersRemoved(final Integer membersRemoved) {
        this.membersRemoved = membersRemoved;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(final String channelId) {
        this.channelId = channelId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(final Integer count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public OverwriteType getType() {
        return type;
    }

    public void setType(final OverwriteType type) {
        this.type = type;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(final String roleName) {
        this.roleName = roleName;
    }
}
