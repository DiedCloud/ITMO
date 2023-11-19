package modules;

import common.LocalizationKeys;
import result.Result;
import result.UpdateWarning;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;

public class ResultManager {
    private RequestHandler requestHandler;

    private boolean isReady = false;
    private Result<?> result;

    public ResultManager(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public <T> T deserialize(DatagramPacket packet) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData());
        ObjectInputStream objectInputStream;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
        } catch (IOException e) {
            throw new RuntimeException("SERIALIZATION_ERROR");
        }
        try {
            return (T) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void start(Notifier n) {
        while (true) {
            Result<DatagramPacket> packet = requestHandler.receivePacketWithTimeout();
            if (!packet.isSuccess()) {
                continue;
            }
            try {
                UpdateWarning warning = deserialize(packet.getValue().get());
                System.out.println("Пришло оповещение! с коллекцией происходит какой-то разврат!!!!!!");
                n.warnAll(warning);
            } catch (Exception e) {
                try {
                    result = deserialize(packet.getValue().get());
                    isReady = true;
                } catch (Exception e1) {
                    continue;
                }
            }
        }
    }

    public boolean isReady() { return isReady; }

    public Result<?> getResult() {
        if (!isReady)
            return Result.failure(new Exception(String.valueOf(LocalizationKeys.ERROR_SERVER_CONNECTION)));
        isReady = false;
        return result;
    }
}
