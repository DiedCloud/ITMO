package attaks;

import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Type;

import java.util.Random;

public class AirSlash extends SpecialMove {
    private boolean oppIsFlinched;

    public AirSlash(Type type, double pow, double acc) {
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
        return "использует Air Slash" + (oppIsFlinched ? ", противник в страхе" : "");
    }
}