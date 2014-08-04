import java.util.ArrayList;

public class Brute {
    private static Point[] storage;
    private static ArrayList<Point[]> foundSolutions;

    public static void main(String[] args) {
        In inputFile = new In(args[0]);
        int N = inputFile.readInt();
        storage = new Point[N];
        foundSolutions = new ArrayList<Point[]>();

        for (int i = 0; !inputFile.isEmpty(); i++) {
            int x = inputFile.readInt();
            int y = inputFile.readInt();
            storage[i] = new Point(x, y);
        }

        for (int a = 0; a < N; a++) {
            for (int b = a + 1; b < N; b++) {
                for (int c = b + 1; c < N; c++) {
                    //if (!isColinear(storage[a], storage[b], storage[c])) { break; } //why bother?
                    for (int d = c + 1; d < N; d++) {
                        Point[] test = new Point[] { storage[a], storage[b], storage[c], storage[d] };
                        Quick.sort(test);
                        if (isColinear(test) && !hasBeenSeen(test)) {
                            markFoundSolution(test);
                        }
                    }
                }
            }
        }

        doOutput();
    }

    private static boolean isColinear(Point... points) {
        Point compareTo = points[0];
        double sought = compareTo.slopeTo(points[1]);
        for (int i = 2; i < points.length; i++) {
            if (compareTo.slopeTo(points[i]) != sought) { return false; }
        }
        return true;
    }

    private static boolean hasBeenSeen(Point... test) {
        for (Point[] aSolution : foundSolutions) {
            if (aSolution == test) { return true; }
        }
        return false;
    }

    private static void markFoundSolution(Point... colinearPoints) {
        foundSolutions.add(colinearPoints);
    }

    private static void doOutput() {
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);

        for (Point p : storage) { p.draw(); }

        for (Point[] solution : foundSolutions) {
            solution[0].drawTo(solution[solution.length -1]);
            for (int i = 0; i < solution.length; i++) {
                StdOut.print(solution[i].toString());
                if (i < solution.length - 1) {
                    StdOut.print(" -> ");
                } else {
                    StdOut.println();
                }
            }
        }
    }
}
