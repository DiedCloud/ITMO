import java.util.ArrayList;
import java.util.Objects;

public class World implements Storages {
    private Weather weather;
    private ArrayList<Storages> locations;

    public World(Weather weather, ArrayList<Storages> locations){
        this.weather = weather;
        this.locations = locations;
    }

    public Weather getWeather() {
        return weather;
    }

    public void changeWeather(Weather type) {
        weather = type;
        System.out.println("Погода в мире изменена на " + weather);
    }

    @Override
    public ArrayList<Storages> getObjects() {
        return locations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        World world = (World) o;
        return getWeather() == world.getWeather() && Objects.equals(locations, world.locations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getWeather(), locations);
    }

    @Override
    public String toString() {
        return "World{" +
                "weather=" + weather +
                ", locations=" + locations +
                '}';
    }
}
