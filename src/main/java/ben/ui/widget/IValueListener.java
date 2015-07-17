package ben.ui.widget;

import org.jetbrains.annotations.Nullable;

/**
 * Value Listener Interface.
 */
public interface IValueListener<T> {

    /**
     * The value has changed.
     * @param value the new value
     */
    void valueChanged(@Nullable T value);
}
