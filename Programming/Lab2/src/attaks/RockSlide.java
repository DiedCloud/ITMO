package attaks;

import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

import java.util.Random;

public class RockSlide extends PhysicalMove {
    private boolean oppIsFlinched;

    public RockSlide(Type type, double pow, double acc) {
        super(type, pow, acc);
    }

    @Override
    protected void applyOppEffects(Pokemon p) {
        Random r = new Random();
        oppIsFlinched = r.nextInt(10) < 3;
        if (oppIsFlinched) {
            Effect.flinch(p);
        }
    }

    @Override
    protected java.lang.String describe() {
        return "использует Rock Slide" + (oppIsFlinched ? ", противник в страхе" : "");
    }
}
