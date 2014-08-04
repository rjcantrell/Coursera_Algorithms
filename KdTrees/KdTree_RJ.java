public class KdTree_RJ {

    private abstract static class Node {
        protected Point2D p;      // the point
        protected RectHV rect;    // the axis-aligned rectangle corresponding to this node
        protected Node lb;        // the left/bottom subtree
        protected Node rt;        // the right/top subtree
        protected int N;          // size of the subtree (inclusive of this node)

        public Node(Point2D _p, int _n) {
            p = _p;
            N = _n;
        }

        public void insert(Point2D p) {
            if (this.p == p) { return; } //already exists
            if (smaller(p)) {
                if (lb == null) { lb = GetAppropriateSubNode(p); }
                else { lb.insert(p); }
            }
            else  {
                if (rt == null) { rt = GetAppropriateSubNode(p); }
                else { rt.insert(p); }
            }

            N = (lb != null ? lb.N : 0) + (rt != null ? rt.N : 0) + 1;

            RectHV leftRect = leftRect();
            RectHV rightRect = rightRect();
            rect = new RectHV(leftRect.xmin(), leftRect.ymin(), rightRect.xmax(), rightRect.ymax());
        }

        public void draw() {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(.01);
            StdDraw.point(p.x(), p.y());
            drawLine();
            if (lb != null) { lb.draw(); }
            if (rt != null) { rt.draw(); }
        }

        public abstract Node GetAppropriateSubNode(Point2D p);
        public abstract boolean smaller(Point2D p);
        public abstract RectHV leftRect();
        public abstract RectHV rightRect();
        public abstract void drawLine();
    }

    private class HNode extends Node {
        public HNode(Point2D _p, int _n) {
            super(_p, _n);
        }

        public HNode(Point2D _p, int _n, RectHV _rect) {
            this(_p, _n);
            rect = _rect;
        }

        public Node GetAppropriateSubNode(Point2D p) {
            return new VNode(p, 1);
        }

        @Override
        public boolean smaller(Point2D p) {
            return (p.x() < this.p.x());
        }

        @Override
        public RectHV leftRect() {
            RectHV retVal = new RectHV(0.0, 0.0, p.x(), 1.0);
            if (lb != null) {
                if (lb.rect == null) {
                    lb.rect = retVal;
                }
                retVal = lb.rect;
            }
            return retVal;
        }

        @Override
        public RectHV rightRect() {
            RectHV retVal = new RectHV(p.x(), 0.0, 1.0, 1.0);
            if (rt != null) {
                if (rt.rect == null) {
                    rt.rect = retVal;
                }
                retVal = rt.rect;
            }
            return retVal;
        }

        @Override
        public void drawLine() {
            StdDraw.setPenRadius();
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(p.x(), rect.ymin(), p.x(), rect.ymax());
        }
    }

    private class VNode extends Node {
        public VNode(Point2D _p, int _n) {
            super(_p, _n);
        }

        public Node GetAppropriateSubNode(Point2D p) {
            return new HNode(p, 1);
        }

        @Override
        public boolean smaller(Point2D p) {
            return (p.y() < this.p.y());
        }

        @Override
        public RectHV leftRect() {
            RectHV retVal = new RectHV(0.0, 0.0, 1.0, p.y());
            if (lb != null) {
                if (lb.rect == null) {
                    lb.rect = retVal;
                }
                retVal = lb.rect;
            }
            return retVal;
        }

        @Override
        public RectHV rightRect() {
            RectHV retVal = new RectHV(0.0, p.y(), 1.0, 1.0);
            if (rt != null) {
                if (rt.rect == null) {
                    rt.rect = retVal;
                }
                retVal = rt.rect;
            }
            return retVal;
        }

        @Override
        public void drawLine() {
            StdDraw.setPenRadius();
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(rect.xmin(), p.y(), rect.xmax(), p.y());
        }
    }

    private Node root;

    public KdTree_RJ() {                               // construct an empty set of points
    }

    public boolean isEmpty() {                       // is the set empty?
        return (root == null);
    }

    public int size() {                               // number of points in the set
        return root.N;
    }

    public void insert(Point2D p) {                   // add the point p to the set (if it is not already in the set)
        if (root == null) { root = new HNode(p, 1, new RectHV(0.0, 0.0, 1.0, 1.0)); }
        else { root.insert(p); }
    }

    public boolean contains(Point2D p)  {             // does the set contain the point p?
        return (get(p) != null);
    }

    public void draw() {                              // draw all of the points to standard draw
        StdDraw.setPenColor();
        StdDraw.rectangle(0.5, 0.5, 0.5, 0.5);
        root.draw();
    }

    public Iterable<Point2D> range(RectHV rect) {     // all points in the set that are inside the rectangle
        final Queue<Point2D> retVal = new Queue<Point2D>();
        range(root, rect, retVal, new RectHV(0, 0, 1, 1));
        return retVal;
    }

    public Point2D nearest(Point2D p) {              // a nearest neighbor in the set to p; null if set is empty
        //TODO: implement nearest
        return p;
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

    private Node get(Point2D p) {
        return get(root, p);
    }

    private Node get(Node n, Point2D p) {
        while (n != null) {
            int cmp = p.compareTo(n.p);
            if      (cmp < 0) n = n.lb;
            else if (cmp > 0) n = n.rt;
            else              return n;
        }
        return null;
    }

    private void range(Node n, RectHV rect, Queue<Point2D> queue, RectHV nodeRect) {
        if (n == null) { return; } //end of a branch -- nothing to do!
        if (rect.intersects(nodeRect)) {
            if (rect.contains(n.p)) { queue.enqueue(n.p); }
            range(n.lb, rect, queue, n.leftRect());
            range(n.rt, rect, queue, n.rightRect());
        }
    }

}