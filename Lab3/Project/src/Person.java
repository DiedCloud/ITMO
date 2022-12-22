import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Objects;

public class Person implements Storages {
    private String name;
    private int weight;
    private int speed;
    int hunger;
    private Storages place;
    private Location cord;
    private ArrayList<Clothes> wearedClothes;
    int guilt;

    public Person(String name, Storages place, ArrayList<Clothes> clothes, int guilt, int hunger, Location location){
        this.name = name;
        this.place = place;
        wearedClothes = clothes;
        this.guilt = guilt;
        weight = 0;
        for (Clothes i: clothes){
            weight += i.getWeight();
        }
        speed = 100 - weight;
        this.hunger = hunger;
        cord = location;
    }

    protected void eat(Food food){
        if (hunger > food.getAmount()){
            hunger -= food.getAmount();
            food.Take(food.getAmount());
            System.out.println(name + " съел " + food.getName() + ". Hunger = " + this.hunger);
        }
        else{
            food.Take(food.getAmount());
            hunger = 0;
            System.out.println(name + " съел " + food.getName()+ " и наелся.");
        }
    }

    protected void run(Location cord){
        this.cord = cord;
        System.out.println("Runnig...");
    }

    protected void press(Rocket rocket){
        System.out.println(name + " нажимет на кнопку " + rocket + " в попытке открыть дверь");
        rocket.open();
    }

    protected ArrayList<Clothes> dressUp(ArrayList<Clothes> clothes){
        ArrayList<Clothes> dressOut = new ArrayList<Clothes>();
        for (Clothes i: clothes){
            for (Clothes j: this.wearedClothes){
                if (i.getType() == j.getType()){
                    dressOut.add(j);
                    weight += i.getWeight() - j.getWeight();
                    System.out.println(name + " переодел " + j.name + " на " + i.name);
                }
            }
            if (!Objects.equals(i.getName(), "Empty")){
                wearedClothes.add(i);
            }
        }
        for (Clothes i: dressOut){
            wearedClothes.remove(i);
        }
        speed = 100 - weight;
        return dressOut;
    }

    protected void ChangeLocation(Storages place){
        this.place = place;
        System.out.println(name + " теперь в " + this.place);
    }

    public int getWeight() {
        return weight;
    }

    public int getHunger() {
        return hunger;
    }

    public int getSpeed() {
        return speed;
    }

    public Storages getPlace() {
        return place;
    }

    public ArrayList<Clothes> getWearedClothes() {
        return wearedClothes;
    }

    public int getGuilt() {
        return guilt;
    }

    public Location getCord() {
        return cord;
    }

    public String getName() {
        return name;
    }

    @Override
    public ArrayList<Clothes> getObjects() {
        return wearedClothes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return getWeight() == person.getWeight() &&
                getSpeed() == person.getSpeed() &&
                getHunger() == person.getHunger() &&
                getGuilt() == person.getGuilt() &&
                Objects.equals(getName(), person.getName()) &&
                Objects.equals(getPlace(), person.getPlace()) &&
                Objects.equals(getCord(), person.getCord()) &&
                Objects.equals(getWearedClothes(), person.getWearedClothes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, getWeight(), getSpeed(), getHunger(), getPlace(), getCord(), getWearedClothes(), getGuilt());
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", cord=" + cord +
                '}';
    }
}
