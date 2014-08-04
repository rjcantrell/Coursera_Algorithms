public class CircularSuffixArray {
    private RJSuffixArrayX sax;

    public CircularSuffixArray(String s) {  // circular suffix array of s
        sax = new RJSuffixArrayX(s);
    }

    public int length() {                   // length of s
        //TODO: modify less() and sort(), per https://class.coursera.org/algs4partII-003/forum/thread?thread_id=280
        return sax.length();
    }

    public int index(int i) {               // returns index of ith sorted suffix
        //TODO: modify less() and sort(), per https://class.coursera.org/algs4partII-003/forum/thread?thread_id=280
        return sax.index(i);
    }
}