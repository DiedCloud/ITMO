package common.builders;

import java.io.Serializable;

public interface Buildable<T> extends Serializable {
    T build();
}
