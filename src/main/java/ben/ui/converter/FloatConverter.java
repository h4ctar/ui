package ben.ui.converter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * # Float Converter
 *
 * A value converter that converts floats to strings.
 * This converter has a private constructor and should be used via its static field instance of itself.
 */
public final class FloatConverter implements IValueConverter<Float> {

    /**
     * Only instance of the converter.
     */
    public static final FloatConverter FLOAT_CONVERTER = new FloatConverter();

    /**
     * Private Constructor.
     */
    private FloatConverter() { }

    @Nullable
    @Override
    public Float convertTo(@Nonnull String text) {
        Float value;
        try {
            value = Float.parseFloat(text);
        }
        catch (NumberFormatException e) {
            value = null;
        }
        return value;
    }

    @Nonnull
    @Override
    public String convertFrom(@Nonnull Float value) {
        return value.toString();
    }
}
