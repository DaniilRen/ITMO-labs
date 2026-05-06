package pokemons;

import moves.special.FireBlast;
import moves.special.ShadowBall;
import moves.special.EnergyBall;
import moves.status.Confide;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Azelf extends Pokemon {
    public Azelf(String name, int level) {
        super(name, level);
        setType(Type.PSYCHIC);
        setStats(75, 125, 70, 125, 70, 115); 
        setMove(new ShadowBall(), new EnergyBall(), new Confide(), new FireBlast());
    }
}
