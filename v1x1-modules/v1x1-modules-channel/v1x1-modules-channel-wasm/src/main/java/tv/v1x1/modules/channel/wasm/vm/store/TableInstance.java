package tv.v1x1.modules.channel.wasm.vm.store;

import java.util.List;
import java.util.Optional;

public class TableInstance {
    private final List<Optional<Integer>> elements;
    private final Optional<Integer> max;

    public TableInstance(final List<Optional<Integer>> elements, final Optional<Integer> max) {
        this.elements = elements;
        this.max = max;
    }

    public List<Optional<Integer>> getElements() {
        return elements;
    }

    public Optional<Integer> getMax() {
        return max;
    }
}
