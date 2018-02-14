package cz.bilysklep.trainings.basicjava.graphicshapes;

/**
 * This class represents the point in 2D space.
 *
 * @author Petr Adamek
 */
public class Point {

    /**
     * Coordinate x
     */
    private final int x;
    /**
     * Coordinate y
     */
    private final int y;

    /**
     * Creates new point.
     *
     * @param x coordinate x
     * @param y coordinate y
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns description of the points.
     *
     * @return the position of the point
     */
    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }

    /**
     * Returns coordinate x.
     *
     * @return coordinate x
     */
    public int getX() {
        return x;
    }

    /**
     * Returns coordinate y.
     *
     * @return coordinate y
     */
    public int getY() {
        return y;
    }
}
