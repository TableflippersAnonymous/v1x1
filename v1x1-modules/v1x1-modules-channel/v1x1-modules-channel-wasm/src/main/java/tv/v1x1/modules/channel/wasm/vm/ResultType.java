package tv.v1x1.modules.channel.wasm.vm;

import java.util.ArrayList;
import java.util.List;

public class ResultType extends ArrayList<ValType> {
    public ResultType(final List<ValType> list) {
        super();
        addAll(list);
    }
}
