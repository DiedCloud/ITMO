package management;

import lombok.Getter;
import lombok.Setter;

/**
 * Managed bean для получения информации о количестве точек
 */
public class CountBean implements CountMBean {
    @Getter
    @Setter
    long resultsCount;
    @Getter
    @Setter
    long resultsHitCount;

    @Override
    public void registerNewPoint(boolean hit) {
        setResultsCount(resultsCount+1);
        if (hit)
            setResultsHitCount(resultsHitCount+1);
    }
}
