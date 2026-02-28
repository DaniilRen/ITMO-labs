package commands;

import managers.CollectionManager;
import util.Response;
import util.Status;

import java.time.LocalDateTime;
import java.util.List;


/**
 * Команда 'info'. Выводит информацию о коллекции.
 * @author Septyq
 */
public class Info extends Command {
    private final CollectionManager collectionManager;

    public Info(CollectionManager collectionManager) {
        super("info", "вывести информацию о коллекции");
        this.collectionManager = collectionManager;
    }

    public Response<?> execute(List<?> args) {
        if (args.size() > 0) { return new Response<>(List.of("Invalid argument number"), Status.ERROR); }
        
        LocalDateTime lastInitTime = collectionManager.getLastInitTime();
        String lastInitTimeString = (lastInitTime == null) ? "no collection init (load) occured in current session" :
            lastInitTime.toLocalDate().toString() + " " + lastInitTime.toLocalTime().toString();

        LocalDateTime lastSaveTime = collectionManager.getLastSaveTime();
        String lastSaveTimeString = (lastSaveTime == null) ? "no save occured in current session" :
            lastSaveTime.toLocalDate().toString() + " " + lastSaveTime.toLocalTime().toString();

        Response<String> response = new Response<>();
        response.put("Collection info:");
        response.put(String.format("Collection Type (Class): %s", collectionManager.getCollectionType()));
        response.put(String.format("Collection size: %d", collectionManager.getCollectionSize()));
        response.put(String.format("Last saved: %s", lastSaveTimeString));
        response.put(String.format("Last initialized: %s", lastInitTimeString));

        return response;
        
    }
}
