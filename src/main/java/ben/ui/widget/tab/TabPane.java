package ben.ui.widget.tab;

import ben.ui.action.AbstractAction;
import ben.ui.math.PmvMatrix;
import ben.ui.math.Vec2i;
import ben.ui.resource.GlResourceManager;
import ben.ui.widget.AbstractPane;
import ben.ui.widget.IWidget;
import com.jogamp.opengl.GL2;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Tab Pane.
 *
 * <pre>
 * +-------+-------+-------+--------+
 * | Tab 1 | Tab 2 | Tab 3 |        |
 * +-------+-------+-------+--------+
 * | Content                        |
 * |                                |
 * +--------------------------------+
 * </pre>
 */
public final class TabPane extends AbstractPane {

    /**
     * Index of an invalid tab.
     */
    private static final int INVALID_TAB = -1;

    /**
     * The tab bar.
     */
    private final TabBar tabBar = new TabBar();

    /**
     * The tab components.
     */
    private final List<TabComponents> tabComponents = new ArrayList<>();

    /**
     * The currently selected tab.
     */
    private int selectedTab = INVALID_TAB;

    /**
     * Constructor.
     * @param name the name of the pane
     */
    public TabPane(@Nullable String name) {
        super(name, true, true);
        addWidget(tabBar);
    }

    @Override
    protected void initDraw(@Nonnull GL2 gl, @Nonnull GlResourceManager glResourceManager) { }

    @Override
    protected void updateDraw(@Nonnull GL2 gl) { }

    @Override
    protected void doDraw(@Nonnull GL2 gl, @Nonnull PmvMatrix pmvMatrix) { }

    /**
     * Add a tab to the pane.
     * @param title the tab title
     * @param tabContent the tab content
     */
    public void addTab(@Nonnull String title, @Nonnull IWidget tabContent) {
        int tabIndex = this.tabComponents.size();

        TabButton tabButton = new TabButton(null, title);
        tabButton.setAction(new SelectTabAction(tabIndex));
        tabBar.add(tabButton);

        TabComponents tabComponents = new TabComponents(tabButton, tabContent);
        this.tabComponents.add(tabComponents);

        // If there was no tab selected, select this tab.
        if (selectedTab == INVALID_TAB) {
            setSelectedTab(tabIndex);
        }

        updateLayout();
    }

    /**
     * Set the selected tab.
     * @param selectedTab the index of the tab to select
     */
    public void setSelectedTab(int selectedTab) {
        assert selectedTab >= 0 && selectedTab < tabComponents.size();

        if (this.selectedTab != INVALID_TAB) {
            TabButton oldButton = tabComponents.get(this.selectedTab).tabButton;
            oldButton.setSelected(false);

            IWidget oldContent = tabComponents.get(this.selectedTab).tabContent;
            removeWidget(oldContent);
        }

        this.selectedTab = selectedTab;

        TabButton newButton = tabComponents.get(selectedTab).tabButton;
        newButton.setSelected(true);

        IWidget newContent = tabComponents.get(selectedTab).tabContent;
        addWidget(newContent);
    }

    @Override
    protected void updateLayout() {
        int paneWidth = getSize().getX();
        int paneHeight = getSize().getY();

        int tabBarHeight = tabBar.getPreferredSize().getY();
        tabBar.setSize(new Vec2i(paneWidth, tabBarHeight));
        tabBar.setPosition(new Vec2i(0, 0));

        Vec2i contentSize = new Vec2i(paneWidth, paneHeight - tabBarHeight);
        Vec2i contentPos = new Vec2i(0, tabBarHeight);
        for (TabComponents tabComponents : this.tabComponents) {
            IWidget tabContent = tabComponents.tabContent;
            tabContent.setSize(contentSize);
            tabContent.setPosition(contentPos);
        }
    }

    @Nonnull
    @Override
    public Vec2i getPreferredSize() {
        Vec2i tabBarPrefSize = tabBar.getPreferredSize();
        int tabBarHeight = tabBarPrefSize.getY();

        int contentWidth = tabBarPrefSize.getX();
        int contentHeight = 0;

        for (TabComponents tabComponents : this.tabComponents) {
            IWidget tabContent = tabComponents.tabContent;
            Vec2i tabContentPrefSize = tabContent.getPreferredSize();
            if (tabContentPrefSize.getX() > contentWidth) {
                contentWidth = tabContentPrefSize.getX();
            }
            if (tabContentPrefSize.getY() > contentHeight) {
                contentHeight = tabContentPrefSize.getY();
            }
        }

        return new Vec2i(contentWidth, tabBarHeight + contentHeight);
    }

    /**
     * Tab Components.
     */
    private static final class TabComponents {

        /**
         * The tab button.
         */
        @Nonnull
        private final TabButton tabButton;

        /**
         * The tab content.
         */
        @Nonnull
        private final IWidget tabContent;

        /**
         * Constructor.
         * @param tabButton the tab button
         * @param tabContent the tab content
         */
        private TabComponents(@Nonnull TabButton tabButton, @Nonnull IWidget tabContent) {
            this.tabButton = tabButton;
            this.tabContent = tabContent;
        }
    }

    /**
     * Select Tab Action.
     */
    private class SelectTabAction extends AbstractAction {

        /**
         * The index of the tab to select.
         */
        private final int tabIndex;

        /**
         * Constructor.
         * @param tabIndex the index of the tab to select
         */
        public SelectTabAction(int tabIndex) {
            this.tabIndex = tabIndex;
        }

        @Override
        public String toString() {
            return SelectTabAction.class.getSimpleName() + "[tabIndex: " + tabIndex + "]";
        }

        @Override
        protected void doAction() {
            setSelectedTab(tabIndex);
        }
    }
}
