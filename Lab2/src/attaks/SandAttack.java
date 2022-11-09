package attaks;

import ru.ifmo.se.pokemon.*;

public class SandAttack extends SpecialMove {

    public SandAttack(Type type, double pow, double acc) {
        super(type, pow, acc);
    }

    @Override
    protected void applyOppEffects(Pokemon p) {
        p.addEffect(new Effect().stat(Stat.ACCURACY, -1));
    }

    @Override
    protected java.lang.String describe() {
        return "использует Sand Attack";
    }
}
