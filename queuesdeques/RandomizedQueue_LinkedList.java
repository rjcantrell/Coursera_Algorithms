import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue_LinkedList<Item> implements Iterable<Item> {
    private int size = 0;
    private Node alpha; //beginning of time and space
    private Node omega; //end of time and space

    private class Node {
        private Item data;
        private Node prev;
        private Node next;
    }

    private class DLinkListIterator implements Iterator<Item> {
        private Node current = alpha.next;
        private int index = 0;

        public boolean hasNext() { return index < size; }
        public void remove() { throw new UnsupportedOperationException(); }
        public Item next() {
            if (isEmpty()) { throw new NoSuchElementException(); }
            Item retVal = current.data;
            current = current.next;
            index++;
            return retVal;
        }

    }

    public RandomizedQueue_LinkedList()  {             // construct an empty randomized queue
        alpha = new Node();
        omega = new Node();
        alpha.next = omega;
        omega.prev = alpha;
    }

    public boolean isEmpty() {               // is the queue empty?
        return (size == 0);
    }

    public int size() {                      // return the number of items on the queue
        return size;
    }

    public void enqueue(Item item) {         // add the item
        if (item == null) { throw new NullPointerException(); }
        Node newLast = new Node();
        Node oldLast = omega.prev;
        newLast.data = item;
        newLast.prev = oldLast;
        newLast.next = omega;
        omega.prev = newLast;
        oldLast.next = newLast;
        size++;
    }

    public Item dequeue() {                  // delete and return a random item
        if (size == 0 ) { throw new NoSuchElementException(); }
        shuffle();
        if (isEmpty()) { throw new java.util.NoSuchElementException(); }
        Node oldFirst = alpha.next;
        Node newFirst = oldFirst.next;
        alpha.next = newFirst;
        newFirst.prev = alpha;
        size--;
        return oldFirst.data;
    }

    public Item sample() {                   // return (but do not delete) a random item
        if (size == 0 ) { throw new NoSuchElementException(); }
        shuffle();
        return alpha.next.data;
    }

    public Iterator<Item> iterator() {       // return an independent iterator over items in random order
        shuffle();
        return new DLinkListIterator();
    }

    public static void main(String[] args) { // unit testing
    }

    private void shuffle() {
        for(int i = 1; i < size; i++) {
            int randomItem = StdRandom.uniform(1, i + 1);
            swapNodes(getNodeAtPosition(i), getNodeAtPosition(randomItem));
        }
    }

    private Node getNodeAtPosition(int pos) {
        if (pos <= 0 || pos > size) { throw new NoSuchElementException(); }
        Node retVal = alpha;
        for (int i = 0; i < pos; i++) { retVal = retVal.next; }
        return retVal;
    }

    private void swapNodes(Node b, Node y) {
        if (b.prev == y.prev && b.next == y.next) return; //don't swap things with themselves, you silly goose
        Node a = b.prev;
        Node c = b.next;

        Node x = y.prev;
        Node z = y.next;

        y.prev = a;
        y.next = c;

        a.next = y;
        c.prev = y;

        b.prev = x;
        b.next = z;

        x.next = b;
        z.prev = b;
    }
}