package managers;

import common.LocalizationKeys;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationManager {
    private ResourceBundle bundle;
    private String language = "en_AU";
    private Locale locale;

    public LocalizationManager(String language, String baseName) {
        this.language = language;
        this.locale = new Locale(language);
        bundle = ResourceBundle.getBundle(baseName, locale);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String baseName) {
        this.language = baseName;
        this.locale = new Locale(baseName);
        bundle = ResourceBundle.getBundle(language, locale);
    }

    public String getLine(LocalizationKeys key){
        return bundle.getString(key.toString());
    }

    public Locale getLocale() {
        return locale;
    }

}
