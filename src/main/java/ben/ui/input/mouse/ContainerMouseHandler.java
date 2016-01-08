package ben.ui.input.mouse;

import ben.ui.input.IFocusManager;
import ben.ui.input.IFocusManagerListener;
import ben.ui.math.Vec2i;
import ben.ui.widget.IWidget;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The container mouse handler.
 * <p>
 *     Along with notifying mouse listeners, this handler will forward the events on to child widgets.
 * </p>
 */
public final class ContainerMouseHandler implements IMouseHandler, IFocusManager {

    /**
     * The widgets that the handler will forward events to.
     */
    private final List<IWidget> widgets = new ArrayList<>();

    /**
     * The focus listeners.
     */
    private final Set<IFocusManagerListener> focusListeners = new HashSet<>();

    /**
     * Listeners of mouse events.
     */
    private final Set<IMouseListener> mouseListeners = new HashSet<>();

    /**
     * The widget that the mouse is currently over.
     */
    @Nullable
    private IWidget mouseOverWidget;

    /**
     * The widget that was pressed.
     */
    @Nullable
    private IWidget mousePressWidget;

    /**
     * The widget that has focus.
     */
    @Nullable
    private IWidget focusedWidget;

    /**
     * Should the handler always consume events.
     * <p>
     *     If false, events will only be consumed if sub widgets consume them.
     * </p>
     */
    private boolean alwaysConsume = true;

    /**
     * Add a widget.
     * @param widget the widget to add
     */
    public void addWidget(@Nonnull IWidget widget) {
        assert !widgets.contains(widget);
        widgets.add(0, widget);
    }

    /**
     * Remove a widget.
     * @param widget the widget to remove
     */
    public void removeWidget(@Nonnull IWidget widget) {
        assert widgets.contains(widget);
        widgets.remove(widget);
    }

    /**
     * Add a new focus listener.
     * <p>
     *     Focus listeners get notified when a widget gets focus.
     * </p>
     * @param focusListener the focus listener to add
     */
    @Override
    public void addFocusListener(@Nonnull IFocusManagerListener focusListener) {
        assert !focusListeners.contains(focusListener);
        focusListeners.add(focusListener);
    }

    @Override
    public void removeFocusListener(@Nonnull IFocusManagerListener focusListener) {
        assert focusListeners.contains(focusListener);
        focusListeners.remove(focusListener);
    }

    /**
     * Clear the focus.
     * <p>
     *     This method should be called by the owner of the mouse handler if the focus has been lost by some external
     *     means.
     * </p>
     */
    public void clearFocus() {
        setFocusedWidget(null);
    }

    /**
     * Set if the handler should always consume events.
     * <p>
     *     If false, events will only be consumed if sub widgets consume them.
     * </p>
     * @param alwaysConsume true to always consume
     */
    public void setAlwaysConsume(boolean alwaysConsume) {
        this.alwaysConsume = alwaysConsume;
    }

    @Override
    public boolean mouseClicked(@Nonnull MouseButton button, @Nonnull Vec2i pos) {
        boolean consumed = false;
        for (IWidget widget : widgets) {
            if (widget.contains(pos)) {
                consumed = widget.getMouseHandler().mouseClicked(button, pos.sub(widget.getPosition()));
                setFocusedWidget(widget);
                if (consumed) {
                    break;
                }
            }
        }
        if (!consumed) {
            setFocusedWidget(null);
        }
        for (IMouseListener mouseListener : mouseListeners) {
            mouseListener.mouseClicked(button);
        }
        return alwaysConsume | consumed;
    }

    @Override
    public boolean mouseEntered() {
        for (IMouseListener mouseListener : mouseListeners) {
            mouseListener.mouseEntered();
        }
        // Always consume mouse entered.
        return true;
    }

    @Override
    public boolean mouseExited() {
        if (mouseOverWidget != null) {
            mouseOverWidget.getMouseHandler().mouseExited();
            mouseOverWidget = null;
        }
        for (IMouseListener mouseListener : mouseListeners) {
            mouseListener.mouseExited();
        }
        // Always consume mouse exited.
        return true;
    }

    @Override
    public boolean mousePressed(@Nonnull MouseButton button, @Nonnull Vec2i pos) {
        boolean consumed = false;
        for (IWidget widget : widgets) {
            if (widget.contains(pos)) {
                consumed = widget.getMouseHandler().mousePressed(button, pos.sub(widget.getPosition()));
                mousePressWidget = widget;
                if (consumed) {
                    break;
                }
            }
        }
        for (IMouseListener mouseListener : mouseListeners) {
            mouseListener.mousePressed(button, pos);
        }
        return alwaysConsume | consumed;
    }

