package tv.v1x1.modules.core.api.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.dropwizard.Configuration;

/**
 * Created by cobi on 10/24/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiConfiguration extends Configuration {
}
