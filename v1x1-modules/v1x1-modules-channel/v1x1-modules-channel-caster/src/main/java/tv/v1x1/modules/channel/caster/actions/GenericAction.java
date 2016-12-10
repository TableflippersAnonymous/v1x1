package tv.v1x1.modules.channel.caster.actions;

import tv.v1x1.modules.channel.caster.CreativeDescriptor;

/**
 * @author Josh
 */
public class GenericAction extends Action {
    protected String action;

    public GenericAction(final String actionLine) {
        action = actionLine;
    }
    protected GenericAction() {}

    @Override
    public String getLine() {
        return action;
    }

    @Override
    public boolean isAllowed(final CreativeDescriptor descriptor) {
        return false;
    }
}
