package model;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.entity.PointEntity;

import java.util.ArrayList;

/**
 * Managed bean для получения информации о среднем расстоянии между точками
 */
@Named
@ApplicationScoped
public class MeanDistanceBean {
    @Inject
    private ResultsControllerBean resultsControllerBean;

    /**
     * Предоставляет возможность получить количество точек, попавших в область
     *
     * @return лист точек
     */
    @Named(value = "countHit")
    public double getResultsHitCount() {
        ArrayList<PointEntity> points = resultsControllerBean.getResults();
        double res = 0;
        for (int i = 0; i < points.size() - 1; i++) {
            double x1 = points.get(i).getX() / points.get(i).getR();
            double y1 = points.get(i).getY() / points.get(i).getR();
            double x2 = points.get(i + 1).getX() / points.get(i + 1).getR();
            double y2 = points.get(i + 1).getY() / points.get(i + 1).getR();
            res += Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        }
        return res / (points.size() - 1);
    }
}
