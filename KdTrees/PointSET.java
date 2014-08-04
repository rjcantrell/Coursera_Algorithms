public class PointSET {
    private SET<Point2D> storage;
    public PointSET() {                              // construct an empty set of points
        storage = new SET<Point2D>();
    }

    public boolean isEmpty() {                       // is the set empty?
        return storage.isEmpty();
    }

    public int size() {                               // number of points in the set
        return storage.size();
    }

    public void insert(Point2D p) {                   // add the point p to the set (if it is not already in the set)
        storage.add(p);
    }

    public boolean contains(Point2D p)  {             // does the set contain the point p?
        return storage.contains(p);
    }

    public void draw() {                              // draw all of the points to standard draw
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        for (Point2D p : storage) {
            StdDraw.point(p.x(), p.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect) {     // all points in the set that are inside the rectangle
        Queue<Point2D> ps = new Queue<Point2D>();
        for (Point2D p : storage) {
            if (rect.contains(p)) { ps.enqueue(p); }
        }
        return ps;
    }

    public Point2D nearest(Point2D p) {              // a nearest neighbor in the set to p; null if set is empty
        Point2D retVal = null;
        double nearestDist = Float.MAX_VALUE;

        for (Point2D curr : storage) {
            if (retVal == null) { retVal = curr; }
            double thisDist = curr.distanceTo(p);
            if (thisDist < nearestDist) {
                retVal = curr;
                nearestDist = thisDist;
            }
        }

        return retVal;
    }
}