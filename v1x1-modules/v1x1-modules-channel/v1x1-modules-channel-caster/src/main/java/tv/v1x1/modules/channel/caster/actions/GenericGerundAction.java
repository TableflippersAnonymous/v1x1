package tv.v1x1.modules.channel.caster.actions;

/**
 * @author Josh
 */
public class GenericGerundAction extends GenericAction {

    public GenericGerundAction(final String verb) {
        if(verb.endsWith("ing"))
            action = verb;
        else
            action = verb + "ing";
    }
}
