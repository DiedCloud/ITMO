package modules;

import caller.Caller;

import commandRealization.CommandRealization;
import result.Result;

import java.util.ArrayList;
import java.util.List;

public class CallableManager {
    private final List<Caller> callers = new ArrayList<>();
    public void add(Caller caller) {
        callers.add(caller);
    }

    public List<Result<?>> callAll() {
        List<Result<?>> results = new ArrayList<>();
        for(Caller caller : callers){
            try {
                caller.call();
                if (caller instanceof CommandRealization)
                    results.add(((CommandRealization) caller).getResult());
            }catch (Exception e){
                System.out.println(e.getMessage());
                results.add(Result.failure(e));
            }
        }
        clear();
        return results;
    }

    public void clear() {
        callers.clear();
    }
}

