package model;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Managed bean для получения текущего времени и только.
 */
@Named
@ApplicationScoped
public class TimeBean {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Использует {@link LocalDateTime#now()}
     * @return строковое представление времени: "yyyy-MM-dd HH:mm:ss"
     */
    public String getNowTime() {
        return LocalDateTime.now().format(formatter);
    }

}