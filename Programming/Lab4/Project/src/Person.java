import com.sun.jdi.ClassType;

import java.util.ArrayList;
import java.util.Objects;

public class Person implements Storages {
    private final String name;
    private int weight;
    private int speed;
    private Storages place;
    private Location cord;
    private ArrayList<Clothes> wearedClothes;
    EatingProgressing eatingProgressing;
    int guilt;
    int sleepiness;

    public Person(String name, Storages place, ArrayList<Clothes> clothes, int guilt, int hunger, Location location, int sleepiness){
        this.name = name;
        this.place = place;
        wearedClothes = clothes;
        this.guilt = guilt;
        weight = 0;
        for (Clothes i: clothes){
            weight += i.getWeight();
        }
        speed = 100 - weight;
        cord = location;
        this.sleepiness = sleepiness;
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

    public EatingProgressing getEatingProgressing() {
        return eatingProgressing;
    }

    public int getSleepiness() {
        return sleepiness;
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
                getEatingProgressing() == person.getEatingProgressing() &&
                getGuilt() == person.getGuilt() &&
                Objects.equals(getName(), person.getName()) &&
                Objects.equals(getPlace(), person.getPlace()) &&
                Objects.equals(getCord(), person.getCord()) &&
                Objects.equals(getWearedClothes(), person.getWearedClothes()) &&
                getSleepiness() == person.getSleepiness();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, getWeight(), getSpeed(), getEatingProgressing(), getPlace(), getCord(), getWearedClothes(), getGuilt(), getSleepiness());
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", cord=" + cord +
                '}';
    }

    class EatingProgressing{
        int hunger;
        EatingProgressing(int hunger){
            this.hunger = hunger;
        }

        public int getHunger() {
            return hunger;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EatingProgressing that = (EatingProgressing) o;
            return getHunger() == that.getHunger();
        }

        @Override
        public int hashCode() {
            return Objects.hash(getHunger());
        }

        @Override
        public String toString() {
            return "EatingProgressing{" +
                    "hunger=" + hunger +
                    '}';
        }
    }

    static public class Calculating{
        static final public int c = 2; //коэфициент, показывающий на сколько дней хватает одной единицы утолённого голода
        static public ArrayList<Food> findFood(Rocket r){
            ArrayList<Food> res = new ArrayList<Food>();

            for (Room i: r.getObjects()){
                for (Furniture j: i.getObjects()){
                    for(Item y: j.getObjects()){
                        if (y instanceof Food){
                            res.add((Food)y);
                        }
                    }
                }
            }
            return res;
        }

        static public int stockEnoughForDays (ArrayList<Food> food) throws NoFoodException{
            class WrongTypeException extends RuntimeException{
                private Class<? extends Item> type;

                public Class<? extends Item> getType() {
                    return type;
                }

                public WrongTypeException(String message, Class<? extends Item> type){
                    super(message);
                    this.type = type;
                }
            }
            int res = 0;
            if (food.size() == 0) {throw new NoFoodException("Еды нет, ее не хватит ни на сколько");}
            for (Item i: food){
                if (!(i instanceof Food)) throw new WrongTypeException("В спике оказалась не еда", i.getClass());
                res += ((Food) i).getAmount()*c;
            }
            return res;
        }
    }
}

class NoFoodException extends Exception{
    NoFoodException(String message){
        super(message);
    }
}
