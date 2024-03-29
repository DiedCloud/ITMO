package model;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.db.DAOFactory;
import model.entity.PointEntity;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Managed bean for handling results in JSF application.
 * This bean is responsible for managing operations related to result entities.
 */
@Data
@Slf4j
@Named
@ApplicationScoped
public class ResultsControllerBean implements Serializable {
    @Inject
    private RBean rBean;
    @Inject
    private XBean xBean;
    @Inject
    private YBean yBean;

    private ArrayList<PointEntity> results = new ArrayList<>();

    @PostConstruct
    public void init() {
        var resultsEntities = DAOFactory.getInstance().getResultDAO().getAllResults();
        results = new ArrayList<PointEntity>(resultsEntities);
        log.info("Results initialized with {} entries.", results.size());
    }

    @Named(value = "resultList")
    public ArrayList<PointEntity> getResults() {
        return results;
    }

    public void setResults(ArrayList<PointEntity> results) {
        this.results = results;
    }

    public void addResult(Double r, Double x, Double y) {
        PointEntity entity = new PointEntity();

        final long startExec = System.nanoTime();
        final boolean result = AreaChecker.getResult(x, y, r);
        final long endExec = System.nanoTime();
        final long executionTime = endExec - startExec;
        entity.setX(x);
        entity.setY(y);
        entity.setR(r);
        entity.setResult(result);
        entity.setCalculatedAt(LocalDateTime.now());
        entity.setCalculationTime(executionTime);

        results.add(entity);
        // add to db
        DAOFactory.getInstance().getResultDAO().addNewResult(entity);
        log.info("Added new result to the db: X={}", x);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResultsControllerBean)) return false;
        ResultsControllerBean that = (ResultsControllerBean) o;
        return Objects.equals(rBean, that.rBean) &&
                Objects.equals(xBean, that.xBean) &&
                Objects.equals(yBean, that.yBean) &&
                Objects.equals(getResults(), that.getResults());
    }

    @Override
    public int hashCode() {
        return Objects.hash(rBean, xBean, yBean, getResults());
    }

    @Override
    public String toString() {
        return "CheckAreaResultsBean{" +
                "selectRBean=" + rBean +
                ", selectXBean=" + xBean +
                ", selectYBean=" + yBean +
                ", results=" + results +
                '}';
    }
}

