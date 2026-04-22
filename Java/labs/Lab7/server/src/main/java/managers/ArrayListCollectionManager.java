package managers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import common.models.Entity;
import common.transfer.Status;


/**
 * Класс для манипуляций элементами коллекции вида Arraylist.
 * @param <T> создаваемый объект
 * @author Septyq
 */
public class ArrayListCollectionManager<T extends Entity> implements CollectionManager<T> {
    private Collection<T> collection = new ArrayList<>();
    private LocalDateTime lastInitTime = LocalDateTime.now();
    private LocalDateTime lastSaveTime;

    public ArrayListCollectionManager(Collection<T> collection) {
        this.collection = collection;
    }


    public Status saveCollection(SourceManager fileManager){
        try {
            fileManager.writeCollection(collection);
            this.lastSaveTime = LocalDateTime.now();
            return Status.OK;
        } catch (Exception e) {
            return Status.ERROR;
        }
    }


    public Status clearCollection() {
        try {
            collection.clear();
            return Status.OK;
        } catch (Exception e) {
            return Status.ERROR;
        }

    }

    public Status removeFromCollection(T element) {
        try {
            collection.remove(element);
            return Status.OK;
        } catch (Exception e) {
            return Status.ERROR;
        }

    }

    public Status addToCollection(T element) {
        try {
            collection.add(element);
            return Status.OK;
        } catch (Exception e) {
            return Status.ERROR;
        }
    }

    public Status updateById(int id, T newElement) {
        Status status = Status.ERROR;
        Iterator<T> iterator = this.collection.iterator();
        while (iterator.hasNext()) {
            T route = iterator.next();
            if (route.getId() == id) {
                iterator.remove();
                this.collection.add(newElement);
                status = Status.OK;
                break;
            }
        }
        return status;
    }


    public boolean checkExist(int id) {
        for (Entity element : collection) {
            if (element.getId() == id) return true;
        }
        return false;
    }

    public T getById(int id) {
        for (T element : collection) {
        if (element.getId() == id) return element;
        }
        return null;
    }

    public T getByValue(T targetElement) {
        for (T element : collection) {
            if (element.equals(targetElement)) return element;
        }
        return null;
    }

    public Collection<T> getCollection() {
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
        for (T entity: collection) {
            result.append(entity + "\n\n");
        }
        return result.toString();
    }
}
