package model;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.entity.PointEntity;

/**
 * Managed bean для получения информации о количестве точек
 */
@Named
@ApplicationScoped
public class CountBean {
    @Inject
    private ResultsControllerBean resultsControllerBean;

    /**
     * Предоставляет возможность получить количество точек
     *
     * @return лист точек
     */
    @Named(value = "countHit")
    public long getResultsCount() {
        return resultsControllerBean.getResults().size();
    }

    /**
     * Предоставляет возможность получить количество точек, попавших в область
     *
     * @return лист точек
     */
    @Named(value = "countHit")
    public long getResultsHitCount() {
        return resultsControllerBean.getResults().
                stream().
                filter(PointEntity::isResult).
                count();
    }
}
