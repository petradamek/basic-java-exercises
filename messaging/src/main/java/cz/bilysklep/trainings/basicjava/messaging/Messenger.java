package cz.bilysklep.trainings.basicjava.messaging;

/**
 * This interface represents a component which is capable to send messages.
 *
 * @author Tomas Pitner
 * @author Petr Adamek
 */
public interface Messenger {

    /**
     * Send the message to appropriate recipient.
     *
     * @param message message to be sent
     * @throws MessageException when the message sending fail
     */
    public void send(Message message) throws MessageException;

}
