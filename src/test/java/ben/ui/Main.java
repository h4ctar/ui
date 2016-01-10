package ben.ui;

import ben.ui.converter.IntegerConverter;
import ben.ui.converter.StringConverter;
import ben.ui.math.Vec2i;
import ben.ui.widget.*;
import ben.ui.widget.scroll.ScrollPane;
import ben.ui.widget.tab.TabPane;
import ben.ui.widget.window.Window;
import ben.ui.window.MainWindow;

import javax.swing.border.Border;

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

        BorderPane borderPane = new BorderPane(null);

        HorizontalPane menuBar = new HorizontalPane(null, false);
        menuBar.add(new MenuItem(null, "File"));
        menuBar.add(new MenuItem(null, "Edit"));
        menuBar.add(new MenuItem(null, "View"));
        menuBar.add(new MenuItem(null, "Help"));

        VerticalPane leftBar = new VerticalPane(null, true);
        leftBar.add(new Button(null, "Button"));
        leftBar.add(new Button(null, "Button"));
        leftBar.add(new Button(null, "Button"));
        leftBar.add(new Button(null, "Button"));

        borderPane.setTop(menuBar);
        borderPane.setLeft(leftBar);

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

        CenterPane centerPane = new CenterPane(null, new TextField<>(null, StringConverter.STRING_CONVERTER));
        Window window2 = new Window(null, "Text Field", centerPane);
        desktopPane.addWindow(window2);

        TabPane tabPane = new TabPane(null);
        tabPane.addTab("Tab 1", new Button(null, "Button 1"));
        tabPane.addTab("Tab 2", getLittleBorderPane());
        tabPane.addTab("Scroll Pane", scrollPane);
        Window window3 = new Window(null, "Tab Pane", tabPane);
        window3.setSize(new Vec2i(300, 200));

        desktopPane.addWindow(window3);

        borderPane.setCenter(desktopPane);

        mainWindow.setRootWidget(borderPane);
    }

    public static IWidget getLittleBorderPane() {
        BorderPane borderPane = new BorderPane(null);
        borderPane.setLeft(new Button(null, "Left"));
        borderPane.setRight(new Button(null, "Right"));
        borderPane.setTop(new Button(null, "Top"));
        borderPane.setBottom(new Button(null, "Bottom"));
        borderPane.setCenter(new TextField<>(null, StringConverter.STRING_CONVERTER));
        return borderPane;
    }
}
