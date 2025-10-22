package moves.status;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.Status;
import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.StatusMove;
import ru.ifmo.se.pokemon.Type;
import java.lang.Math;

public class Rest extends StatusMove {
    public Rest() {
        super(Type.PSYCHIC, 0., 100.);
    }

    @Override
    protected void applySelfEffects(Pokemon pokemon) {
        Effect sleepEffect = new Effect().condition(Status.SLEEP).turns(2);
        pokemon.addEffect(sleepEffect);
        double hpDifference = Math.round(pokemon.getStat(Stat.HP) - pokemon.getHP());
        pokemon.setMod(Stat.HP, - (int) hpDifference);
    }

    @Override
    protected String describe() { 
        return "is using Rest. The pokemon sleeps for two turns, completely healing itself.";
    }
}
