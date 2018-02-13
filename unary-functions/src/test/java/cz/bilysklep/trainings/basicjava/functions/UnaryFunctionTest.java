package cz.bilysklep.trainings.basicjava.functions;

import static org.junit.Assert.*;
import org.junit.Test;

public class UnaryFunctionTest {

    void assertFunction(UnaryFunction function, double delta,
            double[] xValues, double[] yValues) {

        for (int i = 0; i < xValues.length; i++) {
            if (Double.isNaN(yValues[i])) {
                assertFalse("Function [" + function + "] should not be defined "
                        + "for value x = " + xValues[i] + ", but method "
                        + "isDefinedFor(double x) returns true",
                        function.isDefinedFor(xValues[i]));
                assertTrue("Function [" + function + "] should return NaN for "
                        + "value x = " + xValues[i],
                        Double.isNaN(function.getValue(xValues[i])));
            } else {
                assertTrue("Function [" + function + "] should be defined "
                        + "for value x = " + xValues[i] + ", but method "
                        + "isDefinedFor(double x) returns false",
                        function.isDefinedFor(xValues[i]));
                assertEquals("Function [" + function + "] does not return "
                        + "excpected value y for value x = " + xValues[i] + ": ",
                        yValues[i], function.getValue(xValues[i]), delta);
            }
        }
    }

    @Test
    public void testSimpleSinus() {
        UnaryFunction simpleSinus = new SinusFunction(1, 1);
        assertFunction(simpleSinus, 0.00001,
                new double[]{-2 * Math.PI, -Math.PI, -1, 0, 1, Math.PI / 2, Math.PI},
                new double[]{0, 0, -0.84147, 0, 0.84147, 1, 0});
    }

    @Test
    public void testSimpleSinusToString() {
        UnaryFunction simpleSinus = new SinusFunction(1, 1);
        assertEquals("1.0 * sin(1.0 * x)", simpleSinus.toString());
    }

    @Test
    public void testSinusA() {
        UnaryFunction simpleSinus = new SinusFunction(2, 1);
        assertFunction(simpleSinus, 0.00001,
                new double[]{-2 * Math.PI, -Math.PI, -1, 0, 1, Math.PI / 2, Math.PI},
                new double[]{0, 0, -1.68294, 0, 1.68294, 2, 0});
    }

    @Test
    public void testSinusAToString() {
        UnaryFunction simpleSinus = new SinusFunction(2, 1);
        assertEquals("2.0 * sin(1.0 * x)", simpleSinus.toString());
    }

    @Test
    public void testSinusB() {
        UnaryFunction simpleSinus = new SinusFunction(1, 2);
        assertFunction(simpleSinus, 0.00001,
                new double[]{-Math.PI, -Math.PI / 2, -0.5, 0, 0.5, Math.PI / 4, Math.PI / 2},
                new double[]{0, 0, -0.84147, 0, 0.84147, 1, 0});
    }

    @Test
    public void testSinusBToString() {
        UnaryFunction simpleSinus = new SinusFunction(1, 2);
        assertEquals("1.0 * sin(2.0 * x)", simpleSinus.toString());
    }

    @Test
    public void testSinusAB() {
        UnaryFunction simpleSinus = new SinusFunction(2, 2);
        assertFunction(simpleSinus, 0.00001,
                new double[]{-Math.PI, -Math.PI / 2, -0.5, 0, 0.5, Math.PI / 4, Math.PI / 2},
                new double[]{0, 0, -1.68294, 0, 1.68294, 2, 0});
    }

    @Test
    public void testSinusABToString() {
        UnaryFunction simpleSinus = new SinusFunction(2, 2);
        assertEquals("2.0 * sin(2.0 * x)", simpleSinus.toString());
    }

    @Test
    public void testSquareRoot() {
        UnaryFunction squareRoot = new SquareRootFunction();
        final double NaN = Double.NaN;
        assertFunction(squareRoot, 0.00001,
                new double[]{-10, -1, -Double.MIN_VALUE, 0, Double.MIN_VALUE, 1, 2, 4, 16},
                new double[]{NaN, NaN, NaN, 0, 2.22E-162, 1, 1.41421, 2, 4});
    }

