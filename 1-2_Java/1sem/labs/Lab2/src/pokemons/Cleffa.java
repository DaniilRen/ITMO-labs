package pokemons;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;
import moves.status.Rest;
import moves.status.Sing;

public class Cleffa extends Pokemon {
    public Cleffa(String name, int level) {
        super(name, level);
        setType(Type.FAIRY);
        setStats(50, 25, 28, 45, 55, 15);
        setMove(new Sing(), new Rest());
    }
}
