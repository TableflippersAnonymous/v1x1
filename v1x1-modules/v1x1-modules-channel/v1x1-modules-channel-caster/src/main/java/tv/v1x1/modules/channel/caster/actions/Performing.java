package tv.v1x1.modules.channel.caster.actions;

import tv.v1x1.modules.channel.caster.CreativeDescriptor;

/**
 * @author Josh
 */
public class Performing extends Action {
    private final String show;

    public Performing(final String show) {
        this.show = show;
    }

    @Override
    public String getLine() {
        return "performing " + show;
    }

    @Override
    public boolean isAllowed(final CreativeDescriptor descriptor) {
        return false;
    }
}
