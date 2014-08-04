import java.util.ArrayList;
import java.util.Arrays;

public class Fast {
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

        Point[] sandbox = new Point[storage.length];
        System.arraycopy(storage, 0, sandbox, 0, storage.length);
        for (Point origin : storage) {
            Arrays.sort(sandbox, origin.SLOPE_ORDER);
            int start = 0;
            int end = 0;

            while (end < N) {
                start = end;
                while (end < N && origin.slopeTo(sandbox[start]) == origin.slopeTo(sandbox[end])) { end++; }
                int howMany = end - start;
                if (howMany >= 3) {
                    Point[] solution = new Point[howMany + 1];
                    solution[0] = origin;
                    System.arraycopy(sandbox, start, solution, 1, howMany);
                    Quick.sort(solution); //always sort solutions to more easily disambiguate and de-dupe
                    if (!hasBeenSeen(solution)) { markFoundSolution(solution); }
                }
            }
        }

        doOutput();
    }

    private static boolean hasBeenSeen(Point... test) {
        for (Point[] aSolution : foundSolutions) {
            if (Arrays.deepEquals(test, aSolution)) { return true; }
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