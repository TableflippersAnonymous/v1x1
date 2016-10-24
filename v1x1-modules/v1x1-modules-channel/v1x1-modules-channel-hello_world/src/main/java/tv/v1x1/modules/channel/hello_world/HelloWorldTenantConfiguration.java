package tv.v1x1.modules.channel.hello_world;

import tv.v1x1.common.config.Description;
import tv.v1x1.common.config.DisplayName;
import tv.v1x1.common.config.ModuleConfig;
import tv.v1x1.common.config.Permission;
import tv.v1x1.common.config.TenantPermission;
import tv.v1x1.common.config.Version;
import tv.v1x1.common.modules.BasicTenantConfiguration;

/**
 * Created by Josh on 2016-10-06.
 */
@ModuleConfig("hello_world")
@DisplayName("Hello World")
@Description("This module has a few commands which allow you to test basic functionality of v1x1.")
@TenantPermission(Permission.NONE)
@Version(0)
public class HelloWorldTenantConfiguration extends BasicTenantConfiguration {
}
