package management;

import lombok.Getter;
import lombok.Setter;

import javax.management.*;

/**
 * Managed bean для получения информации о количестве точек
 */
public class CountBean implements CountMBean, NotificationBroadcaster {
    @Getter
    @Setter
    long resultsCount;
    @Getter
    @Setter
    long resultsHitCount;

    private final NotificationBroadcasterSupport broadcaster = new NotificationBroadcasterSupport();

    @Override
    public synchronized void registerNewPoint(boolean hit) {
        setResultsCount(resultsCount+1);
        if (hit)
            setResultsHitCount(resultsHitCount+1);

        if (!hit) {
            Notification n = new Notification(
                    "miss",
                    this,
                    System.currentTimeMillis(),
                    "Not in target!");

            broadcaster.sendNotification(n);
        }
    }

    @Override
    public void addNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback) throws IllegalArgumentException {
        broadcaster.addNotificationListener(listener, filter, handback);
    }

    @Override
    public void removeNotificationListener(NotificationListener listener) throws ListenerNotFoundException {
        broadcaster.removeNotificationListener(listener);
    }

    @Override
    public MBeanNotificationInfo[] getNotificationInfo() {
        String[] types = new String[] { "miss" };
        String name = Notification.class.getName();
        String description = "Notification sent when miss recorded";
        return new MBeanNotificationInfo[] { new MBeanNotificationInfo(types, name, description) };
    }
}
