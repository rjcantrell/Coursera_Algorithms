import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] storage = (Item[]) new Object[4];
    private int size = 0;

    private class RandomArrayIterator implements Iterator<Item> {
        private Item[] storageRandomized;
        private int index = 0;

        public RandomArrayIterator() {
            storageRandomized = shuffle(storage);
        }

        public boolean hasNext() { return (index < size); }
        public void remove() { throw new UnsupportedOperationException(); }
        public Item next() {
            if (size == 0 ) { throw new NoSuchElementException("Array is empty"); }
            if (index >= storageRandomized.length) { throw new NoSuchElementException("Array index: " + index); }
            Item retVal = storageRandomized[index];
            index++;
            return retVal;
        }
    }

    public RandomizedQueue()  { }            // construct an empty randomized queue

    public boolean isEmpty() {               // is the queue empty?
        return (size == 0);
    }

    public int size() {                      // return the number of items on the queue
        return size;
    }

    public void enqueue(Item item) {         // add the item
        if (item == null) { throw new NullPointerException(); }
        if (size == storage.length) { resize(size*2); }
        storage[size++] = item;
    }

    public Item dequeue() {                  // delete and return a random item
        if (size == 0 ) { throw new NoSuchElementException(); }
        size--;
        int randoCalrissian = StdRandom.uniform(size + 1);
        Item retVal = storage[randoCalrissian];
        storage[randoCalrissian] = storage[size];
        storage[size] = null;
        if (size == storage.length / 4) { resize(storage.length / 2); }
        return retVal;
    }

    public Item sample() {                   // return (but do not delete) a random item
        if (size == 0 ) { throw new NoSuchElementException(); }
        return storage[StdRandom.uniform(size)];
    }

    public Iterator<Item> iterator() {       // return an independent iterator over items in random order
        return new RandomArrayIterator();
    }

    public static void main(String[] args) { // unit testing
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        System.arraycopy(storage, 0, copy, 0, size);
        storage = copy;
    }

    private Item[] shuffle(Item[] input) {
        for (int i = 0; i < input.length; i++) {
            int monkeyTacos = StdRandom.uniform(i + 1);
            if (i == monkeyTacos) break;
            Item tmp = input[i];
            input[i] = input[monkeyTacos];
            input[monkeyTacos] = tmp;
        }
        return input;
    }
}