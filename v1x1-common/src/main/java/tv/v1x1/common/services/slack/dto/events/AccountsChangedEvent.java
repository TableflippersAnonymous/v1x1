package tv.v1x1.common.services.slack.dto.events;

public class AccountsChangedEvent extends Event {
    public AccountsChangedEvent() {
        super("accounts_changed");
    }
}
