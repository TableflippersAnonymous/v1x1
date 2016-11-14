package tv.v1x1.common.util.text;

/**
 * @author Josh
 */
public class Shorten {

    public static String genPreview(final String message) {
        return genPreview(message, 15);
    }

    /**
     * Truncate a sentence at a word boundary, adding a "..." if we dropped words
     * @param message sentence to truncate
     * @param length desired character count to stop at
     * @return
     */
    public static String genPreview(final String message, final int length) {
        final String[] words = message.split(" ");
        final StringBuilder sb = new StringBuilder();
        int i = 0;
        for(; i < words.length; ++i) {
            if(i > 0) sb.append(' ');
            sb.append(words[i]);
            if(sb.length() > length)
                break;
        }
        if(i < (words.length-1)) sb.append("...");
        if(i == 0) return message.substring(0, length) +  "...";
        return sb.toString();
    }
}
