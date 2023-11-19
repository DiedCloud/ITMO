package common;

import java.io.Serializable;

/**

 The Coordinates class represents coordinates on a plane with x and y values in Long and Float format respectively.
 */
public class Coordinates implements Serializable {

    /**

     The epsilon constant is used in the method that compares objects of type Coordinates with an error margin.
     */
    static final float epsilon = 0.01f;
    /**

     The x and y fields contain coordinate values.
     */
    private Long x;
    private Float y;
    /**

     The constructor takes x and y values and creates an object of type Coordinates.
     @param x the value of the x coordinate in Long format
     @param y the value of the y coordinate in Float format
     @throws IllegalArgumentException if x or y are null, or if x <= -129 or y <= -420
     */
    public Coordinates(Long x, Float y) throws IllegalArgumentException {
        this.x = x;
        this.y = y;
    }
    /**

     The method returns the value of the x coordinate of an object of type Coordinates.
     @return the value of the x coordinate in Long format
     */
    public Long getX() {
        return x;
    }
    /**

     The method returns the value of the y coordinate of an object of type Coordinates.
     @return the value of the y coordinate in Float format
     */
    public Float getY() {
        return y;
    }
    /**

     The method sets the value of the x coordinate of an object of type Coordinates.
     @param x the value of the x coordinate in Long format
     @throws IllegalArgumentException if x is null or x <= -129
     */
    public void setX(Long x) throws IllegalArgumentException {
        if (x == null) {
            throw new IllegalArgumentException("FIELD_X_CANNOT_BE_NULL");
        }
        if (x <= -129) {
            throw new IllegalArgumentException("FIELD_X_MUST_BE_GREATER_THAN_MINUS129");
        }
        this.x = x;
    }
    /**

     The method sets the value of the y coordinate of an object of type Coordinates.
     @param y the value of the y coordinate in Float format
     @throws IllegalArgumentException if y is null or y <= -420
     */
    public void setY(Float y) throws IllegalArgumentException {
        if (y == null) {
            throw new IllegalArgumentException("FIELD_Y_CANNOT_BE_NULL");
        }
        if (y <= -420) {
            throw new IllegalArgumentException("FIELD_Y_MUST_BE_GREATER_THAN_MINUS_420");
        }
        this.y = y;
    }
    /**

     The method overrides the toString() method and returns a string representation of an object of type Coordinates.
     @return a string representation of an object of type Coordinates
     */
    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x + ", y=" + y +
                '}';
    }
    /**

     Compares the coordinates with other coordinates. First, the x fields are compared, then the y fields.
     @param coordinates the coordinates to compare with the current coordinates
     @return 0 if the coordinates are equal; less than 0 if the current coordinates are less than the parameters; greater than 0 if the current coordinates are greater than the parameters
     */
    public int compareTo(Coordinates coordinates) {
        int result = Long.compare(this.x, coordinates.x);
        if (result != 0) {
            return result;
        }

        if (Math.abs(this.y - coordinates.y) < epsilon) {
            return 0;
        } else if (this.y < coordinates.y) {
            return -1;
        } else {
            return 1;
        }

    }
}