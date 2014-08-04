public class BoggleSolver
{
    private TSTWithInternals<String> dictionary;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null) { throw new IllegalArgumentException("You need to supply a dictionary, or else all this is pointless!"); }
        this.dictionary = new TSTWithInternals<String>();
        for(String s : dictionary) { this.dictionary.put(s, s); }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null) { throw new IllegalArgumentException("Can't find anything on a non-existent board..."); }

        SET<String> retVal = new SET<String>(); //because sets don't allow dupes
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                String startLetter = getBoardLetter(board, i, j);
                TSTWithInternals.Node node = dictionary.get(dictionary.root, startLetter, 0); //root, obvs
                if (node != null) { wander(board, i, j, node, new boolean[board.rows()][board.cols()], retVal); }
            }
        }

        return retVal;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null) { throw new IllegalArgumentException("How can you possibly want the score of nothingness?"); }
        if (word.length() <= 2 || !dictionary.contains(word)) {	return 0; }

        switch (word.length()) {
            case 3:
            case 4:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 5;
            case 8:
            default:
                return 11;
        }
    }

    public static void main(String[] args)
    {
        if (args.length != 2) {
            StdOut.println("Invalid arguments. Usage: BoggleSolver [dictionary] [board]");
            return;
        }
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);

        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

    private String getBoardLetter(BoggleBoard board, int i, int j) {
        char letter = board.getLetter(i, j); //I guess I could have used board[i][j]...
        return ((letter == 'Q') ? "QU" : String.valueOf(letter));
    }

    private void wander(BoggleBoard board, int x, int y, TSTWithInternals.Node node, boolean[][] marked, SET<String> wordsSoFar) {
        String word = (String)node.val;
        if (word != null && word.length() >= 3) { wordsSoFar.add(word); }

        marked[x][y] = true;

        for (int i = Math.max(0, x - 1); i <= Math.min(x + 1, board.rows() - 1); i++) { //christ, this would have made bound checks earlier assignments easier
            for (int j = Math.max(0, y - 1); j <= Math.min(y + 1, board.cols() - 1); j++) {
                if (marked[i][j]) { continue; }
                String nextLetter = getBoardLetter(board, i, j);
                TSTWithInternals.Node nextNode = dictionary.get(node.mid, nextLetter, 0);
                if (nextNode != null) { wander(board, i, j, nextNode, marked, wordsSoFar); }
            }
        }

        marked[x][y] = false;
    }
}
