/*************************************************************************
 * Name:
 * Email:
 *
 * Compilation:  javac Point.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Comparator;

public class Point implements Comparable<Point> {

    // compare points by slope
    public final Comparator<Point> SLOPE_ORDER = new BySlope();       // YOUR DEFINITION HERE

    private final int x;                              // x coordinate
    private final int y;                              // y coordinate

    private class BySlope implements Comparator<Point> {

        @Override
        public int compare(Point p1, Point p2) {
            if (p1 == null || p2 == null) { throw new NullPointerException(); }
            return Double.compare(slopeTo(p1), slopeTo(p2));
        }
    }

    // create the point (x, y)
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    // plot this point to standard drawing
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // slope between this point and that point
    public double slopeTo(Point that) {
        if (that == null) { throw new NullPointerException(); }
        if ((this.x == that.x) && (this.y == that.y)) { return Double.NEGATIVE_INFINITY; } //-inf slope between single point and itself, per API
        double rise = that.y - this.y;
        double run = that.x - this.x;
        double retVal = (run != 0) ? (rise / run) : Double.POSITIVE_INFINITY; //+inf is slope of vertical line, per API
        if (retVal == -0.0d) { retVal = +0.0d; } //shouldn't ever return neg zero
        return retVal;
    }

    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    public int compareTo(Point that) {
        int retVal;
        if ((this.y < that.y) || ((this.y == that.y) && (this.x < that.x))) retVal = -1;
        else if (this.y == that.y && this.x == that.x) retVal = 0;
        else retVal = 1;
        return retVal;
    }

    // return string representation of this point
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    // unit test
    public static void main(String[] args) {
    }
}