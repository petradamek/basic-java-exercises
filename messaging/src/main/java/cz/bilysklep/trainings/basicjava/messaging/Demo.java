package cz.bilysklep.trainings.basicjava.messaging;

/*
 * Class for sending messages demonstration.
 *
 * @author Tomas Pitner
 * @author Petr Adamek
 */
public class Demo {

    private static void trySend(Messenger messenger, Message message) {
        try {
            messenger.send(message);
        } catch (MessageException ex) {
            System.err.println("Error: " + ex);
        }
    }

    public static void main(String[] args) {

        Messenger messenger = new MyMessenger(new SimpleConnector(), 5);

        trySend(messenger, new Message("1st message 1!", "the_host1"));
        trySend(messenger, new Message("1st message 2!", "the_host1"));
        trySend(messenger, new Message("1st message 3!", "the_host1"));
        trySend(messenger, new Message("1st message 4!", "the_host1"));

        trySend(messenger, new Message("2nd message!", "the_host2"));

        trySend(messenger, new Message("3rd message!", "the_host3"));
    }

}
