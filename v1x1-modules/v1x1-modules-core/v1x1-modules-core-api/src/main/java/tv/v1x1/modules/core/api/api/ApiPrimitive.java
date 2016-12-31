package tv.v1x1.modules.core.api.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 12/31/2016.
 */
public class ApiPrimitive<T> {
    @JsonProperty
    private T value;

    public ApiPrimitive() {
    }

    public ApiPrimitive(final T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(final T value) {
        this.value = value;
    }
}
