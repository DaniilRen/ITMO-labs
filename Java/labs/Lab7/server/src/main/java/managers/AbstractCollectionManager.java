package managers;

import common.transfer.Status;

import java.time.LocalDateTime;
import java.util.Collection;

import common.models.Entity;


/**
 * Абстрактный класс для манипуляций элементами коллекции.
 * @author Septyq
 * @param <T> элемент коллекции
 */
public abstract class AbstractCollectionManager<T extends Entity> {
    abstract public Collection<T> getCollection();
    abstract public int getCollectionSize();
    abstract public String getCollectionType();
    abstract public Status clearCollection();
    abstract public Status saveCollection(AbstractDatabaseManager fileManager);
    abstract public Status addToCollection(T element);
    abstract public Status removeFromCollection(T element);
    abstract public Status updateById(int id, T newElement);
    abstract public boolean checkExist(int id);
    abstract public T getById(int id);
    abstract public T getByValue(T targetElement);
    abstract public LocalDateTime getLastInitTime();
    abstract public LocalDateTime getLastSaveTime();
}
