package tv.v1x1.modules.channel.link_purger;

import tv.v1x1.common.config.Description;
import tv.v1x1.common.config.DisplayName;
import tv.v1x1.common.config.ModuleConfig;
import tv.v1x1.common.config.Version;
import tv.v1x1.common.modules.BasicTenantConfiguration;

/**
 * @author Josh
 */
@ModuleConfig("link_purger")
@DisplayName("Link Purger")
@Description("This module allows you to purge and/or timeout those who paste links.")
@Version(0)
public class LinkPurgerTenantConfiguration extends BasicTenantConfiguration {
}
