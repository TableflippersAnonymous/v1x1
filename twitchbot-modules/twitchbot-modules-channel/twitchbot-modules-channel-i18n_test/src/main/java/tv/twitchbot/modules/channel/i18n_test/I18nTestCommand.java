package tv.twitchbot.modules.channel.i18n_test;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import tv.twitchbot.common.dto.core.ChatMessage;
import tv.twitchbot.common.dto.core.Permission;
import tv.twitchbot.common.util.commands.Command;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Josh
 */
public class I18nTestCommand extends Command {
    I18nTest module;
    Map<String, Object> testData;

    I18nTestCommand(I18nTest module) {
        this.module = module;
        testData = new HashMap<>();
        testData.put("name", "SnoFox");
        testData.put("tinynum", .0250);
        testData.put("smallnum", 42);
        testData.put("bignum", 1000000);
        testData.put("date", LocalDate.now());
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("i18n");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return ImmutableList.of(new Permission("i18n.use"));
    }

    @Override
    public void run(ChatMessage chatMessage, String command, List<String> args) {
        module.language.set(module.toDto(), "userdefined", Joiner.on(' ').join(args));
        module.crsc.sendMessage(chatMessage.getChannel(), module.language.message(module.toDto(), "userdefined", testData));
    }
}
