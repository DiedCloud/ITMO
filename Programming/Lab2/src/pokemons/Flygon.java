package pokemons;

import attaks.*;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Flygon extends Pokemon {
    public Flygon(String name, int level) {
        super(name, level);
        addMove(new Confide(Type.NORMAL, 0, 0));
        addMove(new SandAttack(Type.GROUND, 0, 100));
        addMove(new Boomburst(Type.NORMAL, 140, 100));
        addMove(new Flamethrower(Type.FIRE, 90, 100));
        setType(Type.GROUND, Type.DRAGON);
    }
}
