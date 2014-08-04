import java.util.Arrays;
import java.util.Iterator;

public class WordNet {

    private LinearProbingHashST<String, Node> storage;
    private LinearProbingHashST<Integer, String> synsetsST;
    private SAP hypernymSAP;
    private int size;

    private class Node {
        private Integer val;
        private Node next;

        public Node(int val) {
            this.val = val;
        }
    }

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        constructSynSets(synsets);
        constructSap(hypernyms);
    }

    // the set of nouns (no duplicates), returned as an Iterable
    public Iterable<String> nouns() {
        return storage.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return storage.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        checkNouns(nounA, nounB);
        Iterable<Integer> iterA = getIteratorAtNoun(nounA);
        Iterable<Integer> iterB = getIteratorAtNoun(nounB);

        return hypernymSAP.length(iterA, iterB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        checkNouns(nounA, nounB);
        Iterable<Integer> iterA = getIteratorAtNoun(nounA);
        Iterable<Integer> iterB = getIteratorAtNoun(nounB);

        return synsetsST.get(hypernymSAP.ancestor(iterA, iterB));
    }

    // for unit testing of this class
    public static void main(String[] args) {
        Stopwatch timer = new Stopwatch();
        WordNet wordnet = new WordNet(
                "./Algs4/src/Assignments/Wordnet/tests/synsets.txt",
                "./Algs4/src/Assignments/Wordnet/tests/hypernyms.txt"
        );

        StdOut.println("23 = distance(white_marlin, mileage): \t\t\t\t"
                + wordnet.distance("white_marlin", "mileage"));
        StdOut.println("33 = distance(Black_Plague, black_marlin): \t\t\t"
                + wordnet.distance("Black_Plague", "black_marlin"));
        StdOut.println("27 = distance(American_water_spaniel, histology): \t"
                + wordnet.distance("American_water_spaniel", "histology"));
        StdOut.println("29 = distance(Brown_Swiss, barrel_roll): \t\t\t"
                + wordnet.distance("Brown_Swiss", "barrel_roll"));

        StdOut.println("Searches took " + timer.elapsedTime() + " seconds.");
    }

    private void checkNouns(String... toBeChecked) {
        checkNouns(Arrays.asList(toBeChecked));
    }

    private void checkNouns(Iterable<String> toBeChecked) {
        for (String checkMe : toBeChecked) {
            if (!isNoun(checkMe)){
                throw new IllegalArgumentException(String.format("Input parameter '%s' not a noun", checkMe));
            }
        }
    }

    private Iterable<Integer> getIteratorAtNoun(final String noun) {
        return new Iterable<Integer>() {
            public Iterator<Integer> iterator() {
                return new Iterator<Integer>() {
                    Node curr = storage.get(noun);

                    public boolean hasNext() {
                        return curr != null;
                    }

                    public Integer next() {
                        Integer val = curr.val;
                        curr = curr.next;
                        return val;
                    }

                    public void remove() {
                        throw new UnsupportedOperationException("Pretty sure we don't have to support 'remove' in this assignment....");
                    }
                };
            }
        };
    }

    private void constructSap(String hypernyms) {
        Digraph graph = new Digraph(size);
        In hyp = new In(hypernyms);

        boolean[] checkRoot = new boolean[size];

        int potentialRoots = 0;

        while (hyp.hasNextLine()) {
            String line = hyp.readLine();
            String[] parts = line.split(",");
            Integer origin = Integer.parseInt(parts[0]);

            if (parts.length == 1)
                potentialRoots++;

            checkRoot[origin] = true;

            for (int i = 1; i < parts.length; i++) {
                graph.addEdge(origin, Integer.parseInt(parts[i]));
            }
        }
        hyp.close();

        for (boolean checkMe : checkRoot) {
            if (!checkMe) { potentialRoots++; }
        }

        if (potentialRoots != 1) { throw new IllegalArgumentException("input graph does not seem to have a single root!"); }
        if (new DirectedCycle(graph).hasCycle()) { throw new IllegalArgumentException("input graph was found to contain a cycle!"); }

        hypernymSAP = new SAP(graph);
    }

    private void constructSynSets(String synsets) {
        storage = new LinearProbingHashST<String, Node>();
        synsetsST = new LinearProbingHashST<Integer, String>();
        In syn = new In(synsets);
        while (syn.hasNextLine()) {
            size++;
            String line = syn.readLine();
            String[] parts = line.split(",");
            String[] nouns = parts[1].split(" ");
            for (String noun : nouns) {
                if (storage.contains(noun)) {
                    Node tmp = storage.get(noun);
                    Node first = tmp;
                    while (tmp.next != null) {
                        tmp = tmp.next;
                    }
                    tmp.next = new Node(Integer.parseInt(parts[0]));

                    storage.put(noun, first);
                } else {
                    storage.put(noun, new Node(Integer.parseInt(parts[0])));
                }

                synsetsST.put(Integer.parseInt(parts[0]), parts[1]);
            }
        }
        syn.close();
    }
}
