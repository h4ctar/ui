package ben.ui.action;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Multi Action Test.
 */
public class MultiActionTest {

    /**
     * Setup.
     */
    @Before
    public void setup() {

    }

    /**
     * Test is executable.
     */
    @Test
    public void testIsExecutable() {
        IAction action1 = mock(IAction.class);
        IAction action2 = mock(IAction.class);

        MultiAction multiAction = new MultiAction(action1, action2);

        ArgumentCaptor<IActionListener> listenerCaptor1 = ArgumentCaptor.forClass(IActionListener.class);
        verify(action1).addListener(listenerCaptor1.capture());
        assertThat(listenerCaptor1.getValue(), notNullValue());

        ArgumentCaptor<IActionListener> listenerCaptor2 = ArgumentCaptor.forClass(IActionListener.class);
        verify(action2).addListener(listenerCaptor2.capture());
        assertThat(listenerCaptor2.getValue(), notNullValue());

        when(action1.isExecutable()).thenReturn(true);
        when(action2.isExecutable()).thenReturn(true);
        listenerCaptor1.getValue().actionChanged();
        listenerCaptor2.getValue().actionChanged();
        assertThat(multiAction.isExecutable(), equalTo(true));

        when(action1.isExecutable()).thenReturn(false);
        listenerCaptor1.getValue().actionChanged();
        assertThat(multiAction.isExecutable(), equalTo(false));
    }

    /**
     * Test execute.
     */
    @Test
    public void testExecute() {

    }
}