package pokemons;

import attaks.*;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Trapinch extends Pokemon {
    public Trapinch(String name, int level) {
        super(name, level);
        addMove(new Confide(Type.NORMAL, 0, 0));
        addMove(new SandAttack(Type.GROUND, 0, 100));
        setType(Type.GROUND);
    }
}
