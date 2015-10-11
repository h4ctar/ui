package ben.ui.input.key;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ben.ui.input.IFocusManagerListener;
import ben.ui.input.IFocusManager;

import com.jogamp.newt.event.KeyEvent;

/**
 * Container Key Handler Test.
 */
public class ContainerKeyHandlerTest {

    /**
     * The key handler.
     */
    private ContainerKeyHandler keyHandler;

    /**
     * Mock focus manager.
     */
    @Mock
    private IFocusManager focusManager;

    /**
     * Mock key listener.
     */
    @Mock
    private IKeyListener keyListener;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        keyHandler = new ContainerKeyHandler(focusManager);
    }

    /**
     * Test create.
     */
    @Test
    public void testCreate() {
        Mockito.verify(focusManager).addFocusListener(Mockito.any(IFocusManagerListener.class));
    }

    // testDestroy

    /**
     * Test key pressed when no widget is focused.
     */
    @Test
    public void testKeyPressedNoWidgetFocused() {
        final ArgumentCaptor<IFocusManagerListener> focusListenerCaptor = ArgumentCaptor.forClass(IFocusManagerListener.class);
        Mockito.verify(focusManager).addFocusListener(focusListenerCaptor.capture());

        final IFocusManagerListener focusListener = focusListenerCaptor.getValue();
        focusListener.focusedWidget(null);

        keyHandler.addKeyListener(keyListener);

        final KeyEvent keyEvent = KeyEvent.create((short) 0, this, 0, 0, (short) 0, (short) 0, (char) 0);
        keyHandler.keyPressed(keyEvent);

        Mockito.verify(keyListener).keyPressed(keyEvent);
    }
}
