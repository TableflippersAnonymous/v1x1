package tv.v1x1.common.util.commands.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by jcarter on 1/28/17.
 */
@Target({TYPE}) @Retention(RUNTIME)
public @interface CommandSet {
    Class<?> parent() default Object.class;
    String value() default "_ROOT_";
}
