package tv.v1x1.common.scanners.permission;

import tv.v1x1.common.dao.DAOPermissionDefinition;
import tv.v1x1.common.dto.db.PermissionDefinition;
import tv.v1x1.common.modules.Module;
import tv.v1x1.common.scanners.ClassScanner;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PermissionScanner extends ClassScanner {
    public static void scanClass(final Module<?, ?> module) {
        final Permissions permissions = getAnnotation(module.getClass(), Permissions.class);
        if(permissions == null)
            return;
        module.getInjector().getInstance(DAOPermissionDefinition.class).put(
                new PermissionDefinition(module.getName(), permissions.version(),
                        Arrays.stream(permissions.value()).map(
                                registeredPermission -> new PermissionDefinition.PermissionEntry(
                                        registeredPermission.node(),
                                        registeredPermission.displayName(),
                                        registeredPermission.description(),
                                        Arrays.asList(registeredPermission.defaultGroups())
                                )
                        ).collect(Collectors.toList())
                )
        );
    }
}
