package tv.v1x1.modules.channel.wasm.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import tv.v1x1.common.modules.BasicUserConfiguration;
import tv.v1x1.common.scanners.config.*;

import java.util.LinkedHashMap;
import java.util.Map;

@ModuleConfig("wasm")
@DisplayName("Custom Scripts")
@Description("Allow your bot to execute custom-written scripts in WebAssembly")
@Version(0)
public class WebAssemblyUserConfiguration extends BasicUserConfiguration {
    @DisplayName("Modules")
    @Description("List of WebAssembly Modules")
    @Type(ConfigType.COMPLEX_STRING_MAP)
    @ComplexType(ModuleUserConfiguration.class)
    @JsonProperty("modules")
    private Map<String, ModuleUserConfiguration> modules = new LinkedHashMap<>();

    public Map<String, ModuleUserConfiguration> getModules() {
        return modules;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final WebAssemblyUserConfiguration that = (WebAssemblyUserConfiguration) o;
        return Objects.equal(modules, that.modules);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(modules);
    }
}
