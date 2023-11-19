package commands;

import receivers.*;
import result.Result;
/**
 * Базовый интерфейс для всех возможных команд.
 */
public abstract class Command<T extends Receiver<?>> {
    T receiver;

    public Command(T receiver){
        this.receiver = receiver;
    }
    /**
     * A method that will be called when executing the command.
     * @return the result of executing the command
     */
    public abstract Result<?> execute();
}