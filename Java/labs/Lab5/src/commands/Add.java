package commands;

import java.util.ArrayList;

import managers.CollectionManager;
import models.Route;


public class Add extends Command {
    private final CollectionManager collectionManager;

    public Add(CollectionManager collectionManager) {
        super("add {element}", "добавить новый элемент в коллекцию");
        this.collectionManager = collectionManager;
    }
    
    public ArrayList<?> execute(String... args) {
        return collectionManager.addToCollection(new Route())
    }
}
