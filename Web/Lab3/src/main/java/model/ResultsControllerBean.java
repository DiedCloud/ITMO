package model;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import management.CountBean;
import management.MeanDistanceBean;
import model.db.DAOFactory;
import model.entity.PointEntity;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
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
public class ResultsControllerBean implements Serializable, ServletContextListener {
    @Inject
    private RBean rBean;
    @Inject
    private XBean xBean;
    @Inject
    private YBean yBean;

    MeanDistanceBean mdb;
    CountBean cb;

    private ArrayList<PointEntity> results = new ArrayList<>();

    @PostConstruct
    public void contextInitialized(ServletContextEvent sce) {
        mdb = new MeanDistanceBean();
        cb = new CountBean();

        var resultsEntities = DAOFactory.getInstance().getResultDAO().getAllResults();
        results = new ArrayList<PointEntity>(resultsEntities);
        log.info("Results initialized with {} entries.", results.size());
        cb.setResultsCount(results.size());
        cb.setResultsHitCount(results.stream().filter(PointEntity::isResult).toList().size());
        mdb.calcDistance(results.stream().map(
                (PointEntity res) -> new double[] {res.getX(), res.getY(), res.getR()}
        ).toList());


        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

        ObjectName mBean1;
        try {
            mBean1 = new ObjectName("Agent:name=MeanDistanceAgent");
            if (!mbs.isRegistered(mBean1)) {
                mbs.registerMBean(mdb, mBean1);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        ObjectName mBean2;
        try {
            mBean2 = new ObjectName("Agent:name=CountAgent");
            if (!mbs.isRegistered(mBean2)) {
                mbs.registerMBean(cb, mBean2);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Предоставляет возможность получить лист с результатами (точками)
     * @return лист точек
     */
    @Named(value = "resultList")
    public ArrayList<PointEntity> getResults() {
        return results;
    }

    /**
     * Предоставляет возможность задать лист с результатами (точками)
     * @param results лист точек
     */
    public void setResults(ArrayList<PointEntity> results) {
        this.results = results;
    }

    /**
     * Обрабатывает новую точку и добавляет ее в коллекцию
     * @param r текущий радиус области
     * @param x абсцисса
     * @param y ордината
     */
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

        mdb.calcDistance(results.stream().map(
                (PointEntity res) -> new double[] {res.getX(), res.getY(), res.getR()}
        ).toList());
        cb.registerNewPoint(result);

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

