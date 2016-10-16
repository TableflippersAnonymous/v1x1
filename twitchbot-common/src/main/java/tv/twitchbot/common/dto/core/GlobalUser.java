package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.proto.core.UserOuterClass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by cobi on 10/16/2016.
 */
public class GlobalUser {
    public static GlobalUser fromProto(UserOuterClass.GlobalUser proto) {
        List<User> list = new ArrayList<>();
        UUID uuid = UUID.fromProto(proto.getId());
        GlobalUser globalUser = new GlobalUser(uuid, list);
        list.addAll(proto.getEntriesList().stream().map(entry -> User.fromProto(globalUser, entry)).collect(Collectors.toList()));
        return globalUser;
    }

    private UUID id;
    private List<User> entries;

    public GlobalUser(UUID id, List<User> entries) {
        this.id = id;
        this.entries = entries;
    }

    public UUID getId() {
        return id;
    }

    public List<User> getEntries() {
        return entries;
    }

    public UserOuterClass.GlobalUser toProto() {
        return UserOuterClass.GlobalUser.newBuilder()
                .setId(id.toProto())
                .addAllEntries(entries.stream().map(User::toProtoGlobalUserEntry).collect(Collectors.toList()))
                .build();
    }

    public tv.twitchbot.common.dto.db.GlobalUser toDB() {
        return new tv.twitchbot.common.dto.db.GlobalUser(id.getValue(), entries.stream().map(User::toDBGlobalUser).collect(Collectors.toList()));
    }
}
