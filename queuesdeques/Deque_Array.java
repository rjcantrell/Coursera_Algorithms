import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque_Array<Item> implements Iterable<Item> {
    private Item[] storage = (Item[]) new Object[2];
    private int size = 0;

    private class ArrayIterator<Item> implements Iterator<Item> {
        private Item[] toBeIterated;
        private int index = 0;

        public ArrayIterator(Item[] array) {
            toBeIterated = array;
        }

        public boolean hasNext() {
            return (index < toBeIterated.length);
        }

        public Item next() {
            if (index >= toBeIterated.length) { throw new NoSuchElementException("Array index: " + index); }
            Item retVal = toBeIterated[index];
            index++;
            return retVal;
        }

        public void remove() { throw new UnsupportedOperationException(); }
    }

    public Deque_Array() { }                       // construct an empty deque

    public boolean isEmpty() {                // is the deque empty?
        return (size == 0);
    }

    public int size() {                       // return the number of items on the deque
        return size;
    }

    public void addFirst(Item item) {        // insert the item at the front
        if (item == null) { throw new NullPointerException(); }
        if (size == storage.length) { resize(size*2); }
        size++;
        for (int i = size - 1; i > 0; i--) {
            storage[i] = storage[i - 1];
        }
        storage[0] = item;
    }

    public void addLast(Item item) {         // insert the item at the end
        if (item == null) { throw new NullPointerException(); }
        if (size == storage.length) { resize(size*2); }
        storage[size++] = item;
    }

    public Item removeFirst() {               // delete and return the item at the front
        if (isEmpty()) { throw new java.util.NoSuchElementException(); }
        Item retVal = storage[0];
        for (int i = 1; i < size; i++) {
            storage[i - 1] = storage[i];
        }
        size--;
        if (size == storage.length / 4) { resize(storage.length / 2); }
        return retVal;
    }

    public Item removeLast() {                 // delete and return the item at the end
        if (isEmpty()) { throw new java.util.NoSuchElementException(); }
        size--;
        Item retVal = storage[size];
        storage[size] = null;
        if (size == storage.length / 4) { resize(storage.length / 2); }
        return retVal;
    }

    public Iterator<Item> iterator() {        // return an iterator over items in order from front to end
        Item[] realEntries = (Item[]) new Object[size];
        System.arraycopy(storage, 0, realEntries, 0, size);
        return new ArrayIterator<Item>(realEntries);
    }

    public static void main(String[] args) {  // unit testing
        Deque_Array<Integer> testMe = new Deque_Array<Integer>();
        testMe.addLast(2);
        testMe.addLast(1);
        testMe.addFirst(3);
        StdOut.println(testMe.removeLast());
        StdOut.println(testMe.removeLast());
        StdOut.println(testMe.removeFirst());
        StdOut.println();
    }

    private void resize(int capacity)
    {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            copy[i] = storage[i];
        }
        storage = copy;
    }
}