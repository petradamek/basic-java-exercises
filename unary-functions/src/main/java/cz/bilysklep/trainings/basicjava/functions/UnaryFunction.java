package cz.bilysklep.trainings.basicjava.functions;

/**
 * This interface represents some unary function.
 *
 * @author Petr Adamek
 */
public interface UnaryFunction {
    
    /**
     * Returns true if this function has defined value for given argument x.
     *
     * @param x argument of function
     * @return true, if this function is defined for given argument
     */
    boolean isDefinedFor(double x);

    /**
     * Returns value of this function for given argument x.
     *
     * @param x argument of function
     * @return value of function for given argument x
     */
    double getValue(double x);

    /**
     * Returns symbolic definition of given function.
     *
     * @return symbolic definition of given function.
     */
    @Override
    String toString();
    
}
