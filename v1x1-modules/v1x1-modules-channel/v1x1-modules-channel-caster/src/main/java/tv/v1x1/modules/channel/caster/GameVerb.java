package tv.v1x1.modules.channel.caster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.modules.channel.caster.actions.Action;
import tv.v1x1.modules.channel.caster.actions.Drawing;
import tv.v1x1.modules.channel.caster.actions.GenericAction;
import tv.v1x1.modules.channel.caster.actions.GenericGerundAction;
import tv.v1x1.modules.channel.caster.actions.Painting;
import tv.v1x1.modules.channel.caster.actions.Performing;
import tv.v1x1.modules.channel.caster.actions.Working;

import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Josh
 */
public class GameVerb {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static String getVerb(final String game) {
        /*
        Gaming Talk Shows
        Creative - Special
        Music - Unofficial
        Twitch Plays
        Social Eating
         */
        switch(game.toLowerCase()) {
            case "creative": return "being creative";
            case "twitch plays":
            case "gaming talk shows": return "hosting " + game;
            case "music": return "performing music";
            case "social eating": return "social eating";
            default: return "playing " + game;
        }
    }

    public static String getCreativeVerb(final String title) {

        final String[] words = title.split(" ");
        Action action = null;
        Set<CreativeDescriptor> descriptors = new HashSet<>();
        for(String word : words) {
            if(!word.startsWith("#")) continue;
            if(word.length() < 2) continue;
            LOG.debug("Parsing hashtag {}", word);
            if(action == null)
                action = getActionFromString(word.substring(1));
            try {
                descriptors.add(CreativeDescriptor.valueOf(word.toUpperCase().substring(1)));
            } catch (IllegalArgumentException ex) {
                // do nothing
            }
        }
        if(action == null)
            action = new GenericAction("being creative");
        for(CreativeDescriptor descriptor : descriptors)
            action.addDescriptor(descriptor);
        return action.getLine();
    }

    private static Action getActionFromString(final String word) {
        final String lowerWord = word.toLowerCase();
        switch(lowerWord) {
            case "drawing":
                return new Drawing();
            case "illustration":
                return new Drawing("illustrating");
            case "sketch":
                return new Drawing("sketching");
            case "digitalillustration":
                return new Drawing("illustrating").with(CreativeDescriptor.DIGITAL);
            case "animation":
                return new Drawing("animating");
            case "painting":
                return new Painting();
            case "watercolor":
                return new Painting().with(CreativeDescriptor.WATERCOLOR);
            case "bodyart":
            case "facepaint":
                return new GenericAction("doing body art");
            case "pixelart":
                return new GenericAction("creating pixel art");
            case "fiberarts":
                return new GenericAction("making fiber arts")
            case "vector":
                return new GenericAction("creating vector art");
            case "music":
            case "theater": // should probably be broken out
                return new Performing(lowerWord);
            case "food":
                return new GenericAction("cooking food");
            case "comedy":
                return new GenericAction("making people laugh");
            case "magic":
                return new GenericAction("showing off magic tricks");
            case "dance": // XXX can have unofficial modifiers
                return new GenericAction("dancing");
            case "robotics":
                return new GenericAction("tinkering with robots");
            case "electronics":
                return new GenericAction("tinkering with electronics");
            case "crossstitch": // XXX may have modifiers
                return new GenericAction("cross-stitching");
            case "voiceacting":
                return new GenericAction("voice acting");
            case "programming":
                return new GenericAction("programming");
            case "gamedev":
                return new GenericAction("programming video games");
            case "editing":
                return new GenericAction("editing videos");
            case "chainmaille":
                return new GenericAction("working with chainmaille");
            case "cosplay": // XXX can have modifiers
                return new GenericAction("cosplaying");
            case "design":
            case "sculpting": // XXX can have modifiers
            case "writing":
            case "brewing":
            case "sewing":
            case "papercraft":
                return new GenericGerundAction(lowerWord);
            case "woodworking":
            case "metalworking":
            case "leatherwork":
                return new Working(lowerWord.substring(0, lowerWord.indexOf("work")));
            case "pcbuilding":
                return new GenericAction("building a PC");
            case "automotive":
                return new GenericAction("tinkering with autos");
            case "3dmodeling":
                return new GenericAction("3D modeling");
            default: return null;
        }
    }

    public enum CreativeGroup {
        DESIGNDOLLS, ARTSTATION
    }
}