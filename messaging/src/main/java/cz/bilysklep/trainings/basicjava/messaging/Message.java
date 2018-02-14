package cz.bilysklep.trainings.basicjava.messaging;

/**
 * This class represents a message which can be sent to recipient.
 *
 * @author Tomas Pitner
 * @author Petr Adamek
 */
public class Message {

    private final String text;
    private final String target;

    /**
     * Create new message with given text for given recipient.
     *
     * @param text message test
     * @param target recipient address
     */
    public Message(String text, String target) {
        this.text = text;
        this.target = target;
    }

    /**
     * Returns message text.
     *
     * @return message text
     */
    public String getText() {
        return text;
    }

    /**
     * Returns recipient address.
     *
     * @return recipient address
     */
    public String getTarget() {
        return target;
    }

    /**
     * Returns string with message description
     *
     * @return message description
     */
    @Override
    public String toString() {
        return "Message for " + target + ": " + text;
    }
}
