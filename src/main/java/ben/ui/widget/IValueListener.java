package ben.ui.widget;

import javax.annotation.Nullable;

/**
 * Value Listener Interface.
 * @param <T> the type of the value
 */
public interface IValueListener<T> {

    /**
     * The value has changed.
     * @param value the new value
     */
    void valueChanged(@Nullable T value);
}
