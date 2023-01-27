import java.util.Objects;

public class Clothes extends Item {
    private int weight;
    private ClothesType type;

    public Clothes(String name, int weight, ClothesType type){
        this.name = name;
        this.weight = weight;
        this.type = type;
    }

    public int getWeight() {
        return weight;
    }

    public ClothesType getType() {
        return type;
    }

    @Override
    public void goBad() {
        super.goBad();
        System.out.println("Элемент одежды " + name + " порвался.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clothes clothes = (Clothes) o;
        return getWeight() == clothes.getWeight() && getType() == clothes.getType() &&
                getName().equals(clothes.getName()) && getBadStatus() == clothes.getBadStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getWeight(), getType(), getName(), getBadStatus());
    }

    @Override
    public String toString() {
        return "Clothes{" +
                "weight=" + weight +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", isBad=" + isBad +
                '}';
    }
}
