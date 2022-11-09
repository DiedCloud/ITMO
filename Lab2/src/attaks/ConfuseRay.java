package attaks;

import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.StatusMove;
import ru.ifmo.se.pokemon.Type;

public class ConfuseRay extends StatusMove {
    public ConfuseRay(Type type, double pow, double acc) {
        super(type, pow, acc);
    }

    @Override
    protected void applyOppEffects(Pokemon p) {
        Effect.confuse(p);
    }

    @Override
    protected java.lang.String describe() {
        return "использует Confuse Ray";
    }
}
