package managers.connection;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.locks.ReentrantLock;

public class DatagramManager {
    protected static DatagramSocket ds;
    protected DatagramPacket dp;

    public DatagramManager(DatagramSocket ds, DatagramPacket dp){
        DatagramManager.ds = ds;
        this.dp = dp;
    }


    public DatagramManager(int port, byte[] arr, int len) throws Exception {
        try {
            DatagramManager.ds = new DatagramSocket(port);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Укажите в переменной SERVER_PORT порт, на котором будет работать приложение. Порт должен быть целым числом");
        } catch (SocketException | SecurityException e) {
            throw new Exception("Не удалось получить доступ к указанному порту");
        }
        dp = new DatagramPacket(arr, len);
    }

    public DatagramManager() throws Exception {
        if(DatagramManager.ds == null)
            throw new RuntimeException("Error with socket initialization");
        byte[] arr = new byte[12288];
        int len = arr.length;
        dp = new DatagramPacket(arr, len);
    }
    public DatagramPacket getDp() {
        return dp;
    }

    public void setDp(DatagramPacket dp) {
        this.dp = dp;
    }

    public DatagramSocket getDS() {
        return ds;
    }

    public void setDS(DatagramSocket ds) {
        this.ds = ds;
    }


    public DatagramPacket receiveDatagramPacket(){
        return dp;
    }

    public <T> T receive(){
        try {
            ds.receive(dp);
        } catch (IOException e) {
            throw new RuntimeException("Error with getting datagram");
        }
        ByteArrayInputStream byteStream = new ByteArrayInputStream(dp.getData());
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(byteStream);
        } catch (IOException e) {
            throw new RuntimeException("Error while reading object");
        }
        try {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public InetAddress getHost() {
        return dp.getAddress();
    }

    public int getPort() {
        return dp.getPort();
    }
}
