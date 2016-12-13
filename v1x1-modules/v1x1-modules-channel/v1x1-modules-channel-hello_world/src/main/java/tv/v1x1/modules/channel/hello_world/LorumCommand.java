package tv.v1x1.modules.channel.hello_world;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;

import java.util.List;

/**
 * @author Josh
 */
public class LorumCommand extends Command {
    private static String LORUM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam bibendum feugiat" +
            " enim et venenatis. Pellentesque blandit porta condimentum. Cras facilisis vel libero ut dictum. Fusce aliq" +
            "uet ligula sit amet metus pharetra, eu auctor nibh sodales. Nunc non porttitor magna. Aenean molestie purus" +
            " nibh, et porttitor mi venenatis sit amet. Duis sit amet faucibus magna. Nulla facilisi. Duis et tortor dap" +
            "ibus, malesuada diam euismod, condimentum erat. Cras gravida vehicula tristique. Mauris euismod felis ac ar" +
            "cu accumsan, et tristique lorem cursus. Cras et accumsan tellus. Nunc et augue ac neque hendrerit varius at" +
            " ac ex. Nullam bibendum turpis eu est gravida, et finibus lorem fermentum. Fusce lorem dui, scelerisque in " +
            "suscipit vel, tincidunt eu orci. Phasellus pharetra mi et nisl tincidunt pellentesque ac sit amet sapien. C" +
            "ras porttitor mauris non diam commodo lacinia. Nam rutrum dapibus lectus id condimentum. Fusce eget posuere" +
            " nunc. Mauris quis elementum leo, quis ullamcorper magna. Vestibulum ut viverra leo, et convallis ipsum. Ut" +
            " rhoncus tortor a molestie venenatis. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Class aptent" +
            " taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Nunc cursus dictum posuere." +
            " Etiam luctus interdum tellus vel venenatis. Phasellus ut malesuada massa. Suspendisse felis est, suscipit " +
            "eget aliquam at, ultrices faucibus metus. Cras maximus quam risus, nec blandit lorem ultricies et. Sed vel " +
            "metus et lacus fringilla aliquet. Suspendisse porta mattis facilisis. In et suscipit libero. Vivamus cursus" +
            " a leo at tristique. Etiam blandit neque ut ante tristique varius. Phasellus porttitor tempor diam nec sagi" +
            "ttis. Donec eleifend sodales urna, non varius tortor auctor eget. Curabitur arcu justo, mollis vitae erat q" +
            "uis, ultricies fermentum dui. Maecenas non magna in tortor pretium tristique. Quisque iaculis tempus interd" +
            "um. Nunc condimentum odio sed viverra rhoncus. Sed suscipit nisl mollis, placerat elit ac, iaculis metus.";
    private HelloWorld module;
    LorumCommand(final HelloWorld module) {
        this.module = module;
    }
    @Override
    public List<String> getCommands() {
        return ImmutableList.of("lorum");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return ImmutableList.of(new Permission("hello.use"));
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final int length;
        try {
            length = Integer.valueOf(args.get(0));
            if(length < 1) {
                Chat.i18nMessage(module, chatMessage.getChannel(), "lorum.toosmall");
                return;
            } else if(length > 2000) {
                Chat.i18nMessage(module, chatMessage.getChannel(), "lorum.toobig");
            }
        } catch (NumberFormatException ex) {
            Chat.i18nMessage(module, chatMessage.getChannel(), "lorum.badnumber");
            return;
        }
        Chat.message(module, chatMessage.getChannel(), LORUM_IPSUM.substring(0, length));
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public int getMaxArgs() {
        return 1;
    }

    @Override
    public void handleArgMismatch(final ChatMessage chatMessage, final String command, final List<String> args) {
        Chat.i18nMessage(module, chatMessage.getChannel(), "lorum.invalidargs");
    }

    @Override
    public void handleNoPermissions(final ChatMessage chatMessage, final String command, final List<String> args) {
        Chat.i18nMessage(module, chatMessage.getChannel(), "lorum.noperms");
    }
}
