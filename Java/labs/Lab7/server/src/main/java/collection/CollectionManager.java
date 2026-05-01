package collection;

import common.transfer.Status;

import java.time.LocalDateTime;
import java.util.Collection;

import common.exceptions.CollectionLoadException;
import common.models.Entity;


/**
 * Класс для работы с коллекцией в памяти.
 * @author Septyq
 * @param <T> элемент коллекции
 */
public abstract class CollectionManager<T extends Entity> {
    abstract public void loadCollection() throws CollectionLoadException;
    abstract public void setCollection(Collection<T> collection);
    abstract public Collection<T> getCollection();
    abstract public int getCollectionSize();
    abstract public String getCollectionType();
    abstract public Status clearCollection();
    abstract public Status addToCollection(T element);
    abstract public Status removeFromCollection(T element);
    abstract public Status updateById(int id, T newElement);
    abstract public boolean checkExist(int id);
    abstract public T getById(int id);
    abstract public T getByValue(T targetElement);
    abstract public LocalDateTime getLastInitTime();
    abstract public LocalDateTime getLastSaveTime();
}
