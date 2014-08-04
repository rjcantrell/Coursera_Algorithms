public class Board {
    private byte[][] tiles;
    private short manhattan = -1;

    public Board(int[][] blocks) {         // construct a board from an N-by-N array of blocks (where blocks[i][j] = block in row i, column j)
        tiles = cloneArray(blocks);
    }

    private Board(byte[][] blocks) {
        tiles = cloneArray(blocks);
    }

    public int dimension() {                 // board dimension N
        return tiles.length; //assumes it's always square, but...
    }

    public int hamming() {                   // number of blocks out of place
        int retVal = 0;
        int N = tiles.length;

        for (int i = 0; i < tiles.length; i++) {            // basically, ask "is this the goal value?" just like isGoal
            for (int j = 0; j < tiles[i].length; j++) {     // and count the number of times that answer is false.
                if ((tiles[i][j] != 0) && (tiles[i][j] != ((i * N) + j + 1))) { retVal++; } //skip the empty
            }
        }

        return retVal;
    }

    public int manhattan() {                 // sum of Manhattan distances between blocks and goal
        if (manhattan != -1) { return manhattan; }
        int retVal = 0;

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                retVal += getManhattanDistance(i , j);
            }
        }

        manhattan = (short) retVal;
        return retVal;
    }

    public boolean isGoal() {                   // is this board the goal board? (e.g., (1,2,3),(4,5,6),(7,8,0)
        int N = tiles.length;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {       // check all tiles unless it's the very very last one
                if (tiles[i][j] != ((i * N) + j + 1) && !((i == (N - 1)) && (j == (N - 1)))) { return false; }
            }
        }
        if (tiles[N - 1][N - 1] != 0) { return false; }
        return true;
    }

    public Board twin() {                    // a board obtained by exchanging two adjacent blocks in the same row
        int N = tiles.length;
        byte[][] twinTiles = new byte[N][N];

        for (int i = 0; i < N; i++) {
            if (tiles[i][0] != 0 && tiles[i][1] != 0) { //with only one empty, you can always swap the first two on at least one row
                twinTiles = getSwappedTileSet(i, 0, i, 1);
                break;
            }
        }

        return new Board(twinTiles);
    }

    public boolean equals(Object y) {        // does this board equal y?
        if (y == this) { return true; }
        if (y == null) { return false; }
        if (y.getClass() != this.getClass()) { return false; }
        Board that = (Board) y;
        if (this.tiles.length != that.tiles.length) { return false; }
        for (int i = 0; i < this.tiles.length; i++) {
            for (int j = 0; j < this.tiles[i].length; j++) {
                if (this.tiles[i][j] != that.tiles[i][j]) { return false; }
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {     // all neighboring boards
        int N = tiles.length;
        Queue<Board> retVal = new Queue<Board>();

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if ((j < N - 1) && (tiles[i][j + 1] == 0)) {                        //empty is to your right (EAST)
                    retVal.enqueue((new Board(getSwappedTileSet(i, j, i, j + 1))));
                }

                if ((j > 0) && (tiles[i][j - 1] == 0)) {                            //empty is to your left (WEST)
                    retVal.enqueue(new Board(getSwappedTileSet(i, j, i, j - 1)));
                }

                if ((i > 0) && (tiles[i - 1][j] == 0)) {                            //empty is above you (NORTH)
                    retVal.enqueue(new Board(getSwappedTileSet(i, j, i - 1 , j)));
                }

                if ((i < N - 1) && (tiles[i + 1][j] == 0)) {                        //empty is below you (SOUTH)
                    retVal.enqueue(new Board(getSwappedTileSet(i, j, i + 1, j)));
                }
            }
        }

        return retVal;
    }

    public String toString() {               // string representation of the board (in the output format specified below)
        StringBuilder s = new StringBuilder();
        s.append(tiles.length + "\n");
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                s.append(String.format("%2d ", (int) (tiles[i][j] & 0xFF)));
            }
            s.append("\n");
        }
        return s.toString();
    }

    private static byte[][] cloneArray(byte[][] src) {
        int length = src.length;
        byte[][] target = new byte[length][src[0].length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(src[i], 0, target[i], 0, src[i].length);
        }
        return target;
    }

    private byte[][] cloneArray(int[][] src) {
        int length = src.length;
        byte[][] target = new byte[length][src[0].length];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < src[i].length; j++) {
                target[i][j] = (byte) src[i][j];
            }
        }
        return target;
    }


    private byte[][] getSwappedTileSet(int i1, int j1, int i2, int j2) {
        byte[][] retVal = cloneArray(tiles);
        retVal[i1][j1] = tiles[i2][j2];
        retVal[i2][j2] = tiles[i1][j1];
        return retVal;
    }

    private int getManhattanDistance(int row, int col) {
        int retVal = 0;
        int actualValue = tiles[row][col];
        if (actualValue != 0) {
            int targetRow = (actualValue - 1) / tiles.length;
            int targetCol = (actualValue - 1) % tiles.length;
            retVal = Math.abs(row - targetRow) + Math.abs(col - targetCol);
        }
        return retVal;
    }
}