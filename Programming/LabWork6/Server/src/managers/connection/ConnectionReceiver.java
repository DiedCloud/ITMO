package managers.connection;

import common.descriptions.CommandDescription;
import main.Main;
import managers.Invoker;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ConnectionReceiver {


    public void run(Invoker invoker) throws SocketException, NumberFormatException {
        byte[] arr = new byte[10*1024];
        int len = arr.length;
        DatagramSocket ds;
        DatagramPacket dp;
        try {
            int port = Integer.parseInt(System.getenv("SERVER_PORT"));
            ds = new DatagramSocket(port);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Укажите в переменной SERVER_PORT порт, на котором будет работать приложение. Порт должен быть целым числом");
        } catch (SocketException | SecurityException e) {
            throw new SocketException("Не удалось получить доступ к указанному порту");
        }

        InputController inputController = new InputController(ds);

        while (true) {
            Main.logger.info("Listening started");
            dp = new DatagramPacket(arr, len);
            try {
                ds.receive(dp);
                ByteArrayInputStream byteStream = new ByteArrayInputStream(dp.getData());
                ObjectInputStream ois = new ObjectInputStream(byteStream);
                CommandDescription cd = (CommandDescription) ois.readObject();

                inputController.parse(cd, invoker, dp);
            } catch (IOException | ClassNotFoundException e) {
                Main.logger.error(e.getMessage(), e);
            }
        }
    }
}
