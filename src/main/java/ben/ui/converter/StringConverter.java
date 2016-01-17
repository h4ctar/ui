package ben.ui.converter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * String Converter.
 * <p>
 *     Just passes the string along, no conversion.
 * </p>
 */
public final class StringConverter implements IValueConverter<String> {

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
    public String convertTo(@Nonnull String text) {
        return text.equals("") ? null : text;
    }

    @Nonnull
    @Override
    public String convertFrom(@Nonnull String value) {
        return value;
    }
}
