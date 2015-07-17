package ben.ui.converter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Float Converter.
 */
public class FloatConverter implements IValueConverter<Float> {

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
    public Float convertTo(@NotNull String text) {
        Float value = null;
        try {
            value = Float.parseFloat(text);
        }
        catch (NumberFormatException e) {
            // Ignore.
        }
        return value;
    }

    @NotNull
    @Override
    public String convertFrom(@NotNull Float value) {
        return value.toString();
    }
}
