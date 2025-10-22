package moves.status;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.StatusMove;
import ru.ifmo.se.pokemon.Type;

public class RockPolish extends StatusMove {
    public RockPolish() {
        super(Type.ROCK, 0., 100.);
    }

    @Override
    protected void applySelfEffects(Pokemon pokemon) {
        pokemon.setMod(Stat.SPEED, +2);
    }

    @Override
    protected String describe() {
        return "is using Rock Polish. The pokemon raises its Speed by two stages";
    }
}
