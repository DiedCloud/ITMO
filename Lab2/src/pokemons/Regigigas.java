package pokemons;

import attaks.*;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Regigigas extends Pokemon {
    public Regigigas(String name, int level) {
        super(name, level);
        addMove(new RockSlide(Type.ROCK, 75, 90));
        addMove(new ThunderWave(Type.ELECTRIC, 90, 20));
        addMove(new ConfuseRay(Type.GHOST, 0, 100));
        addMove(new Thunder(Type.ELECTRIC, 110, 70));
        setType(Type.NORMAL);
    }
}
