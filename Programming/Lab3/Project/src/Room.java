import java.util.ArrayList;
import java.util.Objects;

public class Room implements Storages {
    private String name;
    private ArrayList<Furniture> furniture;
    private Location cord;

    public Room(String name, ArrayList<Furniture> furniture, Location cord){
        this.name = name;
        this.furniture = furniture;
        this.cord = cord;
    }

    public Location getCord() {
        return cord;
    }

    @Override
    public ArrayList<Furniture> getObjects() {
        return furniture;
    }

    public String getName() {
            return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(getName(), room.getName()) &&
                Objects.equals(furniture, room.furniture) &&
                Objects.equals(getCord(), room.getCord());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), furniture, getCord());
    }

    @Override
    public String toString() {
        return "Room{" +
                "name='" + name + '\'' +
                ", cord=" + cord +
                '}';
    }
}
