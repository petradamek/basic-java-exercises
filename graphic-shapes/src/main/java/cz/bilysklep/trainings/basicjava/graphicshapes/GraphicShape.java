package cz.bilysklep.trainings.basicjava.graphicshapes;

/**
 * This interface represents some graphic shape in 2D space like circle,
 * square, triangle, etc.
 *
 * @author Petr Adamek
 */

public interface GraphicShape {

    /**
     * Computes and returns area of the shape
     * @return area of the shape
     */
    double getArea();

    /**
     * Returns color of this shape
     * @return color of the shape
     */
    Color getColor();

    /**
     * Returns text description of the shape.
     * @return text description of the shape
     */
    @Override
    String toString();

}
