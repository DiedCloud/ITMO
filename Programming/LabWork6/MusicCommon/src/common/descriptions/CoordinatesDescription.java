package common.descriptions;

import common.builders.CoordinatesBuilder;
import common.Coordinates;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class CoordinatesDescription extends LoadDescription<Coordinates> implements Serializable {

    {
        CoordinatesBuilder coordinatesBuilder = new CoordinatesBuilder();
        this.builder = coordinatesBuilder;
        fields = new ArrayList<>(Arrays.asList(
                new LoadDescription<Long>("X",coordinatesBuilder::setX, null, Long.class),
                new LoadDescription<Float>("Y",coordinatesBuilder::setY, null, Float.class)));
    }

    public CoordinatesDescription(SerialFunction<Coordinates, Object> fieldSetter) {
        super("Coordinates of Music Band", fieldSetter, Coordinates.class);
    }

}
