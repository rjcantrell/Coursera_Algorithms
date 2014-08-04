import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueueTest {
    private RandomizedQueue<Integer> sut;
    private ArrayList<Integer> sutDequeues;
    private ArrayList<Integer> sutSamples;

    @Before
    public void setUp() throws Exception {
        sut = new RandomizedQueue<Integer>();
        sutDequeues = new ArrayList<Integer>();
        sutSamples = new ArrayList<Integer>();
    }

    @After
    public void tearDown() throws Exception {
        sut = null;
        sutDequeues = null;
        sutSamples = null;
    }

    @Test
    public void isEmpty_InitiallyEmptyReturnsTrue() throws Exception {
        Assert.assertTrue(sut.isEmpty());
    }

    @Test
    public void isEmpty_addReturnsFalse() throws Exception {
        sut.enqueue(1);
        Assert.assertFalse(sut.isEmpty());
    }

    @Test
    public void isEmpty_addAndRemoveReturnsTrue() throws Exception {
        sut.enqueue(1);
        sut.dequeue();
        Assert.assertTrue(sut.isEmpty());
    }

    @Test
    public void isEmpty_refilledReturnsFalse() throws Exception {
        sut.enqueue(1);
        sut.dequeue();
        sut.enqueue(1);
        Assert.assertFalse(sut.isEmpty());
    }

    @Test
    public void isEmpty_sampleDoesntChangeValue() throws Exception {
        sut.enqueue(1);
        Assert.assertFalse(sut.isEmpty());
        sut.sample();
        Assert.assertFalse(sut.isEmpty());
        sut.dequeue();
        Assert.assertTrue(sut.isEmpty());
    }


    @Test
    public void testSize_empty() throws Exception {
        Assert.assertEquals(0, sut.size());
    }

    @Test
    public void testSize_notEmpty() throws Exception {
        int howMany = 5;
        for (int i = 0; i < howMany; i++) { sut.enqueue(i); }
        Assert.assertEquals(howMany, sut.size());
    }

    @Test
    public void testEnqueue() throws Exception {
        sut.enqueue(100);
        Assert.assertEquals(1, (int) sut.size());
    }

    @Test
    public void testDequeue_returnValue() throws Exception {
        int value = 100;
        sut.enqueue(value);
        Assert.assertEquals(value, (int) sut.dequeue());
    }

    @Test
    public void testDequeue_doesClear() throws Exception {
        int value = 100;
        sut.enqueue(value);
        sut.dequeue();
        Assert.assertEquals(0, (int) sut.size());
    }

    @Test(expected=NoSuchElementException.class)
    public void testDequeue_throwsNoSuchWhenEmpty() throws Exception {
        sut.dequeue();
    }

    @Test(expected=NoSuchElementException.class)
    public void testSample_throwsNoSuchWhenEmpty() throws Exception {
        sut.sample();
    }

    @Test
    public void testSample_returnValue() throws Exception {
        int value = 100;
        sut.enqueue(value);
        Assert.assertEquals(value, (int) sut.sample());
    }

    @Test
    public void testSample_doesntClear() throws Exception {
        int value = 100;
        sut.enqueue(value);
        Assert.assertEquals(1, (int) sut.size());
    }

    @Test
    public void testIterator_correctCount() throws Exception {
        randomEnqueueDequeueSample(255, 1.0, 0.0, 0.0);
        Iterator<Integer> sutIterator = sut.iterator();

        int observed = 0;
        while (sutIterator.hasNext()) {
            sutIterator.next();
            observed++;
        }

        Assert.assertEquals((int) sut.size(), observed);
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
    public void testRandomness() throws Exception {
        int howManyVariables = 5;
        int howManyExecutions = 5000;
        int[] results = new int[howManyVariables];

        for (int i = 0; i < howManyVariables; i++) { sut.enqueue(i); }
        for (int i = 0; i < howManyExecutions; i++) {
            int value = sut.sample();
            results[value]++;
        }

         //let's assume each will be within +-5 percent of the mean...
        for (int result : results) { Assert.assertTrue((result >= StdStats.mean(results)*0.95) && (result <= StdStats.mean(results)*1.05)); }
    }

    @Test
    public void randomUntilYourEyesBleed() throws Exception {
        int[] sets = new int[] { 5, 50, 500, 1000 };
        double[][] weights = new double[][] { { 1.0, 0.0, 0.0 }
                                            , { 0.9, 0.1, 0.0 }
                                            , { 0.1, 0.9, 0.0 }
                                            , { 0.9, 0.0, 0.1 }
                                            , { 0.1, 0.0, 0.9 }
                                            , { 0.8, 0.1, 0.1 }
                                            , { 0.1, 0.1, 0.8 }
        };
        for (int reps : sets) {
            for (double[] set : weights) {
                StdOut.println(String.format("%d random calls (p1 = %.1f, p2 = %.1f, p3 = %.1f", reps, set[0], set[1], set[2]));
                randomEnqueueDequeueSample(reps, set[0], set[1], set[2]);
            }
        }
    }

    private void randomEnqueueDequeueSample(int howMany) {
        randomEnqueueDequeueSample(howMany, 0.33333, 0.33333, 0.33333);
    }

    private void randomEnqueueDequeueSample(int howMany, double enqWeight, double deqWeight, double sampWeight) {
        double totalWeight = enqWeight + deqWeight + sampWeight;
        double whatToDo = StdRandom.uniform();
        for (int i = 0; i < howMany; i++) {
            if (whatToDo <= enqWeight / totalWeight) {
                sut.enqueue(i);
            } else if (whatToDo <= (enqWeight + deqWeight) / totalWeight) {
                if (sut.size() == 0) { sut.enqueue(i); } //put something on so you don't fail when you remove it
                sutDequeues.add(sut.dequeue());
            } else {
                sut.sample();
            }
        }
    }
}
