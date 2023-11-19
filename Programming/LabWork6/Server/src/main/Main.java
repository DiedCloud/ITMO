package main;

import commands.SaveMusicCommand;
import commands.SaveUserCommand;
import managers.Invoker;
import managers.connection.ConnectionReceiver;
import org.apache.logging.log4j.Logger;
import receivers.MusicReceiver;
import receivers.UserReceiver;

import org.apache.logging.log4j.LogManager;

import javax.swing.*;
import java.util.Scanner;

public class Main {

    public static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        var saving = new Timer(600000, //every 10 minutes
                event -> {
                    try {
                        new SaveMusicCommand().execute();
                        new SaveUserCommand().execute();
                        logger.info("Collections saved");
                    } catch (Exception e) {
                        logger.error("Can't save. " + e.getMessage(), e);
                    }
                });
        saving.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Server is closing. Save collections?\n'Y' - yes\n'N' - no");
            Scanner s = new Scanner(System.in);
            if (s.nextLine().equalsIgnoreCase("y")) {
                try {
                    new SaveMusicCommand().execute();
                    new SaveUserCommand().execute();
                    logger.info("Collections saved");
                } catch (Exception e) {
                    logger.error("Can't save. " + e.getMessage(), e);
                }
            }
            logger.info("Server application stopped");
        }));

        try {
            UserReceiver.GetInstance();
            MusicReceiver.GetInstance();
            logger.info("Files loaded");
            (new ConnectionReceiver()).run(new Invoker());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
