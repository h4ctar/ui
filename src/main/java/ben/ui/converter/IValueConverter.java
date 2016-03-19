package ben.ui.converter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * # Value Converter Interface
 *
 * @param <V> the type of the value
 */
public interface IValueConverter<V> {

    /**
     * Convert a string to a value.
     *
     * @param text the value as a string
     * @return the converted value
     */
    @Nullable
    V convertTo(@Nonnull String text);

    /**
     * Convert a value to a string.
     *
     * @param value the value
     * @return the string representation of the value
     */
    @Nonnull
    String convertFrom(@Nonnull V value);
}
