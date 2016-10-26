package tv.v1x1.common.services.state;

/**
 * @author Josh
 */
public class StateManager {
    private DisplayNameService displayNameService;
    private MembershipService membershipService;

    public StateManager() {
        displayNameService = new DisplayNameService();
        membershipService = new MembershipService();
    }

    public DisplayNameService getDisplayNameService() {
        return displayNameService;
    }

    public MembershipService getMembershipService() {
        return membershipService;
    }
}
