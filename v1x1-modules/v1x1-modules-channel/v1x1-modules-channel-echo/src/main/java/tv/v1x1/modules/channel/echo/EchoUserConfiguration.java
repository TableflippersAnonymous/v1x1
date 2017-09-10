package tv.v1x1.modules.channel.echo;

import tv.v1x1.common.config.Description;
import tv.v1x1.common.config.DisplayName;
import tv.v1x1.common.config.ModuleConfig;
import tv.v1x1.common.config.Permission;
import tv.v1x1.common.config.TenantPermission;
import tv.v1x1.common.config.Version;
import tv.v1x1.common.modules.BasicUserConfiguration;

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
