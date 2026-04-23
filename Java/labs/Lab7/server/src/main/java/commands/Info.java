package commands;

import common.transfer.request.standart.StandartRequest;
import common.transfer.response.Response;
import managers.collection.AbstractCollectionManager;

import java.time.LocalDateTime;

import common.models.Entity;


/**
 * Команда 'info'. Выводит информацию о коллекции.
 * @author Septyq
 */
public class Info extends Command<StandartRequest> {
    private static final long serialVersionUID = 134654192L;

    private final AbstractCollectionManager<Entity> collectionManager;

    public Info(AbstractCollectionManager<Entity> collectionManager) {
        super(new CommandAttribute(
            "info",
            "вывести информацию о коллекции",
            StandartRequest.class
            ));
        this.collectionManager = collectionManager;
    }

    public Response<?> execute(StandartRequest request) {
        LocalDateTime lastInitTime = collectionManager.getLastInitTime();
        String lastInitTimeString = (lastInitTime == null) ? "invalid collection load time" :
            lastInitTime.toLocalDate().toString() + " " + lastInitTime.toLocalTime().toString();

        LocalDateTime lastSaveTime = collectionManager.getLastSaveTime();
        String lastSaveTimeString = (lastSaveTime == null) ? "no save occured in current session" :
            lastSaveTime.toLocalDate().toString() + " " + lastSaveTime.toLocalTime().toString();

        Response<String> response = new Response<>();
  
        response.put("<Collection info:>");
        response.put(String.format("Collection Type (Class): %s", collectionManager.getCollectionType()));
        response.put(String.format("Collection size: %d", collectionManager.getCollectionSize()));
        response.put(String.format("Last saved: %s", lastSaveTimeString));
        response.put(String.format("Last initialized: %s", lastInitTimeString));

        return response;
        
    }
}
