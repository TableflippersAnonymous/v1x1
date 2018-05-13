package tv.v1x1.modules.channel.caster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * Gets a word summary of a stream based on the game
 * This may transition into an instanced class if we try to get more detailed than "parse game"
 * @author Josh
 */
public class StreamActivity {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    public static String getVerb(final String game) {
        /*
        Twitch:
            Talk Shows
            Creative
            Music
            Twitch Plays
            Social Eating
            Retro
        Mixer: https://watchbeam.zendesk.com/hc/en-us/articles/115001020363-Non-Gaming-Categories
         */
        switch(game.toLowerCase()) {
            // Shared
            case "creative": return "being creative";
            case "music": return "performing music";
            case "irl": return "streaming real life";
            // Twitch
            case "twitch plays":
            case "talk shows": return "hosting " + game;
            case "social eating": return "socially eating";
            case "retro": return "playing retro games";
            // Mixer
            case "art": return "making art";
            // ...
            // Default
            default: return "playing " + game;
        }
    }
}