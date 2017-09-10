package tv.v1x1.common.services.discord.dto.permissions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by naomi on 9/17/2017.
 */
public class PermissionSet {
    public static final long NO_PERMISSIONS        = 0x0000000000000000;
    public static final long CREATE_INSTANT_INVITE = 0x0000000000000001;
    public static final long KICK_MEMBERS          = 0x0000000000000002;
    public static final long BAN_MEMBERS           = 0x0000000000000004;
    public static final long ADMINISTRATOR         = 0x0000000000000008;
    public static final long MANAGE_CHANNELS       = 0x0000000000000010;
    public static final long MANAGE_GUILD          = 0x0000000000000020;
    public static final long ADD_REACTIONS         = 0x0000000000000040;
    public static final long VIEW_AUDIT_LOG        = 0x0000000000000080;
    /* UNUSED/UNDOCUMENTED                           0x0000000000000100 */
    /* UNUSED/UNDOCUMENTED                           0x0000000000000200 */
    public static final long READ_MESSAGES         = 0x0000000000000400;
    public static final long SEND_MESSAGES         = 0x0000000000000800;
    public static final long SEND_TTS_MESSAGES     = 0x0000000000001000;
    public static final long MANAGE_MESSAGES       = 0x0000000000002000;
    public static final long EMBED_LINKS           = 0x0000000000004000;
    public static final long ATTACH_FILES          = 0x0000000000008000;
    public static final long READ_MESSAGE_HISTORY  = 0x0000000000010000;
    public static final long MENTION_EVERYONE      = 0x0000000000020000;
    public static final long USE_EXTERNAL_EMOJIS   = 0x0000000000040000;
    /* UNUSED/UNDOCUMENTED                           0x0000000000080000 */
    public static final long CONNECT               = 0x0000000000100000;
    public static final long SPEAK                 = 0x0000000000200000;
    public static final long MUTE_MEMBERS          = 0x0000000000400000;
    public static final long DEAFEN_MEMBERS        = 0x0000000000800000;
    public static final long MOVE_MEMBERS          = 0x0000000001000000;
    public static final long USE_VAD               = 0x0000000002000000;
    public static final long CHANGE_NICKNAME       = 0x0000000004000000;
    public static final long MANAGE_NICKNAMES      = 0x0000000008000000;
    public static final long MANAGE_ROLES          = 0x0000000010000000;
    public static final long MANAGE_WEBHOOKS       = 0x0000000020000000;
    public static final long MANAGE_EMOJIS         = 0x0000000040000000;

    private boolean createInstantInvite;
    private boolean kickMembers;
    private boolean banMembers;
    private boolean administrator;
    private boolean manageChannels;
    private boolean manageGuild;
    private boolean addReactions;
    private boolean viewAuditLog;
    private boolean readMessages;
    private boolean sendMessages;
    private boolean sendTtsMessages;
    private boolean manageMessages;
    private boolean embedLinks;
    private boolean attachFiles;
    private boolean readMessageHistory;
    private boolean mentionEveryone;
    private boolean useExternalEmojis;
    private boolean connect;
    private boolean speak;
    private boolean muteMembers;
    private boolean deafenMembers;
    private boolean moveMembers;
    private boolean useVad;
    private boolean changeNickname;
    private boolean manageNicknames;
    private boolean manageRoles;
    private boolean manageWebhooks;
    private boolean manageEmojis;

