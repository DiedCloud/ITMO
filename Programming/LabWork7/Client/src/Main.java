import common.descriptions.CommandDescription;
import loaders.ConsoleLoader;
import modules.*;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.channels.DatagramChannel;

public class Main {
    public static void main(String[] args) {
        int port = 0;
        try {
            port = Integer.parseInt(System.getenv("PORT"));
        }catch (NumberFormatException e){
            System.out.println("Error while parsing port");
            System.exit(0);
        }
        TextReceiver textReceiver = new TextReceiver();
        DatagramChannel channel = null;
        try {
            channel = DatagramChannel.open();
        }
        catch (IOException e){
            textReceiver.println("Error while opening channel");
            System.exit(0);
        }

        RequestHandler requestHandler = null;
        try {
            requestHandler = new RequestHandler(channel, 12288, 6000);
        } catch (IOException e) {
            textReceiver.println("Error while creating request handler");
            System.exit(0);
        }
        ConsoleLoader loader = new ConsoleLoader(textReceiver);
        ObjectSender objectSender = null;
        try {
            objectSender = new ObjectSender(InetAddress.getLocalHost(), port, channel);
        } catch (Exception e) {
            textReceiver.println("Error while creating object sender");
        }
        InteractiveMode.getInstance(textReceiver, loader, requestHandler, objectSender).start();
    }
}