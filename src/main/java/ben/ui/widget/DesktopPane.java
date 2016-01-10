package ben.ui.widget;

import ben.ui.input.IFocusManagerListener;
import ben.ui.math.PmvMatrix;
import ben.ui.math.Vec2i;
import ben.ui.resource.GlResourceManager;
import ben.ui.widget.window.IWindow;
import com.jogamp.opengl.GL2;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

/**
 * Desktop Pane.
 *
 * The desktop pane does no layout of widgets.
 */
public final class DesktopPane extends AbstractPane {

    /**
     * The focus listener.
     */
    @Nonnull
    private final IFocusManagerListener focusListener = new FocusListener();

    /**
     * The windows.
     *
     * Maintained so that their desktop rectangles can be updated when the layout changes.
     */
    @Nonnull
    private final Set<IWindow> windows = new HashSet<>();

    /**
     * Constructor.
     * @param name the name of the pane
     */
    public DesktopPane(@Nullable String name) {
        super(name, false, false);
        getFocusManager().addFocusListener(focusListener);
    }

    @Override
    public String toString() {
        return DesktopPane.class.getSimpleName() + "[name: \"" + getName() + "\"]";
    }

    @Override
    protected void initDraw(@Nonnull GL2 gl, @Nonnull GlResourceManager glResourceManager) { }

    @Override
    protected void updateDraw(@Nonnull GL2 gl) { }

    @Override
    protected void doDraw(@Nonnull GL2 gl, @Nonnull PmvMatrix pmvMatrix) { }

    @Override
    protected void updateLayout() {
        // Note, this code is exploiting the fact that this method will be called when the size of the pane changes.
        for (IWindow window : windows) {
            window.setDesktopRect(getRect());
        }
    }

    @Nonnull
    @Override
    public Vec2i getPreferredSize() {
        return getSize();
    }

    /**
     * Add a new window to the desktop pane.
     * @param window the new widget to add
     */
    public void addWindow(@Nonnull IWindow window) {
        assert !windows.contains(window);

        windows.add(window);
        addWidget(window);
        window.setDesktopRect(getRect());
    }

    /**
     * Remove a window from the desktop pane.
     * @param window the widget to remove
     */
    public void removeWindow(@Nonnull IWindow window) {
        assert windows.contains(window);

        removeWidget(window);
        windows.remove(window);
    }

//    public void pushDialog(@Nonnull IWidget dialog) {
//        addWidget(dialog);
//    }
//
//    public void popDialog(@Nonnull IWidget dialog) {
//        removeWidget(dialog);
//    }

    /**
     * Focus Listener.
     *
     * Brings the in focus widget to the top.
     */
    private class FocusListener implements IFocusManagerListener {

        @Override
        public void focusedWidget(@Nullable IWidget focusedWidget) {
            if (focusedWidget != null) {
                // Remove and re-add the newly focused widget so that it comes to top.
                removeWidget(focusedWidget);
                addWidget(focusedWidget);
            }
        }
    }
}
