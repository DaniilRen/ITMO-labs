package moves.physical;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.Type;

public class FlameCharge extends PhysicalMove {
    public FlameCharge() {
        super(Type.FIRE, 50., 100.);
    }

    @Override
    protected void applySelfEffects(Pokemon pokemon) {
        pokemon.setMod(Stat.SPEED, +1);
    }

    @Override
    protected void applyOppDamage(Pokemon pokemon, double damage) {
        super.applyOppDamage(pokemon, damage);
    }

    @Override
    protected String describe() {
        return "is using Flame charge. The pokemon deals damage and raises its Speed by one stage";
    }
} 
