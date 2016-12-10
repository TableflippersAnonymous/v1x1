package tv.v1x1.modules.channel.caster.actions;

import tv.v1x1.modules.channel.caster.CreativeDescriptor;

/**
 * @author Josh
 */
public class Painting extends Action {
    @Override
    public String getLine() {
        final StringBuilder sb = new StringBuilder();
        if(descriptors.contains(CreativeDescriptor.DIGITAL))
            sb.append("digitally ");
        sb.append("painting");
        if(descriptors.contains(CreativeDescriptor.ANIME))
            sb.append(" anime");
        if(descriptors.contains(CreativeDescriptor.WATERCOLOR))
            sb.append(" with watercolor");
        return sb.toString();
    }

    @Override
    public boolean isAllowed(final CreativeDescriptor descriptor) {
        switch(descriptor) {
            case DIGITAL:
            case ANIME:
            case WATERCOLOR:
            case COMICS:
                return true;
            default:
                return false;
        }
    }
}
