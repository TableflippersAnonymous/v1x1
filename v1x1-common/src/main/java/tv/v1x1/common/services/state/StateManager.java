package tv.v1x1.common.services.state;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Josh
 */
@Singleton
public class StateManager {
    private final DisplayNameService displayNameService;
    private final MembershipService membershipService;

    @Inject
    public StateManager(final DisplayNameService displayNameService, final MembershipService membershipService) {
        this.displayNameService = displayNameService;
        this.membershipService = membershipService;
    }

    public DisplayNameService getDisplayNameService() {
        return displayNameService;
    }

    public MembershipService getMembershipService() {
        return membershipService;
    }
}
