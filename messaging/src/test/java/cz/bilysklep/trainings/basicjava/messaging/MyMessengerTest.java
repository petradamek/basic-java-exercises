package cz.bilysklep.trainings.basicjava.messaging;

import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.UnknownHostException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testing class for messaging task.
 *
 * @author Petr Adamek
 */
public class MyMessengerTest {

    @Test
    public void testRules() {
        assertClassFollowRules(MessageException.class);
        assertClassFollowRules(CannotSendException.class);
        assertClassFollowRules(TargetUnreachableException.class);
        assertClassFollowRules(MyMessenger.class);
    }

    private <T> void assertClassFollowRules(Class<T> clazz) {
        ReflectionHelper<T> reflectionHelper = new ReflectionHelper<>(clazz);
        reflectionHelper.assertFieldsHaveCorrectIdentifiersAndAccessModifiers();
        reflectionHelper.assertMethodsHaveCorrectIdentifier();
    }

    private void assertAncestor(Class ancestor, Class clazz) {
        assertThat(clazz.getSuperclass())
                .withFailMessage("Class %s is not direct descendand of class %s", clazz, ancestor)
                .isEqualTo(ancestor);
    }

    @Test
    public void exceptionHierarchy() {
        assertAncestor(Exception.class, MessageException.class);
        assertAncestor(MessageException.class, CannotSendException.class);
        assertAncestor(MessageException.class, TargetUnreachableException.class);
    }

    @Test
    public void newMyMessengerWithNullConnector() {
        assertThatExceptionOfType(NullPointerException.class)
                .describedAs("calling constructor with null connector")
                .isThrownBy(() -> new MyMessenger(null, 1))
                .withMessage("connector is null");
    }

    @Test
    public void newMyMessengerWithZeroMaxRetries() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .describedAs("calling constructor with negative maxSendAttemptCount")
                .isThrownBy(() -> new MyMessenger(mock(Connector.class), 0))
                .withMessage("maxSendAttemptCount is less than one: 0");
    }

    @Test
    public void newMyMessengerWithNegativeMaxRetries() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .describedAs("calling constructor with negative maxSendAttemptCount")
                .isThrownBy(() -> new MyMessenger(mock(Connector.class), -1))
                .withMessage("maxSendAttemptCount is less than one: -1");
    }

    @Test
    public void sendData() throws IOException, MessageException {
        Connector connector = mock(Connector.class);
        Connection connection = mock(Connection.class);
        when(connector.getConnection("address1")).thenReturn(connection);

        Messenger messenger = new MyMessenger(connector, 1);
        messenger.send(new Message("messageText", "address1"));

        verify(connector).getConnection("address1");
        verify(connection).sendData("messageText");
        verify(connection).close();
        verifyNoMoreInteractions(connector, connection);
    }

    @Test
    public void connectWithUnknownHostException() throws IOException {

        Connector connector = mock(Connector.class);
        UnknownHostException unknownHostException = new UnknownHostException("msg-uh");
        when(connector.getConnection("address1")).thenThrow(unknownHostException);

        Messenger messenger = new MyMessenger(connector, 5);

        assertThatExceptionOfType(TargetUnreachableException.class)
                .isThrownBy(() -> messenger.send(new Message("messageText", "address1")))
                .withMessage("Error when connecting to 'address1'")
                .withCause(unknownHostException);

        verify(connector).getConnection("address1");
        verifyNoMoreInteractions(connector);

    }

    @Test
    public void connectWithNoRouteToHostException() throws IOException {

        Connector connector = mock(Connector.class);
        NoRouteToHostException noRouteToHostException = new NoRouteToHostException("msg-nrth");
        when(connector.getConnection("address1")).thenThrow(noRouteToHostException);

        Messenger messenger = new MyMessenger(connector, 5);

        assertThatExceptionOfType(TargetUnreachableException.class)
                .isThrownBy(() -> messenger.send(new Message("messageText", "address1")))
                .withMessage("Error when connecting to 'address1'")
                .withCause(noRouteToHostException);

        verify(connector).getConnection("address1");
        verifyNoMoreInteractions(connector);
    }

    @Test
    public void sendDataWithErrorAndNoRetry() throws IOException, MessageException {

        Connector connector = mock(Connector.class);
        Connection connection = mock(Connection.class);

        IOException ioException = new IOException("msg-io");

        when(connector.getConnection("address1")).thenReturn(connection);

        doThrow(ioException)
                .when(connection).sendData("messageText");

        Messenger messenger = new MyMessenger(connector, 1);

        assertThatExceptionOfType(CannotSendException.class)
                .isThrownBy(() -> messenger.send(new Message("messageText", "address1")))
                .withMessage("Error when sending message 'messageText' to 'address1'")
                .withCause(ioException);

        verify(connector).getConnection("address1");
        verify(connection).sendData("messageText");
        verify(connection).close();
        verifyNoMoreInteractions(connector, connection);

    }

    @Test
    public void sendDataWithErrorAndRetryIsOk() throws IOException, MessageException {

        Connector connector = mock(Connector.class);
        Connection connection = mock(Connection.class);

        IOException ioException = new IOException("msg-io");

        when(connector.getConnection("address1")).thenReturn(connection);
        doThrow(ioException).doNothing()
                .when(connection).sendData("messageText");

        Messenger messenger = new MyMessenger(connector, 2);
        messenger.send(new Message("messageText", "address1"));

        verify(connector).getConnection("address1");
        verify(connection, times(2)).sendData("messageText");
        verify(connection).close();
        verifyNoMoreInteractions(connector, connection);

    }

    @Test
    public void sendDataWithErrorAndRetryIsNotOk() throws IOException, MessageException {

        Connector connector = mock(Connector.class);
        Connection connection = mock(Connection.class);

        IOException ioException1 = new IOException("msg-io-1");
        IOException ioException2 = new IOException("msg-io-2");

        when(connector.getConnection("address1")).thenReturn(connection);

        doThrow(ioException1, ioException2)
                .when(connection).sendData("messageText");

        Messenger messenger = new MyMessenger(connector, 2);

        assertThatExceptionOfType(CannotSendException.class)
                .isThrownBy(() -> messenger.send(new Message("messageText", "address1")))
                .withMessage("Error when sending message 'messageText' to 'address1'")
                .withCause(ioException2);

        verify(connector).getConnection("address1");
        verify(connection, times(2)).sendData("messageText");
        verify(connection).close();
        verifyNoMoreInteractions(connector, connection);

    }
}
