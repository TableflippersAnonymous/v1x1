package tv.v1x1.modules.channel.link_purger;

import tv.v1x1.common.modules.BasicUserConfiguration;
import tv.v1x1.common.scanners.config.Description;
import tv.v1x1.common.scanners.config.DisplayName;
import tv.v1x1.common.scanners.config.ModuleConfig;
import tv.v1x1.common.scanners.config.Version;

/**
 * @author Josh
 */
@ModuleConfig("link_purger")
@DisplayName("Link Purger")
@Description("This module allows you to purge and/or timeout those who paste links.")
@Version(0)
public class LinkPurgerUserConfiguration extends BasicUserConfiguration {
}
