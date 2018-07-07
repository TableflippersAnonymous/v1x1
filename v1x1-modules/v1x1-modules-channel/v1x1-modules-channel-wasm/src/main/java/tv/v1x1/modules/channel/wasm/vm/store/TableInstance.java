package tv.v1x1.modules.channel.wasm.vm.store;

import tv.v1x1.modules.channel.wasm.vm.types.I32;

import java.util.List;
import java.util.Optional;

public class TableInstance {
    private List<Optional<I32>> elements;
    private final Optional<I32> max;

    public TableInstance(final List<Optional<I32>> elements, final Optional<I32> max) {
        this.elements = elements;
        this.max = max;
    }
}
