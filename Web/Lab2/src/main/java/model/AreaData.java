package model;

import java.beans.JavaBean;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@JavaBean
public class AreaData implements Serializable {
    private final double x;
    private final double y;
    private final double r;
    private final boolean result;
    private final LocalDateTime time;
    private final long calculationTime;

    public AreaData(double r, double x, double y, boolean result, LocalDateTime time, long calculationTime) {
        this.r = r;
        this.x = x;
        this.y = y;
        this.result = result;
        this.time = time;
        this.calculationTime = calculationTime;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getR() {
        return r;
    }

    public boolean getResult() {
        return result;
    }

    public LocalDateTime getCalculatedAt() {
        return time;
    }

    public long getCalculationTime() {
        return calculationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AreaData)) return false;
        AreaData data = (AreaData) o;
        return Double.compare(getX(), data.getX()) == 0 &&
                Double.compare(getY(), data.getY()) == 0 &&
                Double.compare(getR(), data.getR()) == 0 &&
                getResult() == data.getResult() &&
                Objects.equals(getCalculatedAt(), data.getCalculatedAt()) &&
                getCalculationTime() == data.getCalculationTime();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY(), getR(), getResult(), getCalculatedAt(), getCalculationTime());
    }

    @Override
    public String toString() {
        return "AreaData{" +
                "x=" + x +
                ", y=" + y +
                ", r=" + r +
                ", result=" + result +
                ", calculatedAt=" + time +
                ", calculationTime=" + calculationTime +
                '}';
    }
}