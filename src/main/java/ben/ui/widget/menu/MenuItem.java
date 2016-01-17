package ben.ui.widget.menu;

import ben.ui.action.AbstractAction;
import ben.ui.action.IAction;
import ben.ui.input.mouse.MouseButton;
import ben.ui.input.mouse.MouseListenerAdapter;
import ben.ui.math.PmvMatrix;
import ben.ui.math.Vec2i;
import ben.ui.renderer.TextRenderer;
import ben.ui.resource.GlResourceManager;
import ben.ui.resource.color.Color;
import ben.ui.widget.AbstractWidget;
import ben.ui.widget.IDesktop;
import com.jogamp.opengl.GL2;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Menu Item.
 */
public final class MenuItem extends AbstractWidget {

    /**
     * The padding around the menu item.
     */
    private static final int PADDING = 5;

    /**
     * The height of the menu item.
     */
    private static final int HEIGHT = TextRenderer.CHARACTER_SIZE + 2 * PADDING;

    /**
     * The text colour.
     */
    @Nonnull
    private static final Color TEXT_COLOR = new Color(0.73f, 0.73f, 0.73f);

    /**
     * The text.
     */
    @Nonnull
    private final String text;

    /**
     * The text renderer.
     */
    @Nullable
    private TextRenderer textRenderer;

    /**
     * True to open sub menus below this menu, otherwise they will be opened to the right side.
     */
    private boolean openBelow;

    /**
     * Constructor.
     *
     * @param name the name of the button
     * @param text the text
     * @param action the action that will be executed when the menu item is clicked
     */
    public MenuItem(@Nullable String name, @Nonnull String text, @Nullable IAction action) {
        super(name);
        this.text = text;
        getMouseHandler().addMouseListener(new MouseListener());
        setSize(getPreferredSize());
        setAction(action);
    }

    /**
     * Constructor.
     *
     * @param name the name of the button
     * @param text the text
     * @param subMenu the sub menu that will be opened when the menu item is clicked
     * @param desktop the desktop that the sub menu will be opened on
     */
    public MenuItem(@Nullable String name, @Nonnull String text, @Nonnull Menu subMenu, @Nonnull IDesktop desktop) {
        super(name);
        this.text = text;
        getMouseHandler().addMouseListener(new MouseListener());
        setSize(getPreferredSize());

        SubMenuAction action = new SubMenuAction(subMenu, desktop);
        setAction(action);
    }

    @Override
    public String toString() {
        return MenuItem.class.getSimpleName() + "[text: '" + text + "']";
    }

    @Override
    protected void initDraw(@Nonnull GL2 gl, @Nonnull GlResourceManager glResourceManager) {
        textRenderer = new TextRenderer(gl, glResourceManager, text, new Vec2i(PADDING, PADDING), TEXT_COLOR);
    }

    @Override
    protected void updateDraw(@Nonnull GL2 gl) {
        assert textRenderer != null : "Update draw should not be called before init draw";
        textRenderer.setText(gl, text);
    }

    @Override
    protected void doDraw(@Nonnull GL2 gl, @Nonnull PmvMatrix pmvMatrix) {
        assert textRenderer != null : "Draw should not be called before init draw";
        textRenderer.draw(gl, pmvMatrix);
    }

    @Nonnull
    @Override
    public Vec2i getPreferredSize() {
        int width = text.length() * TextRenderer.CHARACTER_SIZE + 2 * PADDING;
        return new Vec2i(width, HEIGHT);
    }

    @Override
    protected void preRemove(@Nonnull GL2 gl) {
        if (textRenderer != null) {
            textRenderer.remove(gl);
        }
    }

    /**
     * Set if the open sub menus should open below this menu, otherwise they will be opened to the right side.
     *
     * @param openBelow true to open below
     */
    public void setOpenBelow(boolean openBelow) {
        this.openBelow = openBelow;
    }

    /**
     * Sub Menu Action.
     *
     * Opens a sub menu.
     */
    private final class SubMenuAction extends AbstractAction {

        /**
         * The sub menu.
         */
        @Nonnull
        private final Menu subMenu;

        /**
         * The desktop that the sub menu will be opened on.
         */
        @Nonnull
        private final IDesktop desktop;

        /**
         * Constructor.
         *
         * @param subMenu the sub menu
         * @param desktop the desktop that the sub menu will be opened on
         */
        private SubMenuAction(@Nonnull Menu subMenu, @Nonnull IDesktop desktop) {
            this.subMenu = subMenu;
            this.desktop = desktop;
        }

        @Override
        public String toString() {
            return SubMenuAction.class.getSimpleName() + "[menuItemName: \"" + getName() + "\"" + ", subMenuName: \"" + subMenu.getName() + "\"]";
        }

        @Override
        protected void doAction(@Nonnull Vec2i widgetPos) {
            Vec2i subMenuPos;
            if (openBelow) {
                subMenuPos = widgetPos.add(new Vec2i(0, getSize().getY()));
            }
            else {
                subMenuPos = widgetPos.add(new Vec2i(getSize().getX(), 0));
            }
            subMenu.setPosition(subMenuPos);
            desktop.pushDialog(subMenu);
        }
    }

    /**
     * The Mouse Listener.
     *
     * Executes the action when the menu item is clicked.
     */
    private final class MouseListener extends MouseListenerAdapter {

        @Override
        public void mouseClicked(@Nonnull MouseButton button, @Nonnull Vec2i widgetPos) {
            IAction action = getAction();
            if (action != null) {
                action.execute(widgetPos);
            }
        }
    }
}
