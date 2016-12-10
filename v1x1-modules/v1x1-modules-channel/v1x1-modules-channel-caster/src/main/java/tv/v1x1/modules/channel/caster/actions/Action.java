package tv.v1x1.modules.channel.caster.actions;

import tv.v1x1.modules.channel.caster.CreativeDescriptor;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Josh
 */
public abstract class Action {
    protected Set<CreativeDescriptor> descriptors = new HashSet<>();

    public abstract String getLine();
    public abstract boolean isAllowed(final CreativeDescriptor descriptor);

    public void addDescriptor(final CreativeDescriptor descriptor) {
        if(isAllowed(descriptor))
            descriptors.add(descriptor);
    }

    public Action with(final CreativeDescriptor descriptor) {
        addDescriptor(descriptor);
        return this;
    }
}
