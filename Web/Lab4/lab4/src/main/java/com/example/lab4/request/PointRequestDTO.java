package com.example.lab4.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class PointRequestDTO {
    @Getter
    @Setter
    private double r;
    @Getter
    @Setter
    private double x;
    @Getter
    @Setter
    private double y;

}
