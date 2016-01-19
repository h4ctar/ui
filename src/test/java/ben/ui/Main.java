package ben.ui;

import ben.ui.action.AbstractAction;
import ben.ui.action.IAction;
import ben.ui.converter.FloatConverter;
import ben.ui.converter.IntegerConverter;
import ben.ui.converter.StringConverter;
import ben.ui.math.Vec2i;
import ben.ui.widget.*;
import ben.ui.widget.menu.Menu;
import ben.ui.widget.menu.MenuBar;
import ben.ui.widget.menu.MenuItem;
import ben.ui.widget.scroll.ScrollPane;
import ben.ui.widget.tab.TabPane;
import ben.ui.widget.window.IWindow;
import ben.ui.widget.window.Window;
import ben.ui.window.MainWindow;

import javax.annotation.Nonnull;

/**
 * Example Main.
 */
public class Main {

    /**
     * Main entry.
     * @param args the application args
     */
    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow(800, 600);

        StackPane rootPane = new StackPane(null);
        mainWindow.setRootWidget(rootPane);

        BorderPane borderPane = new BorderPane(null);
        rootPane.add(borderPane);

        Desktop desktop = new Desktop(null);
        rootPane.add(desktop);

        borderPane.setTop(createMenuBar(mainWindow, desktop));
        borderPane.setLeft(createLeftBar());
        borderPane.setCenter(new TextField<>(null, StringConverter.STRING_CONVERTER));

        desktop.addWindow(createWindow1());
        desktop.addWindow(createWindow2());
        desktop.addWindow(createWindow3());
    }

    private static IWidget createMenuBar(MainWindow mainWindow, IDesktop desktop) {
        IAction quitAction = new AbstractAction() {

            @Override
            protected void doAction(@Nonnull Vec2i widgetPos) {
                mainWindow.stop();
            }
        };

        Menu newMenu = new Menu("New menu");
        newMenu.addMenuItem(null, "File", null);
        newMenu.addMenuItem(null, "Project", null);

        Menu fileMenu = new Menu("File menu");
        fileMenu.addMenuItem(null, "New", newMenu, desktop);
        fileMenu.addMenuItem(null, "Open", null);
        fileMenu.addMenuItem(null, "Save", null);
        fileMenu.addMenuItem(null, "Quit", quitAction);

        MenuBar menuBar = new MenuBar(null);
        menuBar.addMenuItem(null, "File", fileMenu, desktop);
        menuBar.addMenuItem(null, "Edit", null);
        menuBar.addMenuItem(null, "View", null);
        menuBar.addMenuItem(null, "Help", null);

        return menuBar;
    }

    private static IWidget createLeftBar() {
        VerticalPane leftBar = new VerticalPane(null, true);
        leftBar.add(new Button(null, "Button"));
        leftBar.add(new Button(null, "Button"));
        leftBar.add(new Button(null, "Button"));
        leftBar.add(new Button(null, "Button"));
        return leftBar;
    }

    private static IWindow createWindow1() {
        VerticalPane verticalPane = new VerticalPane(null, true);

        HorizontalPane row1 = new HorizontalPane(null, false);
        row1.add(new Label(null, " String:"));
        row1.add(new TextField<>(null, StringConverter.STRING_CONVERTER));
        verticalPane.add(row1);

        HorizontalPane row2 = new HorizontalPane(null, false);
        row2.add(new Label(null, "Integer:"));
        row2.add(new TextField<>(null, IntegerConverter.INT_CONVERTER));
        verticalPane.add(row2);

        HorizontalPane row3 = new HorizontalPane(null, false);
        row3.add(new Label(null, "  Float:"));
        row3.add(new TextField<>(null, FloatConverter.FLOAT_CONVERTER));
        verticalPane.add(row3);

        Window window = new Window(null, "Text Field", verticalPane);
        window.setPosition(new Vec2i(100, 100));

        return window;
    }

    private static IWindow createWindow2() {
        VerticalPane list = new VerticalPane(null, true);
        list.add(new Button(null, "Haha 1"));
        list.add(new Button(null, "Haha 2"));
        list.add(new Button(null, "Haha 3"));
        list.add(new Button(null, "Haha 4"));
        list.add(new Button(null, "Haha 5"));
        list.add(new Button(null, "Haha 6"));
        list.add(new Button(null, "Haha 7"));
        list.add(new Button(null, "Haha 8"));
        list.add(new Button(null, "Haha 9"));
        list.add(new Button(null, "Haha 10"));
        list.add(new Button(null, "Haha 11"));
        list.add(new Button(null, "Haha 12"));
        list.add(new Button(null, "Haha 13"));
        ScrollPane scrollPane = new ScrollPane(null, list);

        TabPane tabPane = new TabPane(null);
        tabPane.addTab("Tab 1", new CenterPane(null, new Button(null, "Button")));
        tabPane.addTab("Tab 2", createLittleBorderPane());
        tabPane.addTab("Scroll Pane", scrollPane);

        Window window = new Window(null, "Tab Pane", tabPane);
        window.setSize(new Vec2i(300, 200));
        window.setPosition(new Vec2i(400, 100));
        return window;
    }

    public static IWidget createLittleBorderPane() {
        BorderPane borderPane = new BorderPane(null);
        borderPane.setLeft(new Button(null, "Left"));
        borderPane.setRight(new Button(null, "Right"));
        borderPane.setTop(new Button(null, "Top"));
        borderPane.setBottom(new Button(null, "Bottom"));
        borderPane.setCenter(new TextField<>(null, StringConverter.STRING_CONVERTER));
        return borderPane;
    }

    private static IWindow createWindow3() {
        Button button = new Button(null, "Click Me");
        Window window = new Window(null, "Button", button);
        window.setPosition(new Vec2i(100, 200));

        IAction buttonAction = new AbstractAction() {

            /**
             * Count.
             */
            private int count = 0;

            @Override
            protected void doAction(@Nonnull Vec2i widgetPos) {
                button.setText("Click Me: " + count++);
                window.pack();
            }
        };

        button.setAction(buttonAction);


        return window;
    }
}
