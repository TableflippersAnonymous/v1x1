package tv.v1x1.modules.core.api.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class GlobalUser {
    @JsonProperty
    private UUID id;
    @JsonProperty
    private List<User> users;

    public GlobalUser() {
    }

    public GlobalUser(final UUID id, final List<User> users) {
        this.id = id;
        this.users = users;
    }

    public GlobalUser(final tv.v1x1.common.dto.core.GlobalUser globalUser) {
        this(globalUser.getId().getValue(), globalUser.getEntries().stream().map(User::new).collect(Collectors.toList()));
    }

    public UUID getId() {
        return id;
    }

    public List<User> getUsers() {
        return users;
    }
}
