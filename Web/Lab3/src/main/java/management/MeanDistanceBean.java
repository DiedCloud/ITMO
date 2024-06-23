package management;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.util.List;

/**
 * Managed bean для получения информации о среднем расстоянии между точками
 */
@Named
@ApplicationScoped
public class MeanDistanceBean implements MeanDistanceMBean {
    private double distance;

    /**
     * Предоставляет возможность задать массив точек, из которого будет считаться показатель
     * @param points - лист массивов [x, y, r]
     */
    public void calcDistance(List<double[]> points) {
        double res = 0;
        for (int i = 0; i < points.size() - 1; i++) {
            double x1 = points.get(i)[0] / points.get(i)[2];
            double y1 = points.get(i)[1] / points.get(i)[2];
            double x2 = points.get(i + 1)[0] / points.get(i + 1)[2];
            double y2 = points.get(i + 1)[1] / points.get(i + 1)[2];
            res += Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        }
        distance = res / (points.size() - 1);
    }

    @Override
    public void setDistance(double distance){
        this.distance = distance;
    }

    @Override
    public double getDistance() {
        return distance;
    }
}
