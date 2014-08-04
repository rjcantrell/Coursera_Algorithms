import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
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

    public Deque() {                        // construct an empty deque
        alpha = new Node();
        omega = new Node();
        alpha.next = omega;
        omega.prev = alpha;
    }

    public boolean isEmpty() {                // is the deque empty?
        return (size == 0);
    }

    public int size() {                       // return the number of items on the deque
        return size;
    }

    public void addFirst(Item item) {        // insert the item at the front
        if (item == null) { throw new NullPointerException(); }
        Node newFirst = new Node();
        Node oldFirst = alpha.next;
        newFirst.data = item;
        newFirst.prev = alpha;
        newFirst.next = oldFirst;
        alpha.next = newFirst;
        oldFirst.prev = newFirst;
        size++;
    }

    public void addLast(Item item) {         // insert the item at the end
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

    public Item removeFirst() {               // delete and return the item at the front
        if (isEmpty()) { throw new java.util.NoSuchElementException(); }
        Node oldFirst = alpha.next;
        Node newFirst = oldFirst.next;
        alpha.next = newFirst;
        newFirst.prev = alpha;
        size--;
        return oldFirst.data;
    }

    public Item removeLast() {                 // delete and return the item at the end
        if (isEmpty()) { throw new java.util.NoSuchElementException(); }
        Node oldLast = omega.prev;
        Node newLast = oldLast.prev;
        omega.prev = newLast;
        newLast.next = omega;
        size--;
        return oldLast.data;
    }

    public Iterator<Item> iterator() {        // return an iterator over items in order from front to end
        return new DLinkListIterator();
    }

    public static void main(String[] args) {  // unit testing
    }
}