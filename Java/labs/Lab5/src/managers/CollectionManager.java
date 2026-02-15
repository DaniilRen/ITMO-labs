package managers;

import java.util.ArrayList;
import java.util.List;

import models.Entity;


public class CollectionManager {
    List<Entity> collection = new ArrayList<>();
    private final DatabaseManager databaseManager;

    public CollectionManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;

        loadCollection();
        validateAll();
    }

    private void loadCollection(){
        collection = (ArrayList<Entity>) databaseManager.readCollectionFromFile();
        validateAll();
    }

    private void validateAll(){}

    public void saveCollection(){
        databaseManager.writeCollectionToFile();
    }


    public ArrayList<?> clearCollection() {
        collection.clear();
        return new ArrayList<>();
    }

    public ArrayList<?> removeFromCollection(Entity element) {
        collection.remove(element);
        return new ArrayList<>();
    }

    public ArrayList<?> addToCollection(Entity element) {
        collection.add(element);
        return new ArrayList<>();
    }

    public boolean checkExist(int id) {
        for (Entity element : collection) {
            if (element.getId() == id) return true;
        }
        return false;
    }

    public Entity getByValue(Entity targetElement) {
        for (Entity element : collection) {
            if (element.equals(targetElement)) return element;
        }
        return null;
    }

    @Override
    public String toString() {
        if (collection.isEmpty()) {
            return "Collection is empty";
        }

        StringBuilder result = new StringBuilder();
        for (Entity entity: collection) {
            result.append(entity + "\n\n");
        }
        return result.toString();
    }
}
