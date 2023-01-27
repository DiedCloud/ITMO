package attaks;

import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Type;

import java.util.Random;

public class Flamethrower extends SpecialMove {
    private boolean oppIsBurned;
    public Flamethrower(Type type, double pow, double acc) {
        super(type, pow, acc);
    }

    @Override
    protected void applyOppEffects(Pokemon p) {
        Random r = new Random();
        oppIsBurned = r.nextInt(10) < 1;
        if (oppIsBurned) {
            Effect.burn(p);
        }
    }

    @Override
    protected java.lang.String describe() {
        return "использует Flamethrower" + (oppIsBurned ? ", противник горит" : "");
    }
}