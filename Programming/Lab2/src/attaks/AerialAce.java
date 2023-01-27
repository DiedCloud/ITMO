package attaks;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;


public class AerialAce extends PhysicalMove {
    private boolean oppIsFlinched;

    public AerialAce(Type type, double pow, double acc) {
        super(type, pow, acc);
    }

    @Override
    protected java.lang.String describe() {
        return "использует Aerial Ace";
    }

    @Override
    protected boolean checkAccuracy(Pokemon att, Pokemon def) {
        return true;
    }
}
