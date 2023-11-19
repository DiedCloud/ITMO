package modules;

import result.UpdateWarning;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdateManager {

    private static UpdateManager um;
    private ExecutorService updatePool = Executors.newCachedThreadPool();

    public void addUpdate(Runnable task){
        updatePool.submit(task);
    }

    public void proccessUpdate(UpdateWarning uwu){
        System.out.println("Сюда привяжешь апдейт коллекции. Мы самые крутые айтишники на свете, держу в курсе!");
    }

    private UpdateManager(){
    }

    public static UpdateManager GetInstance(){
        if(um == null){
            um = new UpdateManager();
        }
        return um;
    }


}
