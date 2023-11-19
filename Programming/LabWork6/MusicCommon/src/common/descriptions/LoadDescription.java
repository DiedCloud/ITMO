package common.descriptions;

import common.builders.Buildable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class LoadDescription<T> implements Serializable {

    protected T value;
    protected String description;
    protected Class<T> type;
    protected Buildable<T> builder;
    protected SerialFunction<T, ?> fieldOfDescriptionSetter;
    protected ArrayList<LoadDescription<?>> fields = new ArrayList<>();

    public LoadDescription(String description, SerialFunction<T, ?> fieldSetter, Buildable<T> builder, Class<T> type) {
        this.description = description;
        this.fieldOfDescriptionSetter = fieldSetter;
        this.builder = builder;
        this.type = type;
    }

    public LoadDescription(String description, SerialFunction<T, ?> fieldSetter, Class<T> type) {
        this.description = description;
        this.fieldOfDescriptionSetter = fieldSetter;
        this.type = type;
    }


    public LoadDescription(Class<T> type){
        this(null, null, null, type);
    }

    public Function<T, ?> getFieldOfDescriptionSetter() {
        return fieldOfDescriptionSetter;
    }

    public Buildable<T> getBuilder() {
        return builder;
    }

    public String getDescription() {
        return description;
    }

    public Class<T> getType() {
        return type;
    }

    @SuppressWarnings("unchecked")
    public void setField(Object object){
        if(object.getClass() == type) {
            fieldOfDescriptionSetter.apply((T) object);
        }
        else
            throw new IllegalArgumentException("Wrong type of field");
    }
    public ArrayList<LoadDescription<?>> getFields() {
        return fields;
    }

    public void build() {
        value = builder.build();
    }

    public void setFieldsOfObject(ArrayList<LoadDescription<?>> fields) {
        this.fields = fields;
    }

    public LoadDescription<T> setValue(T value) {
        this.value = value;
        return this;
    }

    public T getValue() {
        return value;
    }

    public void setFieldOfDescriptionSetter(SerialFunction<T, ?> fieldOfDescriptionSetter) {
        this.fieldOfDescriptionSetter = fieldOfDescriptionSetter;
    }

    @Override
    public String toString() {
        return "LoadDescription{" +
                "value=" + value +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", builder=" + builder +
                ", fieldOfDescriptionSetter=" + fieldOfDescriptionSetter +
                ", fields=" + fields +
                '}';
    }
}