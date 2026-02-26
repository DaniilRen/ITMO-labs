package managers;

import java.time.LocalDateTime;
import java.util.ArrayList;

import models.Entity;
import models.Route;
import util.exceptions.CollectionLoadException;


public class CollectionManager {
    ArrayList<Route> collection = new ArrayList<>();
    private final DatabaseManager databaseManager;
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;

    public CollectionManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void loadCollection() throws CollectionLoadException {
        try {
            collection = (ArrayList<Route>) databaseManager.readCollectionFromFile();
            lastInitTime = LocalDateTime.now();
            validateAll();   
        } catch (CollectionLoadException e) {
            throw e;
        }
    }

    private void validateAll() {
        (new ArrayList<>(this.getCollection())).forEach(route -> {
            if (!route.validate()) {
                System.err.println("id=" + route.getId() + " имеет невалидные поля.");
            }
        });
    };

    public void saveCollection(){
        databaseManager.writeCollectionToFile(collection);
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
        collection.add((Route) element);
        Route.IncNextId();
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

    public ArrayList<Route> getCollection() {
        return collection;
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
