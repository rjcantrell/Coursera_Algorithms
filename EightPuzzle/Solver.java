import java.util.Comparator;

public class Solver {
    private class SearchNode {
        private Board board;
        private short movesMade;
        private SearchNode prev;

        private SearchNode(Board b) {
            board = b;
            movesMade = 0;
            prev = null;
        }

        private SearchNode(Board b, short moves, SearchNode previous) {
            board = b;
            movesMade = moves;
            prev = previous;
        }
    }

    private class ManhattanMovesComparator implements Comparator<SearchNode> {

        @Override
        public int compare(SearchNode s1, SearchNode s2) {
            if (s1 == null || s2 == null) { throw new NullPointerException(); }
            int s1man = s1.board.manhattan();
            int s2man = s2.board.manhattan();
            if ((s1man + s1.movesMade) == (s2man + s2.movesMade)) { return Integer.compare(s1man, s2man); }
            else return Integer.compare(s1man + s1.movesMade, s2man + s2.movesMade);
        }
    }

    private SearchNode solution;

    public Solver(Board initial) {          // find a solution to the initial board (using the A* algorithm)
        //long now = System.currentTimeMillis();

        MinPQ<SearchNode> searchArea = new MinPQ<SearchNode>(new ManhattanMovesComparator());

        searchArea.insert(new SearchNode(initial));
        searchArea.insert(new SearchNode(initial.twin()));

        // Then, delete from the priority queue the search node with the minimum priority,
        // and insert onto the priority queue all neighboring search nodes (those that can be reached in one move from the dequeued search node).
        // Repeat this procedure until the search node dequeued corresponds to a goal board.
        SearchNode sn = searchArea.delMin();
        while (!sn.board.isGoal()) {
            Iterable<Board> bs = sn.board.neighbors();
            for (Board b : bs) {
                //StdOut.println("NQing board with manhattan: " +  b.manhattan());
                //StdOut.println(b);
                if (sn.prev != null && (b.equals(sn.prev.board))) { continue; }  //don't nq A if A is how we got to B
                searchArea.insert(new SearchNode(b, (short) (sn.movesMade + 1), sn));
            }
            sn = searchArea.delMin();
            //StdOut.println(String.format("DQing board with priority: %d, manhattan %d, moves %d",
            //                              (sn.board.manhattan() + sn.movesMade), sn.board.manhattan(), sn.movesMade));
            //StdOut.println(sn.board);
        }

        if (getStartingBoard(sn) == initial) { solution = sn; } //if it came from the twin, it's not a solution!
        //long later = System.currentTimeMillis();
        //StdOut.println("PQ size at solution time: " + searchArea.size());
        //StdOut.println(String.format("Time elapsed: %,5d.3 ms", (later - now)));
        //StdOut.println();
    }

    public boolean isSolvable() {           // is the initial board solvable?
        return (solution != null);
    }

    public int moves() {                    // min number of moves to solve initial board; -1 if no solution
        return isSolvable() ? solution.movesMade : -1;
    }

    public Iterable<Board> solution() {     // sequence of boards in a shortest solution; null if no solution
        Stack<Board> retVal = null;
        if (isSolvable()) {
            retVal = new Stack<Board>();
            SearchNode curr = solution;
            while (curr != null) {
                retVal.push(curr.board);
                curr = curr.prev;
            }
        }
        return retVal;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    private Board getStartingBoard(SearchNode s) {
        SearchNode x = s; //only to make the Style gods happy. ugh.
        while (x.prev != null) { x = x.prev; }
        return x.board;
    }
}