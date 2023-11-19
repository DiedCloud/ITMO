package common.descriptions;

import common.LocalizationKeys;
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
                new LoadDescription<Long>(LocalizationKeys.X, LocalizationKeys.X_CORD, coordinatesBuilder::setX, null, Long.class),
                new LoadDescription<Float>(LocalizationKeys.Y, LocalizationKeys.Y_CORD, coordinatesBuilder::setY, null, Float.class)));
    }

    public CoordinatesDescription(SerialFunction<Coordinates, Object> fieldSetter) {
        super(LocalizationKeys.COORDINATES_OF_MUSICBAND, fieldSetter, Coordinates.class);
    }

}
