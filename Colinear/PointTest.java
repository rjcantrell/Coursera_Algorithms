import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class PointTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void SlopeOrderCompare() throws Exception {
        /*
         sign of compare(), where p, q, and r have coordinates in [0, 10)
         Failed on trial 74 of 100000
         p                         = (4, 4)
         q                         = (5, 4)
         r                         = (3, 4)
         student   p.compare(q, r) = 1
         reference p.compare(q, r) = 0
         reference p.slopeTo(q)    = 0.0
         reference p.slopeTo(r)    = 0.0
         */
        Point p = new Point(4, 4);
        Point q = new Point(5, 4);
        Point r = new Point(3, 4);

        double a1 = p.SLOPE_ORDER.compare(q, r);
        double a2 = p.slopeTo(q);
        double a3 = p.slopeTo(r);

    }
}
