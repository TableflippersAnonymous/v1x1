package tv.v1x1.modules.channel.wasm.api;

import tv.v1x1.modules.channel.wasm.ExecutionEnvironment;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.io.IOException;
import java.util.UUID;

public class V1x1RequestFilter implements ClientRequestFilter {
    private final ExecutionEnvironment executionEnvironment;

    public V1x1RequestFilter(final ExecutionEnvironment executionEnvironment) {
        this.executionEnvironment = executionEnvironment;
    }

    @Override
    public void filter(final ClientRequestContext requestContext) throws IOException {
        requestContext.getHeaders().putSingle("User-Agent",
                "v1x1UserScript/1.0 (+https://us.v1x1.tv/useragent; " +
                        "tenantid: " + executionEnvironment.getTenant().getId().getValue() + "; " +
                        "scripthash: " + executionEnvironment.getConfigurationHash());
        requestContext.getHeaders().putSingle("X-V1X1-Tenant-Id", executionEnvironment.getTenant().getId().getValue().toString());
        requestContext.getHeaders().putSingle("X-V1X1-Request-Id", UUID.randomUUID().toString());
        requestContext.getHeaders().putSingle("X-V1X1-Script-Hash", executionEnvironment.getConfigurationHash());
    }
}
