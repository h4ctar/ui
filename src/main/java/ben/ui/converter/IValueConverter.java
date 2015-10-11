package ben.ui.converter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Value Converter Interface.
 * @param <V></V> the type of the value
 */
public interface IValueConverter<V> {

    /**
     * Convert a string to a value.
     * @param text the value as a string
     * @return the converted value
     */
    @Nullable
    V convertTo(@NotNull String text);

    /**
     * Convert a value to a string.
     * @param value the value
     * @return the string representation of the value
     */
    @NotNull
    String convertFrom(@NotNull V value);
}