    @JsonCreator
    public PermissionSet(final long permissions) {
        createInstantInvite = (permissions & CREATE_INSTANT_INVITE) == CREATE_INSTANT_INVITE;
        kickMembers         = (permissions & KICK_MEMBERS         ) == KICK_MEMBERS         ;
        banMembers          = (permissions & BAN_MEMBERS          ) == BAN_MEMBERS          ;
        administrator       = (permissions & ADMINISTRATOR        ) == ADMINISTRATOR        ;
        manageChannels      = (permissions & MANAGE_CHANNELS      ) == MANAGE_CHANNELS      ;
        manageGuild         = (permissions & MANAGE_GUILD         ) == MANAGE_GUILD         ;
        addReactions        = (permissions & ADD_REACTIONS        ) == ADD_REACTIONS        ;
        viewAuditLog        = (permissions & VIEW_AUDIT_LOG       ) == VIEW_AUDIT_LOG       ;
        readMessages        = (permissions & READ_MESSAGES        ) == READ_MESSAGES        ;
        sendMessages        = (permissions & SEND_MESSAGES        ) == SEND_MESSAGES        ;
        sendTtsMessages     = (permissions & SEND_TTS_MESSAGES    ) == SEND_TTS_MESSAGES    ;
        manageMessages      = (permissions & MANAGE_MESSAGES      ) == MANAGE_MESSAGES      ;
        embedLinks          = (permissions & EMBED_LINKS          ) == EMBED_LINKS          ;
        attachFiles         = (permissions & ATTACH_FILES         ) == ATTACH_FILES         ;
        readMessageHistory  = (permissions & READ_MESSAGE_HISTORY ) == READ_MESSAGE_HISTORY ;
        mentionEveryone     = (permissions & MENTION_EVERYONE     ) == MENTION_EVERYONE     ;
        useExternalEmojis   = (permissions & USE_EXTERNAL_EMOJIS  ) == USE_EXTERNAL_EMOJIS  ;
        connect             = (permissions & CONNECT              ) == CONNECT              ;
        speak               = (permissions & SPEAK                ) == SPEAK                ;
        muteMembers         = (permissions & MUTE_MEMBERS         ) == MUTE_MEMBERS         ;
        deafenMembers       = (permissions & DEAFEN_MEMBERS       ) == DEAFEN_MEMBERS       ;
        moveMembers         = (permissions & MOVE_MEMBERS         ) == MOVE_MEMBERS         ;
        useVad              = (permissions & USE_VAD              ) == USE_VAD              ;
        changeNickname      = (permissions & CHANGE_NICKNAME      ) == CHANGE_NICKNAME      ;
        manageNicknames     = (permissions & MANAGE_NICKNAMES     ) == MANAGE_NICKNAMES     ;
        manageRoles         = (permissions & MANAGE_ROLES         ) == MANAGE_ROLES         ;
        manageWebhooks      = (permissions & MANAGE_WEBHOOKS      ) == MANAGE_WEBHOOKS      ;
        manageEmojis        = (permissions & MANAGE_EMOJIS        ) == MANAGE_EMOJIS        ;
    }

    @JsonValue
    public long toLong() {
        return
                  (createInstantInvite ? CREATE_INSTANT_INVITE : NO_PERMISSIONS)
                | (kickMembers         ? KICK_MEMBERS          : NO_PERMISSIONS)
                | (banMembers          ? BAN_MEMBERS           : NO_PERMISSIONS)
                | (administrator       ? ADMINISTRATOR         : NO_PERMISSIONS)
                | (manageChannels      ? MANAGE_CHANNELS       : NO_PERMISSIONS)
                | (manageGuild         ? MANAGE_GUILD          : NO_PERMISSIONS)
                | (addReactions        ? ADD_REACTIONS         : NO_PERMISSIONS)
                | (viewAuditLog        ? VIEW_AUDIT_LOG        : NO_PERMISSIONS)
                | (readMessages        ? READ_MESSAGES         : NO_PERMISSIONS)
                | (sendMessages        ? SEND_MESSAGES         : NO_PERMISSIONS)
                | (sendTtsMessages     ? SEND_TTS_MESSAGES     : NO_PERMISSIONS)
                | (manageMessages      ? MANAGE_MESSAGES       : NO_PERMISSIONS)
                | (embedLinks          ? EMBED_LINKS           : NO_PERMISSIONS)
                | (attachFiles         ? ATTACH_FILES          : NO_PERMISSIONS)
                | (readMessageHistory  ? READ_MESSAGE_HISTORY  : NO_PERMISSIONS)
                | (mentionEveryone     ? MENTION_EVERYONE      : NO_PERMISSIONS)
                | (useExternalEmojis   ? USE_EXTERNAL_EMOJIS   : NO_PERMISSIONS)
                | (connect             ? CONNECT               : NO_PERMISSIONS)
                | (speak               ? SPEAK                 : NO_PERMISSIONS)
                | (muteMembers         ? MUTE_MEMBERS          : NO_PERMISSIONS)
                | (deafenMembers       ? DEAFEN_MEMBERS        : NO_PERMISSIONS)
                | (moveMembers         ? MOVE_MEMBERS          : NO_PERMISSIONS)
                | (useVad              ? USE_VAD               : NO_PERMISSIONS)
                | (changeNickname      ? CHANGE_NICKNAME       : NO_PERMISSIONS)
                | (manageNicknames     ? MANAGE_NICKNAMES      : NO_PERMISSIONS)
                | (manageRoles         ? MANAGE_ROLES          : NO_PERMISSIONS)
                | (manageWebhooks      ? MANAGE_WEBHOOKS       : NO_PERMISSIONS)
                | (manageEmojis        ? MANAGE_EMOJIS         : NO_PERMISSIONS)
                ;
    }

    public boolean canCreateInstantInvite() {
        return createInstantInvite;
    }

    public void setCreateInstantInvite(final boolean createInstantInvite) {
        this.createInstantInvite = createInstantInvite;
    }

