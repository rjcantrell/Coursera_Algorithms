public class BurrowsWheeler {
    private static final int R = 256; // Radix of a byte.

    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() {
        String s = BinaryStdIn.readString();
        int n = s.length();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        int first;
        for (first = 0; first < csa.length(); first++)
            if (csa.index(first) == 0) { break; }
        BinaryStdOut.write(first);
        for (int i = 0; i < csa.length(); i++)
            BinaryStdOut.write(s.charAt((csa.index(i) + n - 1) % n));
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {
        int first = BinaryStdIn.readInt();
        String t = BinaryStdIn.readString();
        int n = t.length();
        int[] count = new int[R + 1], next = new int[n];
        for (int i = 0; i < n; i++) { count[t.charAt(i) + 1]++; }
        for (int i = 1; i < R + 1; i++) { count[i] += count[i - 1]; }
        for (int i = 0; i < n; i++) { next[count[t.charAt(i)]++] = i; }
        for (int i = next[first], c = 0; c < n; i = next[i], c++) { BinaryStdOut.write(t.charAt(i)); }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
        if (args.length != 1) { throw new IllegalArgumentException("Usage: BurrowsWheeler [ + | - ] < (binary input file)\n"); }
        else if (args[0].equals("+")) { decode(); }
        else if (args[0].equals("-")) { encode(); }
        else {
            String msg = "Unknown argument: " + args[0] + "\n";
            throw new IllegalArgumentException(msg);
        }
    }
}