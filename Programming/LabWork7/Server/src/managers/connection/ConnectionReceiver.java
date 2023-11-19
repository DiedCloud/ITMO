package managers.connection;

import common.descriptions.CommandDescription;
import main.Main;
import managers.Invoker;

import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionReceiver {
    private int port;
    private final static ExecutorService requestsPool = Executors.newCachedThreadPool();

    public void run(Invoker invoker){
        Main.logger.info("Listening started");
        while (true) {
            try {
                ReentrantLock lock = new ReentrantLock();
                lock.lock();
                DatagramManager dm = new DatagramManager();
                CommandDescription cd = dm.receive();
                addRequest(() -> {
                    InputController inputController = new InputController(dm);
                    inputController.addParsing(() -> inputController.parse(cd, invoker));
                });
                lock.unlock();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void addRequest(Runnable task){
        requestsPool.submit(task);
    }

    public static void shutdownPool(){
        requestsPool.shutdown();
    }

}
