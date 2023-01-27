package attaks;

import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Type;

public class Boomburst extends SpecialMove {
    public Boomburst(Type type, double pow, double acc) {
        super(type, pow, acc);
    }

    @Override
    protected java.lang.String describe() {
        return "использует Boomburst";
    }
}
