package tv.v1x1.modules.channel.wasm.vm;

import tv.v1x1.modules.channel.wasm.vm.types.I32;

import java.util.Optional;

public class MemoryType extends Limits {
    public MemoryType(final I32 min, final Optional<I32> max) {
        super(min, max);
    }
}
