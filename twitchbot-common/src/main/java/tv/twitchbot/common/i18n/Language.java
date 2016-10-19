package tv.twitchbot.common.i18n;

import tv.twitchbot.common.dto.core.Module;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author Josh
 */
public abstract class Language {
    private static class Pair<T, U> {
        T left;
        U right;
        Pair(T left, U right) {
            this.left = left;
            this.right = right;
        }
    }

    public abstract String message(Module module, String key, Map<String, Object> parameters);
    public abstract void set(Module module, String key, String message);

    protected String format(String string, Map<String, Object> parameters) {
        if(string.isEmpty()) return "<empty message>";
        StringBuilder output = new StringBuilder(string.length());
        char[] inputArray = string.toCharArray();
        int position = 0;
        int blind = 0;
        for(; position < inputArray.length; ++position) {
            char i = inputArray[position];
            if(blind > 0) {
                --blind;
                output.append(i);
                continue;
            }
            if(i == '%') {
                Pair<String, Integer> tokenAndPos = scanForChar(inputArray, position, '%');
                if(tokenAndPos.left == null) {
                    blind = tokenAndPos.right - position;
                    output.append(i);
                    continue;
                }
                output.append(getToken(tokenAndPos.left, parameters));
                position = tokenAndPos.right;
                continue;
            }
            output.append(i);
        }
        return output.toString();
    }

    private Pair<String, Integer> scanForChar(char[] input, int position, char what) {
        StringBuilder ret = new StringBuilder(10);
        for(++position; position < input.length; ++position) {
            if(input[position] == what) return new Pair<>(ret.toString(), position);
            if(input[position] == ' ') break; // we don't support spaces in tokens
            ret.append(input[position]);
        }
        return new Pair<>(null, position);
    }

    private String getToken(String token, Map<String, Object> tokens) {
        if(token.isEmpty())
            return "%%";
        StringBuilder output = new StringBuilder();
        Object tokenVal = tokens.get(token);
        if(tokenVal == null) {
            output.append('%');
            output.append(token);
            output.append('%');
            return output.toString();
        }
        String formatType = handleFormat(tokenVal);
        output.append("{0");
        if(formatType != null) output.append(formatType);
        output.append('}');
        return MessageFormat.format(output.toString(), tokenVal);
    }

    private String handleFormat(Object tokenVal) {
        if(tokenVal instanceof String) return null; // ¯\_(ツ)_/¯
        if(tokenVal instanceof Number) return ",number";
        if(tokenVal instanceof Date) return ",date";
        return null;
    }

    protected String getKey(Module module, String key) {
        return I18n.getKey(module, key);
    }
}