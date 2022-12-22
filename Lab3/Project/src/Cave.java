import java.util.ArrayList;
import java.util.Objects;

public class Cave implements Storages{
    private Location cord;
    private ArrayList<Item> items;

    public Cave(Location cord){
        this.cord = cord;
    }
    public Location getCord() {
        return cord;
    }

    @Override
    public ArrayList<Item> getObjects() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cave cave = (Cave) o;
        return Objects.equals(getCord(), cave.getCord()) && Objects.equals(items, cave.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCord(), items);
    }

    @Override
    public String toString() {
        return "Cave{" +
                "cord=" + cord +
                ", items=" + items +
                '}';
    }
}
