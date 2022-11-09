import pokemons.*;
import ru.ifmo.se.pokemon.Battle;
import ru.ifmo.se.pokemon.Pokemon;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Battle b = new Battle();
        Pokemon p1 = new Regigigas("t1", 1);
        p1.setStats(110, 160, 110, 80, 110, 100);

        Pokemon p2 = new Natu("t1", 1);
        p2.setStats(40, 50, 450, 70, 45, 70);

        Pokemon p3 = new Xatu("t1", 1);
        p3.setStats(65, 75, 70, 95, 70, 95);

        Pokemon p4 = new Trapinch("t2", 1);
        p4.setStats(45, 100, 45, 45, 45, 10);

        Pokemon p5 = new Vibrava("t2", 1);
        p5.setStats(50, 70, 50, 50, 50, 70);

        Pokemon p6 = new Flygon("t2", 1);
        p6.setStats(80, 100, 80, 80, 80, 100);

        b.addAlly(p1);
        b.addAlly(p2);
        b.addAlly(p3);
        b.addFoe(p4);
        b.addFoe(p5);
        b.addFoe(p6);
        b.go();
    }
}