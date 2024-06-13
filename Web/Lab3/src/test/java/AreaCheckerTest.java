import model.AreaChecker;

import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.math.*;

@RunWith(Parameterized.class)
public class AreaCheckerTest {
    private final double x;
    private final double y;
    private final double r;
    private final boolean res;

    public AreaCheckerTest(double x, double y, double r, boolean res) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.res = res;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {1, 0, 1, true},
                {0.5, 0.25, 1, true},
                {1, 0.5, 1, false},
                {0.5, 1, 1, false},
                {0, 0.5, 1, true},

                {-2, 1, 2, true},
                {-0.5, 0.25, 1, true},
                {-0.5, 0.75, 1, false},
                {-1, 0, 1, true},

                {-Math.sqrt(2), -Math.sqrt(2), 2, true},
                {-1, -1, 1, false},
                {0, -1, 1, true},

                {0.25, -0.25, 1, false}
        });
    }

    @Test
    public void test(){
        Assert.assertEquals(AreaChecker.getResult(x, y, r), res);
    }
}
