package ben.ui.widget;

import ben.ui.input.key.BasicKeyHandler;
import ben.ui.input.key.IKeyHandler;
import ben.ui.input.mouse.BasicMouseHandler;
import ben.ui.input.mouse.IMouseHandler;
import ben.ui.math.*;
import ben.ui.resource.GlResourceManager;
import ben.ui.graphic.IGraphic;
import ben.ui.resource.color.Color;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import com.jogamp.opengl.GL2;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Canvas.
 */
@ThreadSafe
public abstract class Canvas implements IWidget {

    /**
     * The background colour.
     * <p>
     *     Used to clear the canvas before it is drawn.
     * </p>
     */
    private static final Color BACKGROUND_COLOR = new Color(0.169f, 0.169f, 0.169f);

    /**
     * The graphics.
     */
    @NotNull
    private final Set<IGraphic> graphics = new CopyOnWriteArraySet<>();

    /**
     * The child graphics that have been removed from the pane and need to be cleaned up.
     */
    @GuardedBy("removedGraphics")
    @NotNull
    private final Set<IGraphic> removedGraphics = new HashSet<>();

    /**
     * The mouse handler.
     */
    @NotNull
    private final BasicMouseHandler mouseHandler = new BasicMouseHandler();

    /**
     * The key handler.
     */
    @NotNull
    private final BasicKeyHandler keyHandler = new BasicKeyHandler();

    /**
     * The name of the canvas.
     */
    @Nullable
    private final String name;

    /**
     * The position of the canvas on the screen.
     */
    @NotNull
    private Vec2i position = new Vec2i(0, 0);

    /**
     * The size of the canvas on the screen.
     */
    @NotNull
    private Vec2i size = new Vec2i(0, 0);

    /**
     * Constructor.
     * @param name the name of the canvas.
     */
    public Canvas(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    @Override
    public String getName() {
        return name;
    }

    @Override
    public final void draw(@NotNull GL2 gl, @NotNull PmvMatrix pmvMatrix, @NotNull GlResourceManager glResourceManager) {
        // Setup the canvas viewport
        Vec4f canvasPosition = Matrix.mul(pmvMatrix.getMvMatrix(), new Vec4f(position.getX(), position.getY(), 0.0f, 1.0f));
        gl.glViewport((int) canvasPosition.getX(), (int) canvasPosition.getY(), getSize().getX(), getSize().getY());

        gl.glClearColor(BACKGROUND_COLOR.getRed(), BACKGROUND_COLOR.getGreen(), BACKGROUND_COLOR.getBlue(), BACKGROUND_COLOR.getAlpha());
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

        // Remove the old graphics.
        synchronized (removedGraphics) {
            for (IGraphic graphic : removedGraphics) {
                graphic.remove(gl);
            }
            removedGraphics.clear();
        }

        // Draw the graphics.
        for (IGraphic graphic : graphics) {
            graphic.draw(gl, getPmvMatrix(), glResourceManager);
        }

        // Reset the viewport.
        Rect viewport = pmvMatrix.getViewport();
        gl.glViewport(viewport.getX(), viewport.getY(), viewport.getWidth(), viewport.getHeight());
    }

    /**
     * Get the canvas PMV matrix.
     * @return the PMV matrix
     */
    @NotNull
    protected abstract PmvMatrix getPmvMatrix();

    @Override
    public final void setPosition(@NotNull Vec2i position) {
        this.position = position;
    }

    @NotNull
    @Override
    public final Vec2i getPosition() {
        return position;
    }

    @Override
    public final void setSize(@NotNull Vec2i size) {
//        assert size.getX() >= 0 : "Canvas size must not be negative";
//        assert size.getY() >= 0 : "Canvas size must not be negative";
        this.size = size;
    }

    @NotNull
    @Override
    public final Vec2i getSize() {
        return size;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public final boolean contains(@NotNull Vec2i pos) {
        // TODO: Move this into Rectangle
        return (pos.getX() >= position.getX()) && (pos.getY() >= position.getY()) && (pos.getX() <= position.getX() + size.getX()) && (pos.getY() <= position.getY() + size.getY());
    }

    @Override
    public final void setFocused(boolean focused) { }

    @NotNull
    @Override
    public final IMouseHandler getMouseHandler() {
        return mouseHandler;
    }

    @NotNull
    @Override
    public final IKeyHandler getKeyHandler() {
        return keyHandler;
    }

    @Override
    public final void updateSize() { }

    /**
     * Add a graphic.
     * @param graphic the graphic to add
     */
    public final void addGraphic(@NotNull IGraphic graphic) {
        assert !graphics.contains(graphic) : "The graphic is already added";
        graphics.add(graphic);
        synchronized (removedGraphics) {
            removedGraphics.remove(graphic);
        }
    }

    /**
     * Remove a graphic.
     * @param graphic the graphic to remove
     */
    public final void removeGraphic(@NotNull IGraphic graphic) {
        assert graphics.contains(graphic) : "Can't remove a graphic that has not been added";
        graphics.remove(graphic);
        synchronized (removedGraphics) {
            assert !removedGraphics.contains(graphic) : "The graphic should not already be in the removed objects";
            removedGraphics.add(graphic);
        }
    }

    @Override
    public final void remove(@NotNull GL2 gl) {
        for (IGraphic graphic : graphics) {
            graphic.remove(gl);
        }
    }
}
