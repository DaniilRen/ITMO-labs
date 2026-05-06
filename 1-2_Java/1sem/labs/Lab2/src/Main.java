import ru.ifmo.se.pokemon.Battle;
import pokemons.*;

public class Main {
    public static void main(String[] args) {
        Battle battle = new Battle();

        // Pokemons
        Cleffa cleffa = new Cleffa("Cleffa", 1);
        Clefairy clefairy = new Clefairy("Clefairy", 1);
        Clefable clefable = new Clefable("Clefable", 1);
        Azelf azelf = new Azelf("Azelf", 1);
        Numel numel = new Numel("Numel", 1);
        Camerupt camerupt = new Camerupt("Camerupt", 1);

        // Team 1
        battle.addAlly(numel);
        battle.addAlly(cleffa);
        battle.addAlly(clefable);

        // Team 2
        battle.addFoe(azelf);
        battle.addFoe(clefairy);
        battle.addFoe(camerupt);
        
        // Start the battle
        battle.go();
    }
}
