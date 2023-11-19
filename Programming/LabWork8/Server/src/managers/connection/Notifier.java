package managers.connection;

import result.UpdateWarning;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Notifier {
    private static Notifier notifier;

    public static Notifier getInstance() {
        if (notifier == null) {
            notifier = new Notifier();
        }
        return notifier;
    }

    private List<ResultSender> observers = new CopyOnWriteArrayList<>();

    public void addObserver(ResultSender observer) {
        observers.add(observer);
    }

    public void warnAll(UpdateWarning updateWarning) {
        observers.forEach(o ->
                {
                    try {
                        o.addSending(() -> {
                            o.send(updateWarning);
                        });
                    } catch (Exception e) {
                        observers.remove(o);
                    }
                    ;
                }
        );
    }

}
