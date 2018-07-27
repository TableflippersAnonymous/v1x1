package tv.v1x1.modules.channel.wasm.vm.validation;

import java.util.List;

public class ValidationFrame {
    private List<ValType> labelTypes;
    private List<ValType> endTypes;
    private int height;
    private boolean unreachable;

    public ValidationFrame(final List<ValType> labelTypes, final List<ValType> endTypes, final int height, final boolean unreachable) {
        this.labelTypes = labelTypes;
        this.endTypes = endTypes;
        this.height = height;
        this.unreachable = unreachable;
    }

    public List<ValType> getLabelTypes() {
        return labelTypes;
    }

    public List<ValType> getEndTypes() {
        return endTypes;
    }

    public int getHeight() {
        return height;
    }

    public boolean isUnreachable() {
        return unreachable;
    }

    public void setUnreachable() {
        unreachable = true;
    }
}
