package tv.v1x1.common.util.commands.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by jcarter on 1/28/17.
 */
@Target({METHOD}) @Retention(RUNTIME)
public @interface Command {
    String[] value();
    String[] permissions();
    String usage();
    String description();
    String help();
    int minArgs() default 0;
    int maxArgs() default -1;
}
