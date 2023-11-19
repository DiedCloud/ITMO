package TextReceivers;

import common.LocalizationKeys;
import managers.BaseTextReceiver;
import managers.LocalizationManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LocalizedTextReceiver implements BaseTextReceiver {

    protected final LocalizationManager localizationManager;

    public LocalizedTextReceiver(String language, String baseName) {
        localizationManager = new LocalizationManager(language, baseName);
    }

    public void switchLocalization(String language) {
        localizationManager.setLanguage(language);
    }

    public void print(LocalizationKeys key) {
        System.out.print(localizationManager.getLine(key));
    }

    public void println(LocalizationKeys key) {
        println(localizationManager.getLine(key));
    }

    @Override
    public void println(String s) {
        try{
            print(LocalizationKeys.valueOf(s.trim()));
        }catch (Exception e){
            System.out.println(s);
        }
    }

    @Override
    public void print(String s) {
        try{
            print(LocalizationKeys.valueOf(s.trim().toUpperCase(Locale.getDefault())));
        }catch (Exception e){
            System.out.print(s);
        }
    }

    public void println(Map<LocalizationKeys, ?> hashMap) {
        for(var key : hashMap.keySet()){
            print(key);
            print(" - ");
            println(hashMap.get(key));
        }
    }

    public void println(Object o) {
        System.out.println(o);
    }

    public void println(common.Collection<common.MusicBand> c) {
        for (var e: c.getCollection()) {
            println(e);
        }
    }

    public void print(Collection<?> c) {
        for (var e: c) {
            println(e);
        }
    }

    public void printAutoDetectType(Object arg) {
        if (arg instanceof LocalizationKeys) {
            println((LocalizationKeys) arg);
        } else if (arg instanceof Map<?, ?>) {
            println((Map<LocalizationKeys, ?>) arg);
        } else if (arg instanceof Collection<?>) {
            print((Collection<?>) arg);
        } else {
            println(arg);
        }
    }

    public void print(LocalDate localDate) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(localizationManager.getLocale());
        String formattedDate = localDate.format(dateFormatter);
        println(formattedDate);
    }

}