    public boolean canKickMembers() {
        return kickMembers;
    }

    public void setKickMembers(final boolean kickMembers) {
        this.kickMembers = kickMembers;
    }

    public boolean canBanMembers() {
        return banMembers;
    }

    public void setBanMembers(final boolean banMembers) {
        this.banMembers = banMembers;
    }

    public boolean canAdministrator() {
        return administrator;
    }

    public void setAdministrator(final boolean administrator) {
        this.administrator = administrator;
    }

    public boolean canManageChannels() {
        return manageChannels;
    }

    public void setManageChannels(final boolean manageChannels) {
        this.manageChannels = manageChannels;
    }

    public boolean canManageGuild() {
        return manageGuild;
    }

    public void setManageGuild(final boolean manageGuild) {
        this.manageGuild = manageGuild;
    }

    public boolean canAddReactions() {
        return addReactions;
    }

    public void setAddReactions(final boolean addReactions) {
        this.addReactions = addReactions;
    }

    public boolean canViewAuditLog() {
        return viewAuditLog;
    }

    public void setViewAuditLog(final boolean viewAuditLog) {
        this.viewAuditLog = viewAuditLog;
    }

    public boolean canReadMessages() {
        return readMessages;
    }

    public void setReadMessages(final boolean readMessages) {
        this.readMessages = readMessages;
    }

    public boolean canSendMessages() {
        return sendMessages;
    }

    public void setSendMessages(final boolean sendMessages) {
        this.sendMessages = sendMessages;
    }

    public boolean canSendTtsMessages() {
        return sendTtsMessages;
    }

    public void setSendTtsMessages(final boolean sendTtsMessages) {
        this.sendTtsMessages = sendTtsMessages;
    }

    public boolean canManageMessages() {
        return manageMessages;
    }

    public void setManageMessages(final boolean manageMessages) {
        this.manageMessages = manageMessages;
    }

    public boolean canEmbedLinks() {
        return embedLinks;
    }

    public void setEmbedLinks(final boolean embedLinks) {
        this.embedLinks = embedLinks;
    }

    public boolean canAttachFiles() {
        return attachFiles;
    }

    public void setAttachFiles(final boolean attachFiles) {
        this.attachFiles = attachFiles;
    }

    public boolean canReadMessageHistory() {
        return readMessageHistory;
    }

    public void setReadMessageHistory(final boolean readMessageHistory) {
        this.readMessageHistory = readMessageHistory;
    }

    public boolean canMentionEveryone() {
        return mentionEveryone;
    }

    public void setMentionEveryone(final boolean mentionEveryone) {
        this.mentionEveryone = mentionEveryone;
    }

    public boolean canUseExternalEmojis() {
        return useExternalEmojis;
    }

    public void setUseExternalEmojis(final boolean useExternalEmojis) {
        this.useExternalEmojis = useExternalEmojis;
    }

    public boolean canConnect() {
        return connect;
    }

    public void setConnect(final boolean connect) {
        this.connect = connect;
    }

    public boolean canSpeak() {
        return speak;
    }

    public void setSpeak(final boolean speak) {
        this.speak = speak;
    }

    public boolean canMuteMembers() {
        return muteMembers;
    }

    public void setMuteMembers(final boolean muteMembers) {
        this.muteMembers = muteMembers;
    }

    public boolean canDeafenMembers() {
        return deafenMembers;
    }

    public void setDeafenMembers(final boolean deafenMembers) {
        this.deafenMembers = deafenMembers;
    }

    public boolean canMoveMembers() {
        return moveMembers;
    }

    public void setMoveMembers(final boolean moveMembers) {
        this.moveMembers = moveMembers;
    }

    public boolean canUseVad() {
        return useVad;
    }

    public void setUseVad(final boolean useVad) {
        this.useVad = useVad;
    }

    public boolean canChangeNickname() {
        return changeNickname;
    }

    public void setChangeNickname(final boolean changeNickname) {
        this.changeNickname = changeNickname;
    }

    public boolean canManageNicknames() {
        return manageNicknames;
    }

    public void setManageNicknames(final boolean manageNicknames) {
        this.manageNicknames = manageNicknames;
    }

    public boolean canManageRoles() {
        return manageRoles;
    }

    public void setManageRoles(final boolean manageRoles) {
        this.manageRoles = manageRoles;
    }

    public boolean canManageWebhooks() {
        return manageWebhooks;
    }

    public void setManageWebhooks(final boolean manageWebhooks) {
        this.manageWebhooks = manageWebhooks;
    }

    public boolean canManageEmojis() {
        return manageEmojis;
    }

    public void setManageEmojis(final boolean manageEmojis) {
        this.manageEmojis = manageEmojis;
    }
}
