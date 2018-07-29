package tv.v1x1.modules.channel.wasm.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import tv.v1x1.common.scanners.config.*;

public class ModuleUserConfiguration {
    @JsonProperty("data")
    @DisplayName("WebAssembly File (.wasm)")
    @Type(ConfigType.FILE)
    @TenantPermission(Permission.WRITE_ONLY)
    private String data;

    public ModuleUserConfiguration() {
    }

    public ModuleUserConfiguration(final String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(final String data) {
        this.data = data;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ModuleUserConfiguration that = (ModuleUserConfiguration) o;
        return Objects.equal(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(data);
    }
}
