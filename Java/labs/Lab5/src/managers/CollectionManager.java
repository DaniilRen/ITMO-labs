package managers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import models.Entity;


public class CollectionManager {
    List<Entity> collection = new ArrayList<>();
    private final DatabaseManager databaseManager;
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;

    public CollectionManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;

        loadCollection();
        validateAll();
    }

    private void loadCollection(){
        collection = (ArrayList<Entity>) databaseManager.readCollectionFromFile();
        lastInitTime = LocalDateTime.now();
        validateAll();
    }

    private void validateAll(){}

    public void saveCollection(){
        databaseManager.writeCollectionToFile();
        this.lastSaveTime = LocalDateTime.now();
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

    public Entity getById(int id) {
        for (Entity element : collection) {
        if (element.getId() == id) return element;
        }
        return null;
    }

    public Entity getByValue(Entity targetElement) {
        for (Entity element : collection) {
            if (element.equals(targetElement)) return element;
        }
        return null;
    }

    public String getCollectionType() {
        return collection.getClass().getName();
    }

    public int getCollectionSize() {
        return collection.size();
    }

    public LocalDateTime getLastInitTime() {
        return this.lastInitTime;
    }

    public LocalDateTime getLastSaveTime() {
        return this.lastSaveTime;
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
