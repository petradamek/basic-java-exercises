package cz.bilysklep.trainings.basicjava.graphicshapes;

/**
 * This interface represents the Circle.
 *
 * @author Petr Adamek
 */
public interface Circle extends GraphicShape {

    /**
     * Returns center of the circle
     *
     * @return center of the circle
     */
    Point getCenter();

    /**
     * Returns radius
     *
     * @return radius
     */
    double getRadius();

}
