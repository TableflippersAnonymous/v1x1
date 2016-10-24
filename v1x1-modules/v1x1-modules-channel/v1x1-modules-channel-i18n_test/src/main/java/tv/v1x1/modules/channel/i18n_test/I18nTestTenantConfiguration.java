package tv.v1x1.modules.channel.i18n_test;

import tv.v1x1.common.config.Description;
import tv.v1x1.common.config.DisplayName;
import tv.v1x1.common.config.ModuleConfig;
import tv.v1x1.common.config.Permission;
import tv.v1x1.common.config.TenantPermission;
import tv.v1x1.common.config.Version;
import tv.v1x1.common.modules.BasicTenantConfiguration;

/**
 * @author Josh
 */
@ModuleConfig("i18n_test")
@DisplayName("I18n Test")
@Description("This module allows testing the internationalization code.")
@TenantPermission(Permission.NONE)
@Version(0)
public class I18nTestTenantConfiguration extends BasicTenantConfiguration {
}
