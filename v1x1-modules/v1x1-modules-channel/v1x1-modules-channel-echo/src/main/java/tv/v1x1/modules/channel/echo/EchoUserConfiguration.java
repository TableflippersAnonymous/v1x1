package tv.v1x1.modules.channel.echo;

import tv.v1x1.common.modules.BasicUserConfiguration;
import tv.v1x1.common.scanners.config.Description;
import tv.v1x1.common.scanners.config.DisplayName;
import tv.v1x1.common.scanners.config.ModuleConfig;
import tv.v1x1.common.scanners.config.Permission;
import tv.v1x1.common.scanners.config.TenantPermission;
import tv.v1x1.common.scanners.config.Version;

/**
 * @author Josh
 */
@ModuleConfig("echo")
@DisplayName("Echo")
@Description("This is a module used for testing.  It announces many events in the channel.")
@TenantPermission(Permission.NONE)
@Version(0)
public class EchoUserConfiguration extends BasicUserConfiguration {
}
