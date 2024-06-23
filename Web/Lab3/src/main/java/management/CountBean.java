package management;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
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
        resultsCount++;
        if (hit)
            resultsHitCount++;
    }
}
