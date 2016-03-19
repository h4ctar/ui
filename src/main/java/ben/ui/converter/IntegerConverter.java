package ben.ui.converter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * # Integer Converter
 *
 * A value converter that converts ints to strings.
 * This converter has a private constructor and should be used via its static field instance of itself.
 */
public final class IntegerConverter implements IValueConverter<Integer> {

    /**
     * Only instance of the converter.
     */
    public static final IntegerConverter INT_CONVERTER = new IntegerConverter();

    /**
     * Private Constructor.
     */
    private IntegerConverter() { }

    @Nullable
    @Override
    public Integer convertTo(@Nonnull String text) {
        Integer value;
        try {
            value = Integer.parseInt(text);
        }
        catch (NumberFormatException e) {
            value = null;
        }
        return value;
    }

    @Nonnull
    @Override
    public String convertFrom(@Nonnull Integer value) {
        return value.toString();
    }
}
