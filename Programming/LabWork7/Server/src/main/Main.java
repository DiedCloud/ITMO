package main;

import managers.Invoker;
import managers.connection.ConnectionReceiver;
import managers.connection.DatagramManager;
import managers.connection.InputController;
import managers.connection.ResultSender;
import org.apache.logging.log4j.Logger;
import receivers.MusicReceiver;
import receivers.UserReceiver;

import org.apache.logging.log4j.LogManager;
import java.net.SocketException;

public class Main {

    public static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ConnectionReceiver.shutdownPool();
            Main.logger.info("ConnectionReceiver shutdown successfully");
            InputController.shutdownPool();
            Main.logger.info("InputController shutdown successfully");
            ResultSender.shutdownPool();
            Main.logger.info("ResultSender shutdown successfully");
            Invoker.shutdownPool();
            Main.logger.info("Invoker shutdown successfully");
        }));

        try {
            Class.forName("org.postgresql.Driver");
            try {
                UserReceiver.GetInstance();
                MusicReceiver.GetInstance();
                int port;
                try {
                    port = Integer.parseInt(System.getenv("SERVER_PORT"));
                } catch (NumberFormatException e) {
                    throw new NumberFormatException("Укажите в переменной SERVER_PORT порт, на котором будет работать приложение. Порт должен быть целым числом");
                } catch (SecurityException e) {
                    throw new SocketException("Не удалось получить доступ к указанному порту");
                }
                byte[] arr = new byte[12288];
                int len = arr.length;
                DatagramManager dm = new DatagramManager(port,arr,len);
                ConnectionReceiver connectionReceiver = new ConnectionReceiver();
                connectionReceiver.run(new Invoker());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        } catch (ClassNotFoundException e) {
            logger.error("PostgreSQL JDBC Driver is not found. ", e);
        }
    }
}
