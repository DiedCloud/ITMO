package model;

public class AreaChecker {
    public static boolean getResult(double x, double y, double r) {
        // check 1st square -- 1/4 triangle
        if (x >= 0 && y >= 0) {
            if (x + 2*y <= r)
                return true;
        }
        // check 2nd square -- square
        if (x <= 0 && y >= 0) {
            if (-x <= r && y <= r/2)
                return true;
        }
        // check 3rd square -- circle
        if (x <= 0 && y <= 0) {
            if (Math.pow(x, 2) + Math.pow(y, 2) <= Math.pow(r, 2))
                return true;
        }
        // check 4th square -- always false
        // also if all checks above fails...
        return false;
    }
}
