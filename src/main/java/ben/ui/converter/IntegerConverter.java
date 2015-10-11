package ben.ui.converter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Integer Converter.
 */
public class IntegerConverter implements IValueConverter<Integer> {

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
    public Integer convertTo(@NotNull String text) {
        Integer value;
        try {
            value = Integer.parseInt(text);
        }
        catch (NumberFormatException e) {
            value = null;
        }
        return value;
    }

    @NotNull
    @Override
    public String convertFrom(@NotNull Integer value) {
        return value.toString();
    }
}
