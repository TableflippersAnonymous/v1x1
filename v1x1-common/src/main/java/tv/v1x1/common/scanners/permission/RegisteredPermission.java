package tv.v1x1.common.scanners.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RegisteredPermission {
    String node();
    String displayName();
    String description();
    DefaultGroup[] defaultGroups() default {};
}
