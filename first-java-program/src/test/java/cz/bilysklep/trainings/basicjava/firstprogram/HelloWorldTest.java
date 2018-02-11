package cz.bilysklep.trainings.basicjava.firstprogram;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class HelloWorldTest {

    @Test
    public void emptyTestToCheckIfTestsAreRunning() {
        // This test is empty, so it will always pass
    }

    @Test
    public void testMain() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        try (PrintStream outMock = new PrintStream(byteOutput)) {
            System.setOut(outMock);

            HelloWorld.main();

            String expectedOutput = String.format("Hello from first java program%n");

            String actualOutput = new String(byteOutput.toByteArray());

            assertEquals(expectedOutput, actualOutput);

        } finally {
            System.setOut(originalOut);
        }

    }

}
