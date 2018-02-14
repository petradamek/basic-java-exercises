package cz.bilysklep.trainings.basicjava.messaging;

import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * This class simulates simple unreliable Connector. Sending the message usually
 * fails and several attempts are necessary for success.
 *
 * @author Tomas Pitner
 * @author Petr Adamek
 */
public class SimpleConnector implements Connector {

    private final Random random = new Random();
    private int retries = random.nextInt(10);
    private boolean nrthFailure = false;

    /**
     * Establish and returns connection to computer with given address.
     *
     * @param target address of target computer
     * @throws java.net.UnknownHostException if the address does not exist
     * @throws java.net.NoRouteToHostException if the connection with target
     * computer can't be established due to insufficient routing information
     * @return established connection
     */
    @Override
    public Connection getConnection(String target) throws UnknownHostException,
            NoRouteToHostException {

        retries--;
        if (retries <= 0) {
            retries = random.nextInt(10);
            if (nrthFailure = !nrthFailure) {
                throw new NoRouteToHostException("No route to " + target
                        + " (counter: " + retries + ").");
            } else {
                throw new UnknownHostException("Unknown host: " + target
                        + " (counter: " + retries + ").");
            }
        } else {
            return new SimpleConnection(target);
        }
    }

    private class SimpleConnection implements Connection {

        private int retries = random.nextInt(8);
        private final String target;

        private SimpleConnection(String target) {
            this.target = target;
        }

        @Override
        public void sendData(String data) throws IOException {
            retries--;
            if (retries > 0) {
                throw new IOException("Cannot send data (counter: " + retries + ")");
            } else {
                retries = random.nextInt(8);
                System.out.println("Data '" + data + "' sent to '" + target + "'.");
            }
        }

        @Override
        public void close() throws IOException {
        }
    }

}
