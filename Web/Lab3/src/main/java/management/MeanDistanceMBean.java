package management;

import model.entity.PointEntity;

import javax.management.MXBean;
import java.util.List;

@MXBean
public interface MeanDistanceMBean {
    double distance = 0;
    public void setDistance(double points); //проблема в PointEntity - MXDean такого не знает
    public double getDistance();
}
