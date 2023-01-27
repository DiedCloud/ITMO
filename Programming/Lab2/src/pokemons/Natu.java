package pokemons;

import attaks.*;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Natu extends Pokemon {
    public Natu(String name, int level) {
        super(name, level);
        addMove(new ConfuseRay(Type.GHOST, 0, 100));
        addMove(new AerialAce(Type.FLYING, 60, 100));
        addMove((new Leer(Type.NORMAL, 0, 100)));
        setType(Type.PSYCHIC, Type.FLYING);
    }
}
