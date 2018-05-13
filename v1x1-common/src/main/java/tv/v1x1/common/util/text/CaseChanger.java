package tv.v1x1.common.util.text;

public class CaseChanger {

    public static String titlecase(final String string) {
        final StringBuilder sb = new StringBuilder(string.length());
        boolean uppercaseNext = true;
        for(final char c : string.toCharArray()) {
            if(uppercaseNext) {
                uppercaseNext = false;
                sb.append(Character.toUpperCase(c));
                continue;
            }
            uppercaseNext = c == ' ';
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }
}
