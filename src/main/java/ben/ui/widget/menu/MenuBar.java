package ben.ui.widget.menu;

import ben.ui.action.IAction;
import ben.ui.math.PmvMatrix;
import ben.ui.math.Vec2i;
import ben.ui.resource.GlResourceManager;
import ben.ui.widget.AbstractPane;
import ben.ui.widget.IDesktop;
import ben.ui.widget.IWidget;
import com.jogamp.opengl.GL2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Menu Bar.
 */
public final class MenuBar extends AbstractPane {

    /**
     * The spacing between the menu items.
     */
    private static final int SPACING = 5;

    /**
     * Constructor.
     * @param name the name of the pane
     */
    public MenuBar(@Nullable String name) {
        super(name, true, true);
    }

    @Override
    public String toString() {
        return MenuBar.class.getSimpleName() + "[name: \"" + getName() + "\"]";
    }

    @Override
    protected void initDraw(@Nonnull GL2 gl, @Nonnull GlResourceManager glResourceManager) { }

    @Override
    protected void updateDraw(@Nonnull GL2 gl) { }

    @Override
    protected void doDraw(@Nonnull GL2 gl, @Nonnull PmvMatrix pmvMatrix) { }

    @Override
    protected void updateLayout() {
        int x = 0;
        int y = 0;
        int height = getSize().getY();

        for (IWidget widget : getWidgets()) {
            Vec2i preferredSize = widget.getPreferredSize();
            Vec2i actualSize = new Vec2i(preferredSize.getX(), height);
            widget.setSize(actualSize);
            widget.setPosition(new Vec2i(x, y));
            x += widget.getSize().getX() + SPACING;
        }
    }

    @Nonnull
    @Override
    public Vec2i getPreferredSize() {
        int height = 0;
        int width = 0;

        if (getWidgets().isEmpty()) {
            width = 0;
            height = 0;
        }
        else {
            for (IWidget widget : getWidgets()) {
                Vec2i preferredSize = widget.getPreferredSize();
                widget.setSize(preferredSize);
                width += preferredSize.getX() + SPACING;

                if (preferredSize.getY() > height) {
                    height = preferredSize.getY();
                }
            }

            // Remove the last bit of spacing.
            width -= SPACING;
        }

        return new Vec2i(width, height);
    }

    /**
     * Add a menu item with an action.
     * @param name the name of the menu item
     * @param text the text of the menu item
     * @param action the action that will be executed when the menu item is clicked
     */
    public void addMenuItem(@Nullable String name, @Nonnull String text, @Nullable IAction action) {
        MenuItem menuItem = new MenuItem(name, text, action);
        menuItem.setOpenBelow(true);
        addWidget(menuItem);
        updateLayout();
    }

    /**
     * Add a menu item for a sub menu.
     * @param name the name of the menu item
     * @param text the text of the menu item
     * @param menu the menu to add
     * @param desktop the desktop that the new menu will be opened onto
     */
    public void addMenuItem(@Nullable String name, @Nonnull String text, @Nonnull Menu menu, @Nonnull IDesktop desktop) {
        MenuItem menuItem = new MenuItem(name, text, menu, desktop);
        menuItem.setOpenBelow(true);
        addWidget(menuItem);
        updateLayout();
    }
}
