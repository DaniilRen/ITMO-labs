package commands;

import java.util.ArrayList;
import java.util.List;

import managers.CollectionManager;
import models.Route;


public class Add extends Command {
    private final CollectionManager collectionManager;

    public Add(CollectionManager collectionManager) {
        super("add {element}", "добавить новый элемент в коллекцию");
        this.collectionManager = collectionManager;
    }
    
    public CommandResponse<?> execute(String... args) {
        collectionManager.addToCollection(new Route());
        return new CommandResponse<String>(new ArrayList<>());
    }
}
