package tv.v1x1.modules.channel.hello_world;

import tv.v1x1.common.modules.BasicUserConfiguration;
import tv.v1x1.common.scanners.config.Description;
import tv.v1x1.common.scanners.config.DisplayName;
import tv.v1x1.common.scanners.config.ModuleConfig;
import tv.v1x1.common.scanners.config.Permission;
import tv.v1x1.common.scanners.config.TenantPermission;
import tv.v1x1.common.scanners.config.Version;

/**
 * Created by Josh on 2016-10-06.
 */
@ModuleConfig("hello_world")
@DisplayName("Hello World")
@Description("This module has a few commands which allow you to test basic functionality of v1x1.")
@TenantPermission(Permission.NONE)
@Version(0)
public class HelloWorldUserConfiguration extends BasicUserConfiguration {
}
