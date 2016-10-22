package tv.v1x1.common.i18n;

import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.util.data.Pair;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author Josh
 */
public abstract class Language {
    public abstract String message(Module module, String key, Map<String, Object> parameters);
    public abstract void set(Module module, String key, String message);

    protected String format(final String string, final Map<String, Object> parameters) {
        if(string.isEmpty()) return "<empty message>";
        final StringBuilder output = new StringBuilder(string.length());
        final char[] inputArray = string.toCharArray();
        int position = 0;
        int blind = 0;
        for(; position < inputArray.length; ++position) {
            final char i = inputArray[position];
            if(blind > 0) {
                --blind;
                output.append(i);
                continue;
            }
            if(i == '%') {
                final Pair<String, Integer> tokenAndPos = scanForChar(inputArray, position, '%');
                if(tokenAndPos.getFirst() == null) {
                    blind = tokenAndPos.getSecond() - position;
                    output.append(i);
                    continue;
                }
                output.append(getToken(tokenAndPos.getFirst(), parameters));
                position = tokenAndPos.getSecond();
                continue;
            }
            output.append(i);
        }
        return output.toString();
    }

    private Pair<String, Integer> scanForChar(final char[] input, int position, final char what) {
        final StringBuilder ret = new StringBuilder(10);
        for(++position; position < input.length; ++position) {
            if(input[position] == what) return new Pair<>(ret.toString(), position);
            if(input[position] == ' ') break; // we don't support spaces in tokens
            ret.append(input[position]);
        }
        return new Pair<>(null, position);
    }

    private String getToken(final String token, final Map<String, Object> tokens) {
        if(token.isEmpty())
            return "%%";
        final StringBuilder output = new StringBuilder();
        final Object tokenVal = tokens.get(token);
        if(tokenVal == null) {
            output.append('%');
            output.append(token);
            output.append('%');
            return output.toString();
        }
        final String formatType = handleFormat(tokenVal);
        output.append("{0");
        if(formatType != null) output.append(formatType);
        output.append('}');
        return MessageFormat.format(output.toString(), tokenVal);
    }

    private String handleFormat(final Object tokenVal) {
        if(tokenVal instanceof String) return null; // ¯\_(ツ)_/¯
        if(tokenVal instanceof Number) return ",number";
        if(tokenVal instanceof Date) return ",date";
        return null;
    }

    protected String getKey(final Module module, final String key) {
        return I18n.getKey(module, key);
    }
}