/**

 The CoordinatesBuilder class is designed to create a Coordinates object with specified coordinates.
 */
package common.builders;
import common.Coordinates;

import java.io.Serializable;

public class CoordinatesBuilder implements Buildable<Coordinates>, Serializable {
    private Long x; // The x coordinate
    private Float y; // The y coordinate

    /**
     * Sets the value of the x coordinate
     * @param x - the value of the x coordinate
     * @return CoordinatesBuilder
     */
    public CoordinatesBuilder setX(Long x) {
        if(x == null)
            throw new IllegalArgumentException("The value of the x coordinate cannot be null");
        if (x <= -129)
            throw new IllegalArgumentException("The value of the x coordinate must be greater than -129");
        this.x = x;
        return this;
    }

    /**
     * Sets the value of the y coordinate
     * @param y - the value of the y coordinate
     * @return CoordinatesBuilder
     */
    public CoordinatesBuilder setY(Float y) {
        if(y == null)
            throw new IllegalArgumentException("The value of the y coordinate cannot be null");
        if(y.isNaN() || y.isInfinite())
            throw new IllegalArgumentException("The value of the y coordinate is integer between -420 and " + Float.MAX_VALUE);
        if (y <= -420)
            throw new IllegalArgumentException("The value of the y coordinate must be greater than -420");
        this.y = y;
        return this;
    }

    /**
     * Creates a Coordinates object with the specified coordinates
     * @return Coordinates
     */
    @Override
    public Coordinates build(){
        return new Coordinates(x, y);
    }
}