package pokemons;

import moves.physical.FlameCharge;
import moves.status.DoubleTeam;
import moves.status.Rest;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Numel extends Pokemon {
    public Numel(String name, int level) {
        super(name, level);
        setType(Type.FIRE, Type.GROUND);
        setStats(60, 60, 40, 65, 45, 35);
        setMove(new FlameCharge(), new Rest(), new DoubleTeam());
    }
}
