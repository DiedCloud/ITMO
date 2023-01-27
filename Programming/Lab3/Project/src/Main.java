import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Storages> locations = new ArrayList<Storages>();
            ArrayList<Room> rooms = new ArrayList<Room>();
                ArrayList<Furniture> furniture = new ArrayList<Furniture>();
                    ArrayList<Item> foods = new ArrayList<Item>();
                    foods.add(new Food("PVC tube", 90, 1));
                    foods.add(new Food("PVC tube", 90, 1));
                    foods.add(new Food("Tube", 40, 1));
                    foods.add(new Food("Pouch", 30, 10));
                furniture.add(new Furniture("Cupboard1", new Location(1,2,1), foods, FurnitureType.Cupboard));
                    foods = new ArrayList<Item>();
                    foods.add(new Food("PVC tube", 90, 1));
                    foods.add(new Food("PVC tube", 90, 1));
                    foods.add(new Food("Tube", 40, 1));
                    foods.add(new Food("Pouch", 30, 10));
                furniture.add(new Furniture("Termostat6423", new Location(1,2,2), foods, FurnitureType.Termostat));
                    foods = new ArrayList<Item>();
                    foods.add(new Food("Pack", 27, 6));
                    foods.add(new Food("PVC tube", 90, 1));
                    foods.add(new Food("Tube", 45, 1));
                    foods.add(new Food("CookiesPouch", 460, 15));
                    furniture.add(new Furniture("Fridge234", new Location(4,2,1), foods, FurnitureType.Fridge));
            rooms.add(new Room("Kitchen", furniture, new Location(0, 1, 0)));
        locations.add(new Rocket(new Location(0,0,0), 50, rooms));

        locations.add(new Cave(new Location(0,0,0)));
        World world = new World(Weather.Sunny, locations);

        ArrayList<Clothes> clothes = new ArrayList<Clothes>();
        clothes.add(new Clothes("Boots", 0, ClothesType.ForFoots));
        clothes.add(new Clothes("Pants", 5, ClothesType.ForLegs));
        clothes.add(new Clothes("Shirt", 4, ClothesType.ForBody));
        Person donut = new Person("Donut", world, clothes, 0, 5, new Location(100, 0, 100));
        //--------------
        donut.run(new Location(80, 0, 95));

        world.changeWeather(Weather.Heat);

        clothes = new ArrayList<Clothes>();
        clothes.add(new Clothes("Empty", 0, ClothesType.ForLegs));
        donut.dressUp(clothes);

        for (Storages i: world.getObjects()){
            if (i.getClass() == Rocket.class){
                donut.run(((Rocket)i).getCord());
                donut.guilt = 80;
                donut.hunger += 10;
            }
        }

        ArrayList<Storages> t = donut.getPlace().getObjects();
        for (Storages i: t){
            if (i.getClass() == Rocket.class){
                donut.press((Rocket)i);
                donut.ChangeLocation(i);
                donut.guilt += 7;
                break;
            }
        }

        t = donut.getPlace().getObjects();
        for (Storages i: t){
            if (i.getClass() == Room.class){
                if (((Room) i).getName() == "Kitchen"){
                    donut.ChangeLocation(i);
                    donut.guilt += 1;
                    break;
                }
            }
        }

        t = donut.getPlace().getObjects();
        saturation:
        for (Storages i: t){
            if (i.getClass() == Furniture.class){
                ArrayList<Item> food = ((Furniture) i).getObjects();
                int j = 0;
                while (j < food.size()){
                    if (donut.getHunger() > 0){
                        if (food.get(j).getClass() == Food.class){
                            if ( ((Food) food.get(j)).getGoodForXDays() > 0 ){
                                donut.eat((Food)((Furniture) i).takeFromInv(j));
                            }
                            else{
                                ((Furniture) i).takeFromInv(j);
                                System.out.println(donut.getName() + " нашел просроченный продукт и выкинул.");
                            }
                        }
                    }
                    else{
                        System.out.println(donut.getName() + " наелся.");
                        break saturation;
                    }
                }
            }
        }
    }
}

