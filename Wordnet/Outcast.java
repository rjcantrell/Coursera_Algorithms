public class Outcast {
    private WordNet theNet;
    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        theNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int loserSoFar = -1;
        int maxDist = -1;

        for (int i = 0; i < nouns.length; i++) {
            int currDist = 0;
            for (int j = 0; j < nouns.length; j++) {
                if (j != i) { currDist += theNet.distance(nouns[i], nouns[j]); }
            }
            if (currDist > maxDist) {
                maxDist = currDist;
                loserSoFar = i;
            }
        }

        return nouns[loserSoFar];
    }

    // for unit testing of this class (such as the one below)
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
