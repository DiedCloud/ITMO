import java.util.ArrayList;
import java.util.Objects;

public class Rocket implements Storages, Openable{
    private Location cord;
    private Boolean isDoorOpen;
    private ArrayList<Room> rooms;
    private int fuel;

    public Rocket(Location cord, int fuel, ArrayList<Room> rooms){
        this.cord = cord;
        this.fuel = fuel;
        this.rooms = rooms;
        isDoorOpen = false;
    }

    @Override
    public ArrayList<Room> getObjects() {//getRooms
        return rooms;
    }

    public Location getCord() {
        return cord;
    }

    public Boolean getDoorStatus() {
        return isDoorOpen;
    }

    public int getFuel() {
        return fuel;
    }

    @Override
    public void open() {//pressDoorButton
        isDoorOpen = !isDoorOpen;
        if (isDoorOpen){
            System.out.println("Дверь открыта");
        }
        else {
            System.out.println("Дверь закрыта");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rocket rocket = (Rocket) o;
        return getFuel() == rocket.getFuel() &&
                Objects.equals(getCord(), rocket.getCord()) &&
                Objects.equals(getDoorStatus(), rocket.getDoorStatus()) &&
                Objects.equals(rooms, rocket.rooms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCord(), getDoorStatus(), rooms, getFuel());
    }

    @Override
    public String toString() {
        return "Rocket{" +
                "cord=" + cord +
                ", isDoorOpen=" + isDoorOpen +
                ", fuel=" + fuel +
                '}';
    }
}
