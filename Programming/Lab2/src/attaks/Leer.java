package attaks;

import ru.ifmo.se.pokemon.*;

public class Leer  extends StatusMove {
    public Leer(Type type, double pow, double acc) {
        super(type, pow, acc);
    }

    @Override
    protected void applyOppEffects(Pokemon p) {
        p.addEffect(new Effect().stat(Stat.DEFENSE, -1));
    }

    @Override
    protected java.lang.String describe() {
        return "использует Leer";
    }
}