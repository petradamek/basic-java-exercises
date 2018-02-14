package cz.bilysklep.trainings.basicjava.graphicshapes;

import org.junit.Test;

import static org.junit.Assert.*;

public class CircleImplTest {

    private final ReflectionHelper<CircleImpl> circleImplClass
            = new ReflectionHelper<>(CircleImpl.class);

    @Test
    public void testMethodsAndAttributes() {
        circleImplClass.assertFieldsHaveCorrectIdentifiersAndAccessModifiers();
        circleImplClass.assertMethodsHaveCorrectIdentifier();
    }

    @Test
    public void testNew() {
        Point center = new Point(12, -15);
        Circle circle = new CircleImpl(Color.RED, center, 13.25);

        assertEquals("CircleImpl.getCenter() does not return correct value",
                center, circle.getCenter());
        assertEquals("CircleImpl.getColor() does not return correct value",
                Color.RED, circle.getColor());
        assertEquals("CircleImpl.getRadius() does not return correct value",
                13.25, circle.getRadius(), 1e-10);

        Point center2 = new Point(122, 18);
        Circle circle2 = new CircleImpl(Color.BLUE, center2, 11.0);

        assertEquals("CircleImpl.getCenter() does not return correct value",
                center2, circle2.getCenter());
        assertEquals("CircleImpl.getColor() does not return correct value",
                Color.BLUE, circle2.getColor());
        assertEquals("CircleImpl.getRadius() does not return correct value",
                11.0, circle2.getRadius(), 1e-10);
    }

    @Test
    public void testGetArea() {
        Point center = new Point(12, -15);
        String msg = "CircleImpl.getArea() does not return correct value";
        assertEquals(msg, 551.5, new CircleImpl(Color.RED, center, 13.25).getArea(), 0.1);
        assertEquals(msg, Math.PI, new CircleImpl(Color.RED, center, 1).getArea(), 0.1);
    }

    @Test
    public void testToString() {
        Point center = new Point(12, -15);
        Circle circle = new CircleImpl(Color.RED, center, 13.25);

        assertEquals("CircleImpl.toString() returns wrong value: ",
                "Circle with center [12, -15], radius 13.25 and color RED",
                circle.toString());

        Point center2 = new Point(122, 18);
        Circle circle2 = new CircleImpl(Color.BLUE, center2, 11.0);

        assertEquals("CircleImpl.toString() returns wrong value: ",
                "Circle with center [122, 18], radius 11.0 and color BLUE",
                circle2.toString());
    }

    @Test
    public void testGetColor() {
        circleImplClass.assertMethodDeclaredInAncestor(
                AbstractGraphicShape.class, "getColor");
    }
}
