package ben.ui;

import ben.ui.converter.StringConverter;
import ben.ui.math.Vec2i;
import ben.ui.widget.*;
import ben.ui.widget.scroll.ScrollPane;
import ben.ui.widget.tab.TabPane;
import ben.ui.widget.window.Window;
import ben.ui.window.MainWindow;

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

        BorderPane borderPane = new BorderPane(null);

        HorizontalPane menuBar = new HorizontalPane(null, false);
        menuBar.add(new MenuItem(null, "File"));
        menuBar.add(new MenuItem(null, "Edit"));
        menuBar.add(new MenuItem(null, "View"));
        menuBar.add(new MenuItem(null, "Help"));

        HorizontalPane statusBar = new HorizontalPane(null, true);
        statusBar.add(new Button(null, "File"));
        statusBar.add(new Button(null, "Edit"));
        statusBar.add(new Button(null, "View"));
        statusBar.add(new Button(null, "Help"));

        VerticalPane leftBar = new VerticalPane(null, true);
        leftBar.add(new Button(null, "Button"));
        leftBar.add(new Button(null, "Button"));
        leftBar.add(new Button(null, "Button"));
        leftBar.add(new Button(null, "Button"));

        VerticalPane rightBar = new VerticalPane(null, false);
        rightBar.add(new Button(null, "Button"));
        rightBar.add(new Button(null, "Button"));
        rightBar.add(new Button(null, "Button"));
        rightBar.add(new Button(null, "Button"));

        borderPane.setTop(menuBar);
        borderPane.setBottom(statusBar);
        borderPane.setLeft(leftBar);
        borderPane.setRight(rightBar);

        rootPane.add(borderPane);

        DesktopPane desktopPane = new DesktopPane(null);

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

        CenterPane centerPane = new CenterPane(null);
        centerPane.setCenter(new TextField<>(null, StringConverter.STRING_CONVERTER));
        Window window2 = new Window(null, "Text Field", centerPane);
        window2.setPosition(new Vec2i(400, 30));
        desktopPane.add(window2);

        TabPane tabPane = new TabPane(null);
        tabPane.addTab("Tab 1", new Button(null, "Button 1"));
        tabPane.addTab("Tab 2", new Button(null, "Button 2"));
        tabPane.addTab("Scroll Pane", scrollPane);
        Window window3 = new Window(null, "Tab Pane", tabPane);
        window3.setSize(new Vec2i(300, 200));
        window3.setPosition(new Vec2i(80, 300));

        desktopPane.add(window3);

        rootPane.add(desktopPane);

        mainWindow.setRootWidget(rootPane);
    }
}
