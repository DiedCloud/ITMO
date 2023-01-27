import java.util.Objects;
public class Food extends Item implements Openable{
    private int goodForXDays;
    private int amount;

    public Food(String name, int goodForXDays, int amount){
        this.name = name;
        this.goodForXDays = goodForXDays;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public int getGoodForXDays() {//getTerm
        return goodForXDays;
    } //getTerm

    public void Take(int n){
        if (n <= amount){
            amount -= n;
            System.out.println("Из упаковки " + name + " взято " + n + ". Остлось в упаковке: " + amount);
        }
    }

    @Override
    public void goBad() {
        goodForXDays = 0;
        System.out.println("Срок годности " + name + " закончился");
    }

    @Override
    public void open() {
        goodForXDays = goodForXDays/2;
        System.out.println("Октрыта упаковка " + name + ". Срок годности уменьшился. Осталось дней: " + goodForXDays);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return getGoodForXDays() == food.getGoodForXDays() && getAmount() == food.getAmount() &&
                getName().equals(food.getName()) && getBadStatus() == food.getBadStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGoodForXDays(), getAmount(), getName(), getBadStatus());
    }

    @Override
    public String toString() {
        return "Food{" +
                "goodForXDays=" + goodForXDays +
                ", amount=" + amount +
                ", name='" + name + "'" +
                ", isBad=" + isBad +
                '}';
    }
}
