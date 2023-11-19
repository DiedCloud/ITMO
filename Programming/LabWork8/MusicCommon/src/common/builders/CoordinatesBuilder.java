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
            throw new IllegalArgumentException("FIELD_X_CANNOT_BE_NULL");
        if (x <= -129)
            throw new IllegalArgumentException("FIELD_X_MUST_BE_GREATER_THAN_MINUS_129");
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
            throw new IllegalArgumentException("FIELD_Y_CANNOT_BE_NULL");
        if(y.isNaN() || y.isInfinite())
            throw new IllegalArgumentException("FIELD_Y_IS_INTEGER");
        if (y <= -420)
            throw new IllegalArgumentException("FIELD_Y_MUST_BE_GREATER_THAN_MINUS_420");
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