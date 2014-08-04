public class Percolation {
    private WeightedQuickUnionUF connections;
    private boolean[] openings;
    private int sideLength;

    public Percolation(int N) {              // create N-by-N grid, with all sites blocked
        sideLength = N;
        int length = N*N + 2;

        openings = new boolean[length];
        connections = new WeightedQuickUnionUF(length); //0 is "hidden" start, final is "hidden" end
        for (int i = 1; i <= N; i++) {
            connections.union(0, i);                         //connect "start" to entire first row
            connections.union(length - 1, length - 1 - i);   //connect "end" to entire last row
        }
    }

    public void open(int i, int j) {         // open site (row i, column j) if it is not already
        if (isOpen(i, j)) return; //don't bother if it's already open...

        openings[getIndex(i, j)] = true;
        if ((i != 1) && (isOpen(i - 1, j))) { connect(i, j, i - 1, j); }            // connect above (row - 1)
        if ((i < sideLength) && (isOpen(i + 1, j))) { connect(i, j, i + 1, j); }    // connect below (row + 1)
        if ((j != 1) && (isOpen(i, j - 1))) { connect(i, j, i, j - 1); }            // connect left  (col - 1)
        if ((j < sideLength) &&  (isOpen(i, j + 1))) { connect(i, j, i, j + 1); }   // connect right (col + 1)
    }

    public boolean isOpen(int i, int j) {    // is site (row i, column j) open?
        return (openings[getIndex(i, j)]);
    }

    public boolean isFull(int i, int j) {    // is site (row i, column j) full?
        return (isOpen(i, j) && connections.connected(0, getIndex(i, j))); //doesn't stop backwash...
    }

    public boolean percolates() {          // does the system percolate?
        if (sideLength == 1) { return isOpen(1, 1); }
        return connections.connected(0, sideLength * sideLength + 1);  //are the "start" and "end" connected in any way?
    }

    private int getIndex(int row, int col) {
        if (row < 1 || row > sideLength) { throw new IndexOutOfBoundsException("Invalid row: " + row); }
        if (col < 1 || col > sideLength) { throw new IndexOutOfBoundsException("Invalid col: " + col); }
        return (((row - 1) * sideLength) + col);
    }

    private void connect(int i1, int j1, int i2, int j2) {
        connections.union(getIndex(i1, j1), getIndex(i2, j2));
    }
}
