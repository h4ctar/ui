package ben.ui.resource.color;


import net.jcip.annotations.Immutable;

/**
 * The standard colour implementation.
 */
@Immutable
public final class Color {

    /**
     * Red Constant.
     */
    public static final Color RED = new Color(1, 0, 0, 1);

    /**
     * Green Constant.
     */
    public static final Color GREEN = new Color(0, 1, 0, 1);

    /**
     * Blue Constant.
     */
    public static final Color BLUE = new Color(0, 0, 1, 1);

    /**
     * Black Constant.
     */
    public static final Color BLACK = new Color(0, 0, 0, 1);

    /**
     * White Constant.
     */
    public static final Color WHITE = new Color(1, 1, 1, 1);

    /**
     * The red component.
     */
    private final float red;

    /**
     * The green component.
     */
    private final float green;

    /**
     * The blue component.
     */
    private final float blue;

    /**
     * The alpha component.
     */
    private final float alpha;

    /**
     * Constructor.
     * @param red the red component
     * @param green the green component
     * @param blue the blue component
     * @param alpha the alpha component
     */
    public Color(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    /**
     * Constructor.
     *
     * Sets the alpha to the default value
     * @param red the red component
     * @param green the green component
     * @param blue the blue component
     */
    public Color(float red, float green, float blue) {
        this(red, green, blue, 1);
    }

    /**
     * Get the red component of the color.
     * @return the red component
     */
    public float getRed() {
        return red;
    }

    /**
     * Get the green component of the color.
     * @return the green component
     */
    public float getGreen() {
        return green;
    }

    /**
     * Get the blue component of the color.
     * @return the blue component
     */
    public float getBlue() {
        return blue;
    }

    /**
     * Get the alpha component of the color.
     * @return the alpha component
     */
    public float getAlpha() {
        return alpha;
    }
}
