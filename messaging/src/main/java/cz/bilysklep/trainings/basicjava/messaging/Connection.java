package cz.bilysklep.trainings.basicjava.messaging;

import java.io.Closeable;
import java.io.IOException;

/**
 * This class represents an established connection to some computer.
 *
 * @author Tomas Pitner
 * @author Petr Adamek
 */
public interface Connection extends Closeable {

    /**
     * Send some data to target computer.
     *
     * @param data data to be send
     * @throws IOException if any error occurred during sending data
     */
    public void sendData(String data) throws IOException;

}
