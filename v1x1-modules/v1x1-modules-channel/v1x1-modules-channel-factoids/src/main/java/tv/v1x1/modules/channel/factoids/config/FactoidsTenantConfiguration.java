package tv.v1x1.modules.channel.factoids.config;

import tv.v1x1.common.config.Description;
import tv.v1x1.common.config.DisplayName;
import tv.v1x1.common.config.ModuleConfig;
import tv.v1x1.common.config.Version;
import tv.v1x1.common.modules.BasicTenantConfiguration;

/**
 * @author Josh
 */
@ModuleConfig("factoids")
@DisplayName("Factoids")
@Description("This module adds the factoids -- or custom commands, if you will -- feature")
@Version(0)
public class FactoidsTenantConfiguration extends BasicTenantConfiguration {
}
