package cz.bilysklep.trainings.basicjava.graphicshapes;

import org.junit.Test;

import static org.junit.Assert.*;

public class TriangleImplTest {

    private final ReflectionHelper<TriangleImpl> triangleImplClass
            = new ReflectionHelper<>(TriangleImpl.class);

    @Test
    public void testMethodsAndAttributes() {
        triangleImplClass.assertFieldsHaveCorrectIdentifiersAndAccessModifiers();
        triangleImplClass.assertMethodsHaveCorrectIdentifier();
    }

    private static void assertNewTriangle(Color color, Point vertexA, Point vertexB,
            Point vertexC) {

        Triangle triangle = new TriangleImpl(color, vertexA, vertexB, vertexC);

        assertEquals("TriangleImpl.getVertexA() does not return correct value",
                vertexA, triangle.getVertexA());
        assertEquals("TriangleImpl.getVertexB() does not return correct value",
                vertexB, triangle.getVertexB());
        assertEquals("TriangleImpl.getVertexC() does not return correct value",
                vertexC, triangle.getVertexC());
        assertEquals("TriangleImpl.getColor() does not return correct value",
                color, triangle.getColor());
    }

    @Test
    public void testNew() {
        assertNewTriangle(Color.RED, new Point(12, -15), new Point(11, 18), new Point(1, 123));
        assertNewTriangle(Color.GREEN, new Point(11, 1), new Point(1, 8), new Point(-12, -13));
    }

    @Test
    public void testGetSide() {

        Triangle triangle = new TriangleImpl(Color.RED, new Point(12, -15),
                new Point(11, 18), new Point(1, 123));

        assertEquals("TriangleImpl.getSideA() does not return correct value",
                105.5, triangle.getSideA(), 0.1);
        assertEquals("TriangleImpl.getSideB() does not return correct value",
                138.4, triangle.getSideB(), 0.1);
        assertEquals("TriangleImpl.getSideC() does not return correct value",
                33.0, triangle.getSideC(), 0.1);

        triangle = new TriangleImpl(Color.RED, new Point(1, -15),
                new Point(111, -18), new Point(1234, 13));
        assertEquals("TriangleImpl.getSideA() does not return correct value",
                1123.4, triangle.getSideA(), 0.1);
        assertEquals("TriangleImpl.getSideB() does not return correct value",
                1233.3, triangle.getSideB(), 0.1);
        assertEquals("TriangleImpl.getSideC() does not return correct value",
                110.0, triangle.getSideC(), 0.1);
    }

    @Test
    public void testGetArea() {
        String msg = "TriangleImpl.getArea() does not return correct value";
        assertEquals(msg, 112.5, new TriangleImpl(Color.RED, new Point(12, -15),
                new Point(11, 18), new Point(1, 123)).getArea(), 0.1);
        assertEquals(msg, 601, new TriangleImpl(Color.RED, new Point(112, -115),
                new Point(111, 18), new Point(102, 13)).getArea(), 0.1);
        assertEquals(msg, 50.0, new TriangleImpl(Color.RED, new Point(0, 0),
                new Point(10, 0), new Point(0, 10)).getArea(), 0.1);
    }

    @Test
    public void testToString() {

        Triangle triangle = new TriangleImpl(Color.RED, new Point(12, -15),
                new Point(11, 18), new Point(1, 123));
        Triangle triangle2 = new TriangleImpl(Color.BLUE, new Point(12, 15),
                new Point(211, 138), new Point(-11, -11));

        if (triangle.toString() != null && triangle.toString().contains("vertexes")) {
            assertEquals("TriangleImpl.toString() returns wrong value: ",
                "Triangle with vertexes [12, -15], [11, 18], [1, 123] and color RED",
                triangle.toString());
            assertEquals("TriangleImpl.toString() returns wrong value: ",
                "Triangle with vertexes [12, 15], [211, 138], [-11, -11] and color BLUE",
                triangle2.toString());
        } else {
            assertEquals("TriangleImpl.toString() returns wrong value: ",
                "Triangle with vertices [12, -15], [11, 18], [1, 123] and color RED",
                triangle.toString());
            assertEquals("TriangleImpl.toString() returns wrong value: ",
                "Triangle with vertices [12, 15], [211, 138], [-11, -11] and color BLUE",
                triangle2.toString());
        }
    }

    @Test
    public void testGetColor() {
        triangleImplClass.assertMethodDeclaredInAncestor(
                AbstractGraphicShape.class, "getColor");
    }
}
