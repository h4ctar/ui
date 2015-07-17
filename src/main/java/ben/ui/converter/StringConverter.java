package ben.ui.converter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * String Converter.
 * <p>
 *     Just passes the string along, no conversion.
 * </p>
 */
public class StringConverter implements IValueConverter<String> {

    /**
     * Only instance of the converter.
     */
    public static final StringConverter STRING_CONVERTER = new StringConverter();

    /**
     * Private Constructor.
     */
    private StringConverter() { }

    @Nullable
    @Override
    public String convertTo(@NotNull String text) {
        return text;
    }

    @NotNull
    @Override
    public String convertFrom(@NotNull String value) {
        return value;
    }
}
