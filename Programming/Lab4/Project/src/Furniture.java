import java.util.ArrayList;
import java.util.Objects;

public class Furniture extends Item implements Storages {
    private Location cord;
    private ArrayList<Item> inventory;
    private FurnitureType type;

    public Furniture(String name, Location cord, ArrayList<Item> inventory, FurnitureType furnitureType){
        this.name = name;
        this.cord = cord;
        this.inventory = inventory;
        type = furnitureType;
    }

    public Item takeFromInv(int index){
        return inventory.remove(index);
    }

    public Location getPlace() {
        return cord;
    }

    public FurnitureType getType() {
        return type;
    }

    @Override
    public void goBad() {
        super.goBad();
        inventory.clear();
        System.out.println("Шкаф сломался, содеожимое безвозвратно утрачено");
    }

    @Override
    public ArrayList<Item> getObjects() {
        return inventory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Furniture furniture = (Furniture) o;
        return Objects.equals(cord, furniture.cord) && Objects.equals(inventory, furniture.inventory) &&
                getType() == furniture.getType() && getName().equals(furniture.getName()) && getBadStatus() == furniture.getBadStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(cord, inventory, getType());
    }

    @Override
    public String toString() {
        return "Furniture{" +
                "cord=" + cord +
                ", inventory=" + inventory +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", isBad=" + isBad +
                '}';
    }
}
