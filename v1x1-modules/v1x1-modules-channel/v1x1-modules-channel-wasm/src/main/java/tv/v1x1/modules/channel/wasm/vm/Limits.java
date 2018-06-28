package tv.v1x1.modules.channel.wasm.vm;

import tv.v1x1.modules.channel.wasm.vm.types.I32;

import java.util.Optional;

public class Limits {
    private I32 min;
    private Optional<I32> max;

    public Limits(final I32 min, final Optional<I32> max) {
        this.min = min;
        this.max = max;
    }

    // https://webassembly.github.io/spec/core/valid/types.html#limits
    public boolean validate() {
        return max.map(i32 -> min.leU(i32) == I32.ONE)
                .orElse(true);
    }
}
