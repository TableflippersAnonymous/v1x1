package tv.v1x1.common.services.slack.dto.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ErrorEvent.class, name = "error"),
        @JsonSubTypes.Type(value = AccountsChangedEvent.class, name = "accounts_changed"),
        @JsonSubTypes.Type(value = BotAddedEvent.class, name = "bot_added"),
        @JsonSubTypes.Type(value = BotChangedEvent.class, name = "bot_changed"),
        @JsonSubTypes.Type(value = ChannelArchiveEvent.class, name = "channel_archive"),
        @JsonSubTypes.Type(value = ChannelCreatedEvent.class, name = "channel_created"),
        @JsonSubTypes.Type(value = ChannelDeletedEvent.class, name = "channel_deleted"),
        @JsonSubTypes.Type(value = ChannelHistoryChangedEvent.class, name = "channel_history_changed"),
        @JsonSubTypes.Type(value = ChannelJoinedEvent.class, name = "channel_joined"),
        @JsonSubTypes.Type(value = ChannelLeftEvent.class, name = "channel_left"),
        @JsonSubTypes.Type(value = ChannelMarkedEvent.class, name = "channel_marked"),
        @JsonSubTypes.Type(value = ChannelRenameEvent.class, name = "channel_rename"),
        @JsonSubTypes.Type(value = ChannelUnarchiveEvent.class, name = "channel_unarchive"),
        @JsonSubTypes.Type(value = CommandsChangedEvent.class, name = "commands_changed"),
        @JsonSubTypes.Type(value = DndUpdatedEvent.class, name = "dnd_updated"),
        @JsonSubTypes.Type(value = DndUpdatedUserEvent.class, name = "dnd_updated_user"),
        @JsonSubTypes.Type(value = EmailDomainChangedEvent.class, name = "email_domain_changed"),
        @JsonSubTypes.Type(value = EmojiChangedEvent.class, name = "emoji_changed"),
        @JsonSubTypes.Type(value = FileChangedEvent.class, name = "file_change"),
        @JsonSubTypes.Type(value = FileCommentAddedEvent.class, name = "file_comment_added"),
        @JsonSubTypes.Type(value = FileCommentDeletedEvent.class, name = "file_comment_deleted"),
        @JsonSubTypes.Type(value = FileCommentEditedEvent.class, name = "file_comment_edited"),
        @JsonSubTypes.Type(value = FileCreatedEvent.class, name = "file_created"),
        @JsonSubTypes.Type(value = FileDeletedEvent.class, name = "file_deleted"),
        @JsonSubTypes.Type(value = FilePublicEvent.class, name = "file_public"),
        @JsonSubTypes.Type(value = FileSharedEvent.class, name = "file_shared"),
        @JsonSubTypes.Type(value = FileUnsharedEvent.class, name = "file_unshared"),
        @JsonSubTypes.Type(value = GoodbyeEvent.class, name = "goodbye"),
        @JsonSubTypes.Type(value = GroupArchiveEvent.class, name = "group_archive"),
        @JsonSubTypes.Type(value = GroupCloseEvent.class, name = "group_close"),
        @JsonSubTypes.Type(value = GroupHistoryChangedEvent.class, name = "group_history_changed"),
        @JsonSubTypes.Type(value = GroupJoinedEvent.class, name = "group_joined"),
        @JsonSubTypes.Type(value = GroupLeftEvent.class, name = "group_left"),
        @JsonSubTypes.Type(value = GroupMarkedEvent.class, name = "group_marked"),
        @JsonSubTypes.Type(value = GroupOpenEvent.class, name = "group_open"),
        @JsonSubTypes.Type(value = GroupRenameEvent.class, name = "group_rename"),
        @JsonSubTypes.Type(value = GroupUnarchiveEvent.class, name = "group_unarchive"),
        @JsonSubTypes.Type(value = HelloEvent.class, name = "hello"),
        @JsonSubTypes.Type(value = ImCloseEvent.class, name = "im_close"),
        @JsonSubTypes.Type(value = ImCreatedEvent.class, name = "im_created"),
        @JsonSubTypes.Type(value = ImHistoryChangedEvent.class, name = "im_history_changed"),
        @JsonSubTypes.Type(value = ImMarkedEvent.class, name = "im_marked"),
        @JsonSubTypes.Type(value = ImOpenEvent.class, name = "im_open"),
        @JsonSubTypes.Type(value = ManualPresenceChangeEvent.class, name = "manual_presence_change"),
        @JsonSubTypes.Type(value = MemberJoinedChannelEvent.class, name = "member_joined_channel"),
        @JsonSubTypes.Type(value = MemberLeftChannelEvent.class, name = "member_left_channel"),
        @JsonSubTypes.Type(value = MessageEvent.class, name = "message"),
        @JsonSubTypes.Type(value = PinAddedEvent.class, name = "pin_added"),
        @JsonSubTypes.Type(value = PinRemovedEvent.class, name = "pin_removed"),
        @JsonSubTypes.Type(value = PrefChangeEvent.class, name = "pref_change"),
        @JsonSubTypes.Type(value = PresenceChangeEvent.class, name = "presence_change"),
        @JsonSubTypes.Type(value = PresenceQueryEvent.class, name = "presence_query"),
        @JsonSubTypes.Type(value = PresenceSubEvent.class, name = "presence_sub"),
        @JsonSubTypes.Type(value = ReactionAddedEvent.class, name = "reaction_added"),
        @JsonSubTypes.Type(value = ReactionRemovedEvent.class, name = "reaction_removed"),
        @JsonSubTypes.Type(value = ReconnectUrlEvent.class, name = "reconnect_url"),
        @JsonSubTypes.Type(value = StarAddedEvent.class, name = "star_added"),
        @JsonSubTypes.Type(value = StarRemovedEvent.class, name = "star_removed"),
        @JsonSubTypes.Type(value = SubteamCreatedEvent.class, name = "subteam_created"),
        @JsonSubTypes.Type(value = SubteamMembersChangedEvent.class, name = "subteam_members_changed"),
        @JsonSubTypes.Type(value = SubteamSelfAddedEvent.class, name = "subteam_self_added"),
        @JsonSubTypes.Type(value = SubteamSelfRemovedEvent.class, name = "subteam_self_removed"),
        @JsonSubTypes.Type(value = SubteamUpdatedEvent.class, name = "subteam_updated"),
        @JsonSubTypes.Type(value = TeamDomainChangeEvent.class, name = "team_domain_change"),
        @JsonSubTypes.Type(value = TeamJoinEvent.class, name = "team_join"),
        @JsonSubTypes.Type(value = TeamMigrationStartedEvent.class, name = "team_migration_started"),
        @JsonSubTypes.Type(value = TeamPlanChangeEvent.class, name = "team_plan_change"),
        @JsonSubTypes.Type(value = TeamPrefChangeEvent.class, name = "team_pref_change"),
        @JsonSubTypes.Type(value = TeamProfileChangeEvent.class, name = "team_profile_change"),
        @JsonSubTypes.Type(value = TeamProfileDeleteEvent.class, name = "team_profile_delete"),
        @JsonSubTypes.Type(value = TeamProfileReorderEvent.class, name = "team_profile_reorder"),
        @JsonSubTypes.Type(value = TeamRenameEvent.class, name = "team_rename"),
        @JsonSubTypes.Type(value = UserChangeEvent.class, name = "user_change"),
        @JsonSubTypes.Type(value = UserTypingEvent.class, name = "user_typing")
})
public abstract class Event {
    @JsonProperty("type")
    private String type;

    public Event() {
    }

    public Event(final String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }
}
