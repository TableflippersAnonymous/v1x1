package tv.v1x1.modules.channel.wasm.vm.store;

import tv.v1x1.modules.channel.wasm.vm.TableType;

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

    public boolean matches(final TableType tableType) {
        return elements.size() >= tableType.getLimits().getMin().getValU()
                && (!tableType.getLimits().getMax().isPresent()
                    || (max.isPresent() && max.get() <= tableType.getLimits().getMax().get().getValU()));
    }

    @Override
    public String toString() {
        return "TableInstance{" +
                "elements=" + elements +
                ", max=" + max +
                '}';
    }
}
