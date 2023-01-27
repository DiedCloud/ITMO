package attaks;

import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Type;

import java.util.Random;

public class Thunder extends SpecialMove {
    private boolean oppIsParalyzed;
    public Thunder(Type type, double pow, double acc) {
        super(type, pow, acc);
    }

    @Override
    protected void applyOppEffects(Pokemon p) {
        Random r = new Random();
        oppIsParalyzed = r.nextInt(10) < 3;
        if (oppIsParalyzed) {
            Effect.paralyze(p);
        }
    }

    @Override
    protected java.lang.String describe() {
        return "использует Thunder" + (oppIsParalyzed ? ", противник парализован" : "");
    }
}