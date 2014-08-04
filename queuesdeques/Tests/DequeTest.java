import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class DequeTest {
    private Deque sut;
    private java.util.ArrayDeque<Integer> ref;
    private List<Object> sutResults;
    private List<Object> refResults;

    @Before
    public void setUp() throws Exception {
        sut = new Deque<Integer>();
        ref = new java.util.ArrayDeque<Integer>();
        sutResults = new ArrayList<Object>();
        refResults = new ArrayList<Object>();
    }

    @After
    public void tearDown() throws Exception {
        sut = null;
        ref = null;
        sutResults = null;
        refResults = null;
    }

    @Test
    public void isEmpty_InitiallyEmptyReturnsTrue() throws Exception {
        Assert.assertTrue(sut.isEmpty());
    }

    @Test
    public void isEmpty_addFirstReturnsFalse() throws Exception {
        sut.addFirst(1);
        Assert.assertFalse(sut.isEmpty());
    }

    @Test
    public void isEmpty_addLastReturnsFalse() throws Exception {
        sut.addLast(1);
        Assert.assertFalse(sut.isEmpty());
    }

    @Test
    public void isEmpty_EmptiedViaFirstOnlyReturnsTrue() throws Exception {
        sut.addFirst(1);
        sut.addLast(1);
        sut.removeFirst();
        sut.removeFirst();
        Assert.assertTrue(sut.isEmpty());
    }

    @Test
    public void isEmpty_EmptiedViaLastOnlyReturnsTrue() throws Exception {
        sut.addFirst(1);
        sut.addLast(1);
        sut.removeLast();
        sut.removeLast();
        Assert.assertTrue(sut.isEmpty());
    }

    @Test
    public void isEmpty_FilledASecondTimeReturnsFalse() throws Exception {
        sut.addFirst(1);
        sut.removeLast();
        sut.addLast(1d);
        Assert.assertFalse(sut.isEmpty());
    }

    @Test
    public void testSize_WithAddsOnly() throws Exception {
        Assert.assertEquals(0, sut.size());
        int[] sizes = new int[] { 1, 10, 20, 50};
        for (int size : sizes) {
            for (int i = 0; i < size; i++) { sut.addFirst(i); }
            Assert.assertEquals(size, sut.size());
            while (!sut.isEmpty()) { sut.removeFirst(); }
        }
    }


    @Test
    public void testSize_WithAddsAndRandomRemovals() throws Exception {
        Assert.assertEquals(0, sut.size());
        int[] sizes = new int[] { 1, 10, 20, 50};
        for (int size : sizes) {
            int removals = 0;
            for (int i = 0; i < size; i++) {
                sut.addFirst(i);
                boolean shouldRemove = (StdRandom.uniform() <= .25);
                if (shouldRemove) {
                    sut.removeFirst();
                    removals++;
                }
            }
            Assert.assertEquals(size - removals, sut.size());
            while (!sut.isEmpty()) { sut.removeFirst(); }
        }
    }

    @Test
    public void testAddFirst() throws Exception {
        sut.addFirst(1);
        Assert.assertEquals(1, sut.size());
        Assert.assertEquals(1, sut.removeFirst());
    }

    @Test
    public void testAddLast() throws Exception {
        sut.addLast(1);
        Assert.assertEquals(1, sut.size());
        Assert.assertEquals(1, sut.removeFirst());
    }

    @Test
    public void testRemoveFirst() throws Exception {
        sut.addFirst(1);
        Assert.assertEquals(1, sut.size());
        Assert.assertEquals(1, sut.removeFirst());
    }

    @Test
    public void testRemoveLast() throws Exception {
        sut.addFirst(1);
        Assert.assertEquals(1, sut.size());
        Assert.assertEquals(1, sut.removeLast());
    }

    @Test
    public void testIterator() throws Exception {
        for (int i = 0; i < 100; i++) {
            sut.addFirst(i);
            ref.addFirst(i);
        }
        Iterator<Integer> sutIterator = sut.iterator();
        Iterator<Integer> refIterator = ref.iterator();
        while (sutIterator.hasNext()) {
            Assert.assertEquals(refIterator.next(), sutIterator.next());
        }
    }

    @Test(expected=NoSuchElementException.class)
    public void iteratorThrowsNoSuchWhenEmpty() {
        Iterator<Integer> sutIterator = sut.iterator();
        sutIterator.next();
    }

    @Test(expected=UnsupportedOperationException.class)
    public void iteratorThrowsUnsupportedUponRemove() {
        Iterator<Integer> sutIterator = sut.iterator();
        sutIterator.remove();
    }

    @Test
    public void testRandomFillThenFirstEmpty() throws Exception {
        testRandomFillAndEmptyAgainstJavaUtil(100, 0.5, 0.5, 1.0, 0.0);
    }

    @Test
    public void testRandomFillThenLastEmpty() throws Exception {
        testRandomFillAndEmptyAgainstJavaUtil(100, 0.5, 0.5, 0.0, 1.0);
    }

    @Test
    public void testFirstFillThenRandomEmpty() throws Exception {
        testRandomFillAndEmptyAgainstJavaUtil(100, 1.0, 0.0, 0.5, 0.5);
    }

    @Test
    public void testLastFillThenRandomEmpty() throws Exception {
        testRandomFillAndEmptyAgainstJavaUtil(100, 0.0, 1.0, 0.5, 0.5);
    }

    @Test
    public void testRandomAddsRemovesAgainstJavaUtil() {
        testRandomFillAndEmptyAgainstJavaUtil(100, 0.5, 0.5, 1.0, 0.0);
        testRandomFillAndEmptyAgainstJavaUtil(100, 0.25, 0.25, 0.25, 0.25);
    }

    @Test
    public void randomsUntilYourEyesBleed() {
        int[] sets = new int[] { 5, 50, 500, 1000};
        double[][] weights = new double[][] { { 0.5, 0.5, 0.0, 0.0 }
                                            , { 0.9, 0.0, 0.1, 0.0 }
                                            , { 0.1, 0.0, 0.9, 0.0 }
                                            , { 0.9, 0.0, 0.0, 0.1 }
                                            , { 0.1, 0.0, 0.0, 0.9 }
                                            , { 0.0, 0.9, 0.0, 0.1 }
                                            , { 0.0, 0.9, 0.1, 0.0 }
                                            , { 0.0, 0.1, 0.9, 0.0 }
                                            , { 0.4, 0.4, 0.1, 0.1 }
                                            , { 0.1, 0.1, 0.4, 0.4 }
        };
        for (int reps : sets) {
            for (double[] set : weights) {
                StdOut.println(String.format("%d random calls (p1 = %.1f, p2 = %.1f, p3 = %.1f, p4 = %.1f", reps, set[0], set[1], set[2], set[3]));
                doRandomOperations(reps, set[0], set[1], set[2], set[3]);
                Assert.assertArrayEquals(refResults.toArray(), sutResults.toArray());
                sutResults = new ArrayList<Object>();
                refResults = new ArrayList<Object>();
            }
        }
    }

    private void testRandomFillAndEmptyAgainstJavaUtil(int howMany, double addFirstWeight, double addLastWeight, double removeFirstWeight, double removeLastWeight) {
        fillTestDequeAndReference(howMany, addFirstWeight, addLastWeight);
        emptyTestDequeAndReference(removeFirstWeight, removeLastWeight);
        Assert.assertArrayEquals(refResults.toArray(), sutResults.toArray());
    }

    private void fillTestDequeAndReference(int howMany, double firstWeight, double lastWeight) {
        double totalWeight = (firstWeight + lastWeight);
        for (int i = 0; i < howMany; i++) {
            double addWhere = StdRandom.uniform();
            if (addWhere <= (firstWeight / totalWeight)) {
                sut.addFirst(i);
                ref.addFirst(i);
            } else {
                sut.addLast(i);
                ref.addLast(i);
            }
        }
    }

    private void emptyTestDequeAndReference(double firstWeight, double lastWeight) {
        double totalWeight = (firstWeight + lastWeight);

        while (!sut.isEmpty()) {
            double removeWhere = StdRandom.uniform();
            if (removeWhere <= (firstWeight / totalWeight)) {
                sutResults.add(sut.removeFirst());
                refResults.add(ref.removeFirst());
            } else {
                sutResults.add(sut.removeLast());
                refResults.add(ref.removeLast());
            }
        }
    }

    private void doRandomOperations(int howMany, double addFirstWeight, double addLastWeight, double removeFirstWeight, double removeLastWeight) {
        double totalWeight = addFirstWeight + addLastWeight + removeFirstWeight + removeLastWeight;
        for (int i = 0; i < howMany; i++) {
            double whatToDo = StdRandom.uniform();
            if (whatToDo <= (addFirstWeight / totalWeight)) {
                sut.addFirst(i);
                ref.addFirst(i);
            } else if (whatToDo <= (addFirstWeight + addLastWeight) / totalWeight) {
                sut.addLast(i);
                ref.addLast(i);
            } else if (whatToDo <= (addFirstWeight + addLastWeight + removeFirstWeight) / totalWeight) {
                sutResults.add(sut.removeFirst());
                refResults.add(ref.removeFirst());
            } else {
                sutResults.add(sut.removeLast());
                refResults.add(ref.removeLast());
            }
        }
    }
}