    @Override
    public boolean mouseReleased(@Nonnull MouseButton button, @Nonnull Vec2i pos) {
        boolean consumed = false;
        if (mousePressWidget != null) {
            consumed = mousePressWidget.getMouseHandler().mouseReleased(button, pos.sub(mousePressWidget.getPosition()));
            mousePressWidget = null;
        }
        for (IMouseListener mouseListener : mouseListeners) {
            mouseListener.mouseReleased(button, pos);
        }
        return alwaysConsume | consumed;
    }

    @Override
    public boolean mouseMoved(@Nonnull Vec2i pos) {
        boolean consumed = false;
        for (IWidget widget : widgets) {
            if (widget.contains(pos)) {
                consumed = widget.getMouseHandler().mouseMoved(pos.sub(widget.getPosition()));
                if (widget != mouseOverWidget) {
                    if (mouseOverWidget != null) {
                        // This covers the scenario where the mouse moves from one widget to another.
                        mouseOverWidget.getMouseHandler().mouseExited();
                    }
                    widget.getMouseHandler().mouseEntered();
                    mouseOverWidget = widget;
                }
                if (consumed) {
                    break;
                }
            }
            else {
                if (widget == mouseOverWidget) {
                    widget.getMouseHandler().mouseExited();
                    mouseOverWidget = null;
                }
            }
        }
        for (IMouseListener mouseListener : mouseListeners) {
            mouseListener.mouseMoved(pos);
        }
        return alwaysConsume | consumed;
    }

    @Override
    public boolean mouseDragged(@Nonnull Vec2i pos) {
        boolean consumed = false;
        // Forward the drag to the widget that received the mouse press
        if (mousePressWidget != null) {
            consumed = mousePressWidget.getMouseHandler().mouseDragged(pos.sub(mousePressWidget.getPosition()));
        }
        for (IWidget widget : widgets) {
            if (widget.contains(pos)) {
                if (widget != mouseOverWidget) {
                    if (mouseOverWidget != null) {
                        // This covers the scenario where the mouse moves from one widget to another.
                        mouseOverWidget.getMouseHandler().mouseExited();
                    }
                    widget.getMouseHandler().mouseEntered();
                    mouseOverWidget = widget;
                }
                break;
            }
            else {
                if (widget == mouseOverWidget) {
                    widget.getMouseHandler().mouseExited();
                    mouseOverWidget = null;
                }
            }
        }
        for (IMouseListener mouseListener : mouseListeners) {
            mouseListener.mouseDragged(pos);
        }
        return alwaysConsume | consumed;
    }

    @Override
    public boolean mouseWheelMoved(float wheel, @Nonnull Vec2i pos) {
        boolean consumed = false;
        for (IWidget widget : widgets) {
            if (widget.contains(pos)) {
                consumed = widget.getMouseHandler().mouseWheelMoved(wheel, pos.sub(widget.getPosition()));
                if (consumed) {
                    break;
                }
            }
        }
        for (IMouseListener mouseListener : mouseListeners) {
            mouseListener.mouseWheelMoved(wheel);
        }
        return alwaysConsume | consumed;
    }

    @Override
    public void addMouseListener(@Nonnull IMouseListener mouseListener) {
        assert !mouseListeners.contains(mouseListener);
        mouseListeners.add(mouseListener);
    }

    @Override
    public void removeMouseListener(@Nonnull IMouseListener mouseListener) {
        assert mouseListeners.contains(mouseListener);
        mouseListeners.remove(mouseListener);
    }

    /**
     * Set the focused widget.
     * <p>
     *     Notifies the widget that it is focused and the old focused widget that it is no longer focused.
     *     Also notifies the focus listeners.
     * </p>
     * @param focusedWidget the focused widget, null to clear the focused widget
     */
    private void setFocusedWidget(@Nullable IWidget focusedWidget) {
        if (focusedWidget != null) {
            if (focusedWidget != this.focusedWidget) {
                if (this.focusedWidget != null) {
                    this.focusedWidget.setFocused(false);
                }
                this.focusedWidget = focusedWidget;
                focusedWidget.setFocused(true);
            }
        }
        else if (this.focusedWidget != null) {
            this.focusedWidget.setFocused(false);
            this.focusedWidget = null;
        }
        for (IFocusManagerListener focusListener : focusListeners) {
            focusListener.focusedWidget(this.focusedWidget);
        }
    }
}
