package moves.status;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Status;

import java.util.Random;

import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.StatusMove;
import ru.ifmo.se.pokemon.Type;

public class Sing extends StatusMove {
    public Sing() {
        super(Type.NORMAL, 0., 55.);
    }

    @Override
    protected void applyOppEffects(Pokemon pokemon) {
        Random rand = new Random();
        Effect sleepEffect = new Effect().condition(Status.SLEEP).turns(rand.nextInt(3)+1);
        pokemon.addEffect(sleepEffect);
    }

    @Override
    protected String describe() {
        return "is using Sing. The pokemon puts the target to sleep";
    }
}
