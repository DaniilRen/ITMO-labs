package managers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

import models.Entity;
import models.Route;
import util.exceptions.CollectionLoadException;


/**
 * Оперирует коллекцией.
 * @author Septyq
 */
public class CollectionManager {
    private ArrayList<Route> collection = new ArrayList<>();
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
            Route.updateNextId(this);
        } catch (CollectionLoadException e) {
            throw e;
        }
    }

    private void validateAll() throws CollectionLoadException {
        for (Route route : this.getCollection()) {
            if (!route.validate()) {
                throw new CollectionLoadException("element id=" + route.getId() + " has invalid fields");
            }
        }
    }
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

    public boolean updateById(int id, Route newRoute) {
        newRoute.setId(id);
        boolean updated = false;
        Iterator<Route> iterator = this.collection.iterator();
        while (iterator.hasNext()) {
            Route route = iterator.next();
            if (route.getId() == id) {
                iterator.remove();
                this.collection.add(newRoute);
                updated = true;
                break;
            }
        }
        return updated;
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
