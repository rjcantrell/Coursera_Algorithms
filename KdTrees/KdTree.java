public class KdTree {

    private static class Node {
        private Point2D p;          // the point
        //private RectHV rect;      // the axis-aligned rectangle corresponding to this node
        private Node lb;            // the left/bottom subtree
        private Node rt;            // the right/top subtree
        //private int N;              // size of the subtree (inclusive of this node)
        private boolean isVertical; //way smarter than all that HNode / VNode subclassing I tried

        public Node(Point2D p, /*int n,*/ boolean v) {
            this.p = p;
            //N = n;
            isVertical = v;
        }
    }

    private Node root;
    private int size; //putting this on the node means we recalc it all the time...
    private RectHV TOP = new RectHV(0, 0, 1, 1); //smarter than calling it out manually; reused

    public KdTree() {                               // construct an empty set of points
        root = null; //not really needed, I guess, but...
        size = 0;
    }

    public boolean isEmpty() {                       // is the set empty?
        return (root == null);
    }

    public int size() {                               // number of points in the set
        return size;
    }

    public void insert(Point2D p) {                   // add the point p to the set (if it is not already in the set)
        root = insert(root, p, true);
    }

    public boolean contains(Point2D p)  {             // does the set contain the point p?
        return contains(root, p);
    }

    public void draw() {                              // draw all of the points to standard draw
        TOP.draw();
        draw(root, TOP);
    }

    public Iterable<Point2D> range(RectHV rect) {     // all points in the set that are inside the rectangle
        final Queue<Point2D> retVal = new Queue<Point2D>();
        range(root, rect, retVal, TOP);
        return retVal;
    }

    public Point2D nearest(Point2D p) {              // a nearest neighbor in the set to p; null if set is empty
        return nearest(root, TOP, p, null);
    }

    public static void main(String[] args) {
        KdTree test = new KdTree();
        Point2D p = new Point2D(0.5, 0.5);
        Point2D q = new Point2D(0.5, 0.5);
        Point2D r = new Point2D(0.7, 0.7);
        Point2D s = new Point2D(0.9, 0.9);
        StdOut.println("test contains " + p.toString() + " = " + test.contains(p));
        StdOut.println("adding " + r.toString() + " to test");
        test.insert(r);
        StdOut.println("adding " + q.toString() + " to test");
        test.insert(p);
        StdOut.println("test contains " + p.toString() + " = " + test.contains(p));
        StdOut.println("test contains " + q.toString() + " = " + test.contains(q));
        StdOut.println("test contains " + r.toString() + " = " + test.contains(r));
        StdOut.println("test contains " + s.toString() + " = " + test.contains(s));

        //test.draw();

        String filename = args[0];
        In in = new In(filename);

        // initialize the data structures with N points from standard input
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            StdDraw.clear();
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D z = new Point2D(x, y);
            kdtree.insert(z);
            kdtree.draw();
            while (!StdDraw.hasNextKeyTyped()) { } //pause until key is pressed
            StdDraw.nextKeyTyped();
        }

    }

    private Node insert(Node n, Point2D p, boolean vert) {
        if (n == null) {
            size++; //not being able to inline this if is the ONE bad part of not keeping size on the node
            return new Node(p, vert);
        }
        if (n.p.x() == p.x() & n.p.y() == p.y()) { return n; } //maybe n.p = p isn't good enough for the grader....
        if ((n.isVertical && p.x() < n.p.x()) || (!n.isVertical && p.y() < n.p.y())) {
            n.lb = insert(n.lb, p, !n.isVertical);
        } else {
            n.rt = insert(n.rt, p, !n.isVertical); //always negating keeps me from that abstract class that returned the opposite subtype
        }

        //N = (lb != null ? lb.N : 0) + (rt != null ? rt.N : 0) + 1;
        //instead of storing the rect on the node, how about we just calc it live when searching?
        return n;
    }

    private boolean contains(Node n, Point2D p) {   //originally this returned the node, but I never used the value...
        if (n == null) { return false; }
        if (n.p.x() == p.x() && n.p.y() == p.y()) { return true; }
        if (n.isVertical && p.x() < n.p.x() || !n.isVertical && p.y() < n.p.y()) { return contains(n.lb, p); }
        else { return contains(n.rt, p); }
    }

    private void range(Node n, RectHV rect, Queue<Point2D> queue, RectHV nodeRect) {
        if (n == null) { return; } //end of a branch -- nothing to do!
        if (rect.intersects(nodeRect)) {
            if (rect.contains(n.p)) { queue.enqueue(n.p); }
            range(n.lb, rect, queue, leftRect(nodeRect, n));
            range(n.rt, rect, queue, rightRect(nodeRect, n));
        }
    }

    private RectHV leftRect(RectHV rect, Node n)
    {
        return n.isVertical ? new RectHV(rect.xmin(), rect.ymin(), n.p.x(), rect.ymax())
                            : new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), n.p.y());
    }

    private RectHV rightRect(RectHV rect, Node n)
    {
        return n.isVertical ? new RectHV(n.p.x(), rect.ymin(), rect.xmax(), rect.ymax())
                            : new RectHV(rect.xmin(), n.p.y(), rect.xmax(), rect.ymax());
    }

    private void draw(Node n, RectHV rect) {
        if (n == null) { return; }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        StdDraw.point(n.p.x(), n.p.y());

        Point2D start;
        Point2D stop;
        if (n.isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            start = new Point2D(n.p.x(), rect.ymin());
            stop = new Point2D(n.p.x(), rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            start = new Point2D(rect.xmin(), n.p.y());
            stop = new Point2D(rect.xmax(), n.p.y());
        }
        StdDraw.setPenRadius();
        StdDraw.line(start.x(), start.y(), stop.x(), stop.y());

        draw(n.lb, leftRect(rect, n));
        draw(n.rt, leftRect(rect, n));
    }

    private Point2D nearest(Node n, RectHV rect, Point2D p, Point2D bestSoFar)
    {
        if (n == null) return bestSoFar;

        double dqn = 0.0;
        double drq = 0.0;
        RectHV L = null;
        RectHV R = null;
        Point2D nearest = bestSoFar;

        if (nearest != null) {
            dqn = p.distanceSquaredTo(nearest);
            drq = rect.distanceSquaredTo(p);
        }

        if (nearest == null || dqn > drq) {
            if (nearest == null || dqn > p.distanceSquaredTo(n.p))
                nearest = n.p;

            if (n.isVertical) {
                L = new RectHV(rect.xmin(), rect.ymin(), n.p.x(), rect.ymax());
                R = new RectHV(n.p.x(), rect.ymin(), rect.xmax(), rect.ymax());

                if (p.x() < n.p.x()) {
                    nearest = nearest(n.lb, L, p, nearest);
                    nearest = nearest(n.rt, R, p, nearest);
                } else {
                    nearest = nearest(n.rt, R, p, nearest);
                    nearest = nearest(n.lb, L, p, nearest);
                }
            } else {
                L = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), n.p.y());
                R = new RectHV(rect.xmin(), n.p.y(), rect.xmax(), rect.ymax());

                if (p.y() < n.p.y()) {
                    nearest = nearest(n.lb, L, p, nearest);
                    nearest = nearest(n.rt, R, p, nearest);
                } else {
                    nearest = nearest(n.rt, R, p, nearest);
                    nearest = nearest(n.lb, L, p, nearest);
                }
            }
        }

        return nearest;
    }
}