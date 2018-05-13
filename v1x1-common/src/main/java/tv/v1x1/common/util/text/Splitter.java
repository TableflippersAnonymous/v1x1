package tv.v1x1.common.util.text;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Josh
 */
public class Splitter {
    public static List<String> split(final int lineLength, final CharSequence separator, final String input) {
        if(input.length() <= lineLength || lineLength == -1)
            return ImmutableList.of(input); // Don't do any processing if we don't have to
        int length = lineLength - separator.length();
        final ArrayList<String> ret = new ArrayList<>();
        for(int pos = 0; pos < input.length(); ) {
            final StringBuilder sb = new StringBuilder(lineLength);
            if(pos != 0)
                sb.append(separator);
            int end = pos + length;
            if(end > input.length()) end = input.length();
            if(input.length() - end <= separator.length()) {
                sb.append(input, pos, input.length());
                end = input.length();
            } else {
                sb.append(input, pos, end);
                sb.append(separator);
            }
            ret.add(sb.toString());
            if(pos == 0) // we're now prefixing and suffixing lines with the separator, so it counts further against our limit
                length -= separator.length();
            pos = end;
        }
        return ret;
    }
}
