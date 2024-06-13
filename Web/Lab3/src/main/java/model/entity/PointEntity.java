package model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класса-сущность, позволяющий связать объект результата (точки) в программе и в базе данных
 */
@Entity
@Table(name = "points", schema = "public") //, schema = "s367590"
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private long id;

    @Column(name = "r")
    @Getter
    @Setter
    private double r;

    @Column(name = "x")
    @Getter
    @Setter
    private double x;

    @Column(name = "y")
    @Getter
    @Setter
    private double y;

    @Column(name = "result")
    @Getter
    @Setter
    private boolean result;

    @Column(name = "\"calculatedAt\"")
    @Getter
    @Setter
    private LocalDateTime calculatedAt = LocalDateTime.now();

    @Column(name = "\"calculationTime\"")
    @Getter
    @Setter
    private long calculationTime;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PointEntity bean)) return false;
        return getId() == bean.getId() &&
                Double.compare(getX(), bean.getX()) == 0 &&
                Double.compare(getY(), bean.getY()) == 0 &&
                Double.compare(getR(), bean.getR()) == 0 &&
                isResult() == bean.isResult() &&
                getCalculationTime() == bean.getCalculationTime() &&
                Objects.equals(getCalculatedAt(), bean.getCalculatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getX(), getY(), getR(), isResult(),
                getCalculatedAt(), getCalculationTime());
    }

    @Override
    public String toString() {
        return "CheckAreaBean{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", r=" + r +
                ", result=" + result +
                ", calculatedAt=" + calculatedAt +
                ", calculationTime=" + calculationTime +
                '}';
    }
}
