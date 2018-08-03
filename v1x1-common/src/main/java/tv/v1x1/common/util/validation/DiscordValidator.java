package tv.v1x1.common.util.validation;

/**
 * @author Josh
 */
public class DiscordValidator {
    public static boolean isSnowflake(final String arg) {
        // https://discordapp.com/developers/docs/reference#snowflakes
        try {
            Long.valueOf(arg);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
