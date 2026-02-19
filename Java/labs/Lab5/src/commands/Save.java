package commands;

import managers.CollectionManager;

import util.Response;
import util.Status;

import java.util.List;

public class Save extends Command {
    private final CollectionManager collectionManager;

    public Save(CollectionManager collectionManager) {
        super("save", "сохранить коллекцию в файл");
        this.collectionManager = collectionManager; 
    }
    
    public Response<?> execute(List<?> args) {
        if (args.size() > 0) {
            return new Response<>(Status.ERROR);
        }
        collectionManager.saveCollection();
        return new Response<>();
    }
}
