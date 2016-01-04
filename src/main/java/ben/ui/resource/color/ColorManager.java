package ben.ui.resource.color;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import javax.annotation.Nonnull;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Color Manager.
 */
public class ColorManager {

    /**
     * The Logger.
     */
    private static final Logger LOGGER = LogManager.getLogger(ColorManager.class.getSimpleName());

    /**
     * The textures.
     */
    private final Map<Class<?>, Map<Enum<?>, Color>> colors = new HashMap<>();

    /**
     * Load colors from a local file.
     * @param type the enum to load the colors for
     * @param colorsResourceName the path to the colors xml
     */
    public final void loadColors(@Nonnull Class<? extends Enum<?>> type, @Nonnull String colorsResourceName) {
        LOGGER.info("Loading colors: " + type.getSimpleName() + " -> " + colorsResourceName);
        assert !colors.containsKey(type);
        try (InputStream stream = getClass().getResourceAsStream(colorsResourceName)) {
            Map<Enum<?>, Color> newColors = new HashMap<>();
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
            NodeList colorsElement = doc.getElementsByTagName("color");
            Method valueOfMethod = type.getMethod("valueOf", String.class);
            for (int i = 0; i < colorsElement.getLength(); i++) {
                Node colorNode = colorsElement.item(i);
                NamedNodeMap attributes = colorNode.getAttributes();
                String name = attributes.getNamedItem("name").getTextContent();
                float red = Float.parseFloat(attributes.getNamedItem("red").getTextContent());
                float green = Float.parseFloat(attributes.getNamedItem("green").getTextContent());
                float blue = Float.parseFloat(attributes.getNamedItem("blue").getTextContent());
                Color color = new Color(red, green, blue);
                Enum<?> key = (Enum<?>) valueOfMethod.invoke(null, name);
                assert !newColors.containsKey(key) : "Color already loaded";
                newColors.put(key, color);
            }
            colors.put(type, newColors);
        }
        catch (@Nonnull ParserConfigurationException | IOException | SAXException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Couldn't load colors", e);
        }
    }

    /**
     * Get a color.
     * @param key the key of the color
     * @return the color
     */
    @Nonnull
    public final Color getColor(@Nonnull Enum<?> key) {
        assert colors.containsKey(key.getClass()) : "Color type not loaded";
        assert colors.get(key.getClass()).containsKey(key) : "Color not loaded";
        return colors.get(key.getClass()).get(key);
    }
}
