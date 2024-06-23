package management;

import javax.management.MXBean;

@MXBean
public interface CountMBean {
    long resultsCount = 0;
    long resultsHitCount = 0;
    public void registerNewPoint(boolean hit);
    public long getResultsCount();
    public long getResultsHitCount();
    public void setResultsCount(long resultsCount);
    public void setResultsHitCount(long resultsHitCount);
}
