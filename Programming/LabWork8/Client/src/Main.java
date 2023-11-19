import TextReceivers.LocalizedTextReceiver;
import TextReceivers.TextReceiver;
import common.LocalizationKeys;
import loaders.ConsoleLoader;
import modules.*;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.channels.DatagramChannel;

public class Main {
    public static void main(String[] args) {
        LocalizedTextReceiver localizedTextReceiver = new LocalizedTextReceiver("en_AU","en_AU");
        int port = 0;
        try {
            port = Integer.parseInt(System.getenv("PORT"));
        }catch (NumberFormatException e){
            localizedTextReceiver.println(LocalizationKeys.ERROR_PARSING_PORT);
            System.exit(0);
        }
        DatagramChannel channel = null;
        try {
            channel = DatagramChannel.open();
        }
        catch (IOException e){
            localizedTextReceiver.println(LocalizationKeys.ERROR_OPENING_CHANNEL);
            System.exit(0);
        }

        RequestHandler requestHandler = null;
        try {
            requestHandler = new RequestHandler(channel, 12288, 6000);
        } catch (IOException e) {
            localizedTextReceiver.println(LocalizationKeys.ERROR_CREATING_REQUEST_HANDLER);
            System.exit(0);
        }
        ConsoleLoader loader = new ConsoleLoader(localizedTextReceiver);
        ObjectSender objectSender = null;
        try {
            objectSender = new ObjectSender(InetAddress.getLocalHost(), port, channel);
        } catch (Exception e) {
            localizedTextReceiver.println(LocalizationKeys.ERROR_CREATING_OBJECT_SENDER);
        }
        InteractiveMode.getInstance(localizedTextReceiver, loader, requestHandler, objectSender).start();
    }
}