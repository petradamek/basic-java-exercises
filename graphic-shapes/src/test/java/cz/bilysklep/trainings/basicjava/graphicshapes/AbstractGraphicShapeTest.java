package cz.bilysklep.trainings.basicjava.graphicshapes;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for AbstractGraphicShape
 *
 * @author Petr Adamek
 */
public class AbstractGraphicShapeTest {

    private final ReflectionHelper<AbstractGraphicShape> abstractGraphicShapeClass
            = new ReflectionHelper<>(AbstractGraphicShape.class);

    @Test
    public void testMethodsAndAttributes() {
        abstractGraphicShapeClass.assertFieldsHaveCorrectIdentifiersAndAccessModifiers();
        abstractGraphicShapeClass.assertMethodsHaveCorrectIdentifier();
    }

    @Test
    public void testColorAttribute() {
        abstractGraphicShapeClass.assertHasPrivateFinalField(Color.class, "color");
    }

    @Test
    public void testConstructor() {
        abstractGraphicShapeClass.getConstructor(Color.class);
        assertTrue("Class " + AbstractGraphicShape.class.getName()
                + " contains some unexpected extra constructor",
                AbstractGraphicShape.class.getDeclaredConstructors().length == 1);
    }

    @Test
    public void testGetColor() {
        String msg = "getColor() method does not return color which was passed "
                + "as constructor parameter (bug could be in constructor or in "
                + "getColor() method";

        AbstractGraphicShape instance = new AbstractGraphicShapeImpl(Color.GREEN);
        assertEquals(msg, Color.GREEN, instance.getColor());
        instance = new AbstractGraphicShapeImpl(Color.BLUE);
        assertEquals(msg, Color.BLUE, instance.getColor());
    }

    @Test
    public void testToString() {
        Method toStringMethod;
        try {
            toStringMethod = AbstractGraphicShape.class.getMethod("toString");
        } catch (NoSuchMethodException ex) {
            // This should never happen, toString() is defined in Object class
            throw new AssertionError(AbstractGraphicShape.class.getName()
                    + ".toString() method not found", ex);
        }
        assertTrue("Method toString() in AbstractGraphicShape class "
                + "should be abstract or it should not be defined here",
                Modifier.isAbstract(toStringMethod.getModifiers())
                || !toStringMethod.getDeclaringClass()
                        .equals(AbstractGraphicShape.class));
    }

    private class AbstractGraphicShapeImpl extends AbstractGraphicShape {

        AbstractGraphicShapeImpl(Color color) {
            super(color);
        }

        public String toString() {
            throw new UnsupportedOperationException();
        }

        public double getArea() {
            throw new UnsupportedOperationException();
        }

    }

}