    @Test
    public void testSquareRootToString() {
        UnaryFunction squareRoot = new SquareRootFunction();
        assertEquals("sqrt(x)", squareRoot.toString());
    }

    @Test
    public void testPolynomialZero() {
        UnaryFunction polynomial = new PolynomialFunction(0);
        assertFunction(polynomial, 0.00001,
                new double[]{-10, -1, -Double.MIN_VALUE, 0, Double.MIN_VALUE, 1, 10},
                new double[]{0, 0, 0, 0, 0, 0, 0});
    }

    @Test
    public void testPolynomialZeroToString() {
        UnaryFunction polynomial = new PolynomialFunction(0);
        assertEquals("0", polynomial.toString());
    }

    @Test
    public void testPolynomialConst() {
        UnaryFunction polynomial = new PolynomialFunction(Math.E);
        assertFunction(polynomial, 0.00001,
                new double[]{-10, -1, -Double.MIN_VALUE, 0, Double.MIN_VALUE, 1, 10},
                new double[]{Math.E, Math.E, Math.E, Math.E, Math.E, Math.E, Math.E});
    }

    @Test
    public void testPolynomialConstToString() {
        UnaryFunction polynomial = new PolynomialFunction(Math.E);
        assertEquals("2.718281828459045", polynomial.toString());
    }

    @Test
    public void testPolynomialLinear() {
        UnaryFunction polynomial = new PolynomialFunction(2, 3);
        assertFunction(polynomial, 0.00001,
                new double[]{-10, -1, -Double.MIN_VALUE, 0, Double.MIN_VALUE, 1, 10},
                new double[]{-28, -1, 2, 2, 2, 5, 32});
    }

    @Test
    public void testPolynomialLinearToString() {
        UnaryFunction polynomial = new PolynomialFunction(2, 3);
        assertEquals("2.0 + (3.0 * x)", polynomial.toString());
    }

    @Test
    public void testPolynomialSquare() {
        UnaryFunction polynomial = new PolynomialFunction(2, 3, 4);
        assertFunction(polynomial, 0.00001,
                new double[]{-10, -1, -Double.MIN_VALUE, 0, Double.MIN_VALUE, 1, 10},
                new double[]{372, 3, 2, 2, 2, 9, 432});
    }

    @Test
    public void testPolynomialSquareToString() {
        UnaryFunction polynomial = new PolynomialFunction(2, 3, 4);
        assertEquals("2.0 + (3.0 * x) + (4.0 * x^2)", polynomial.toString());
    }

    @Test
    public void testPolynomialCubic() {
        UnaryFunction polynomial = new PolynomialFunction(2, 3, 4, 5);
        assertFunction(polynomial, 0.00001,
                new double[]{-10, -1, -Double.MIN_VALUE, 0, Double.MIN_VALUE, 1, 10},
                new double[]{-4628, -2, 2, 2, 2, 14, 5432});
    }

    @Test
    public void testPolynomialCubicToString() {
        UnaryFunction polynomial = new PolynomialFunction(2, 3, 4, 5);
        assertEquals("2.0 + (3.0 * x) + (4.0 * x^2) + (5.0 * x^3)", polynomial.toString());
    }

    @Test
    public void testPolynomialToString() {
        assertEquals("(3.0 * x) + (4.0 * x^2) + (5.0 * x^3)",
                new PolynomialFunction(0, 3, 4, 5).toString());

        assertEquals("2.0 + (4.0 * x^2) + (5.0 * x^3)",
                new PolynomialFunction(2, 0, 4, 5).toString());

        assertEquals("2.0 + (3.0 * x) + (5.0 * x^3)",
                new PolynomialFunction(2, 3, 0, 5).toString());

        assertEquals("2.0 + (5.0 * x^3)",
                new PolynomialFunction(2, 0, 0, 5).toString());

        assertEquals("(5.0 * x^3)",
                new PolynomialFunction(0, 0, 0, 5).toString());
    }
}
