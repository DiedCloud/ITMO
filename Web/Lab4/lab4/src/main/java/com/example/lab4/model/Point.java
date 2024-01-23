package com.example.lab4.model;

import com.example.lab4.entity.PointEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@EqualsAndHashCode
public class Point {
    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private double r;

    @Getter
    @Setter
    private double x;

    @Getter
    @Setter
    private double y;

    @Getter
    @Setter
    private boolean result;

    @Getter
    @Setter
    private LocalDateTime calculatedAt = LocalDateTime.now();

    @Getter
    @Setter
    private long calculationTime;

    public static Point toModel(PointEntity entity) {
        Point model = new Point();
        model.setId(entity.getId());
        model.setR(entity.getR());
        model.setX(entity.getX());
        model.setY(entity.getY());
        model.setResult(entity.isResult());
        model.setCalculatedAt(entity.getCalculatedAt());
        model.setCalculationTime(entity.getCalculationTime());
        return model;
    }
}
