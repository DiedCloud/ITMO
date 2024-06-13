import model.AreaChecker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class AreaCheckerTest {
    @Test
    public void controllerNotHit() {
        boolean result = AreaChecker.getResult(1, 1, 1);
        System.out.println("Hit Test: " + result);
        Assertions.assertFalse(result);
    }
    @Test
    public void controllerHit() {
        boolean result = AreaChecker.getResult(-1.7, 1.2, 3);
        System.out.println("Hit Test: " + result);
        Assertions.assertTrue(result);
    }
}
