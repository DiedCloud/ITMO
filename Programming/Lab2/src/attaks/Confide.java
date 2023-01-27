package attaks;

import ru.ifmo.se.pokemon.*;

public class Confide extends SpecialMove {

    public Confide(Type type, double pow, double acc) {
        super(type, pow, acc);
    }

    @Override
    protected void applyOppEffects(Pokemon p) {
        p.addEffect(new Effect().stat(Stat.SPECIAL_ATTACK, -1));
    }

    @Override
    protected java.lang.String describe() {
        return "использует Confide";
    }
}
