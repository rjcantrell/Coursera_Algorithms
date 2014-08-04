public class Subset {
    public static void main(String[] args) { // unit testing
        if (args.length <= 0) return;
        int N = Integer.parseInt(args[0]);
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) { q.enqueue(StdIn.readString()); }
        for (int i = 0; i < N; i++) { StdOut.println(q.dequeue()); }
    }
}
