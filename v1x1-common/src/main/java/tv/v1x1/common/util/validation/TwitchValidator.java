package tv.v1x1.common.util.validation;

import java.util.regex.Pattern;

/**
 * @author Josh
 */
public class TwitchValidator {
    private static final Pattern USERNAME_REGEX = Pattern.compile("^[a-zA-Z0-9_]{4,25}$");
    public static boolean isValidUsername(final String username) {
        return USERNAME_REGEX.matcher(username).matches();
    }
}
