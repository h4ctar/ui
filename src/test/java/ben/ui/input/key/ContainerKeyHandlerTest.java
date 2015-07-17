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

public class ContainerKeyHandlerTest {

    private ContainerKeyHandler keyHandler;

    @Mock
    private IFocusManager focusManager;

    @Mock
    private IKeyListener keyListener;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        keyHandler = new ContainerKeyHandler(focusManager);
    }

    @Test
    public void testCreate() {
        Mockito.verify(focusManager).addFocusListener(Mockito.any(IFocusManagerListener.class));
    }

    // testDestroy

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
