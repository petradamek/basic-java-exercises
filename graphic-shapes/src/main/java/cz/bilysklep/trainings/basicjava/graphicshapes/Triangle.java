package cz.bilysklep.trainings.basicjava.graphicshapes;

/**
 * This interface represents the Triangle.
 *
 * @author Petr Adamek
 */
public interface Triangle extends GraphicShape {

    /**
     * Returns length of side a.
     *
     * @return length of side a
     */
    double getSideA();

    /**
     * Returns length of side b.
     *
     * @return length of side b
     */
    double getSideB();

    /**
     * Returns length of side c.
     *
     * @return length of side c
     */
    double getSideC();

    /**
     * Returns vertex A.
     *
     * @return vertex A
     */
    Point getVertexA();

    /**
     * Returns vertex B.
     *
     * @return vertex B
     */
    Point getVertexB();

    /**
     * Returns vertex C.
     *
     * @return vertex C
     */
    Point getVertexC();

}
