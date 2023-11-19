package modules;

import managers.BaseTextReceiver;

import common.Collection;

public class TextReceiver implements BaseTextReceiver {
    public void print(String message) {
        System.out.print(message);
    }
    public void println(String message) {
        print(message + "\n");
    }
    public void println(Object o) {
        print(o);
        println("");
    }

    public void print(Object object) {
        if(object instanceof Collection)
            for(Object o : ((Collection<?>) object).getCollection()) {
                println(o.toString());
            }
        else
            if(object instanceof java.util.Collection<?>)
            {
                for(Object o : (java.util.Collection<?>) object) {
                    println(o.toString());
                }
            }
                else println(object.toString());
    }

}
