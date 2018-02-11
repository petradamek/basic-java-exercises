package cz.bilysklep.trainings.basicjava.classesandinstances;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.*;

@Ignore
public class MainTest {

    @Test
    public void testMain() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        try (PrintStream outMock = new PrintStream(byteOutput)) {
            System.setOut(outMock);

            Main.main();

            String expectedOutput = String.format(
                    "John Novak, born 1970-07-17%n"
                    + "Jeniffer Smith, born 2000-02-29%n");

            String actualOutput = new String(byteOutput.toByteArray());

            assertEquals(expectedOutput, actualOutput);

        } finally {
            System.setOut(originalOut);
        }

    }
}
