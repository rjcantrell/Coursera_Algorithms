public class PercolationStats {
    private double[] results;
    private int percSideLength;

    public PercolationStats(int N, int T)    // perform T independent computational experiments on an N-by-N grid
    {
        if (N <= 0 || T <= 0) { throw new IllegalArgumentException(); }

        results = new double[T];
        percSideLength = N;

        for (int i = 0; i < T; i++)
        {
            results[i] = willItPercolate(new Percolation(percSideLength));
        }
    }

    public double mean()                     // sample mean of percolation threshold
    {
        return StdStats.mean(results);
    }

    public double stddev()                   // sample standard deviation of percolation threshold
    {
        return StdStats.stddev(results);
    }

    public double confidenceLo()             // returns lower bound of the 95% confidence interval
    {
        return (mean() - ((1.96 * stddev()) / Math.sqrt(results.length)));
    }

    public double confidenceHi()             // returns upper bound of the 95% confidence interval
    {
        return (mean() + ((1.96 * stddev()) / Math.sqrt(results.length)));
    }

    public static void main(String[] args)   // test client, described below
    {
        PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        stats.outputTextResults();
    }

    private double willItPercolate(Percolation perc)
    {
        double openings = 0d;
        do {
            int row = StdRandom.uniform(1, percSideLength + 1);
            int col = StdRandom.uniform(1, percSideLength + 1);
            if (!perc.isOpen(row, col)) {
                perc.open(row, col);
                openings++;
            }
        } while (!perc.percolates());
        return (openings / (percSideLength * percSideLength));
    }

    private void outputTextResults()
    {
        StdOut.printf("%-23s = %f%n", "mean", mean());
        StdOut.printf("%-23s = %f%n", "stddev", stddev());
        StdOut.printf("%-23s = %f, %f%n", "95% confidence interval", confidenceLo(), confidenceHi());
    }
}