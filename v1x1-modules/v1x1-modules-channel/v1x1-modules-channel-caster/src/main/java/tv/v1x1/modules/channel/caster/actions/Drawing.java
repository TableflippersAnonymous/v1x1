package tv.v1x1.modules.channel.caster.actions;

import tv.v1x1.modules.channel.caster.CreativeDescriptor;

/**
 * @author Josh
 */
public class Drawing extends Action {
    final private String verb;

    public Drawing() {
        super();
        this.verb = "drawing";
    }

    public Drawing(final String verb) {
        super();
        this.verb = verb;
    }

    @Override
    public String getLine() {
        final StringBuilder sb = new StringBuilder();
        if(descriptors.contains(CreativeDescriptor.DIGITAL))
            sb.append("digitally ");
        sb.append(verb);
        if(descriptors.contains(CreativeDescriptor.ANIME))
            sb.append(" anime");
        if(descriptors.contains(CreativeDescriptor.COMICS))
            sb.append(" comics");
        return sb.toString();
    }

    @Override
    public boolean isAllowed(final CreativeDescriptor descriptor) {
        switch(descriptor) {
            case ANIME:
            case COMICS:
            case DIGITAL:
                return true;
            default:
                return false;
        }
    }
}
