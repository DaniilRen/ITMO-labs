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
public interface CollectionManager<T extends Entity> {
    Collection<T> getCollection();
    int getCollectionSize();
    String getCollectionType();
    Status clearCollection();
    Status saveCollection(SourceManager fileManager);
    Status addToCollection(T element);
    Status removeFromCollection(T element);
    Status updateById(int id, T newElement);
    boolean checkExist(int id);
    T getById(int id);
    T getByValue(T targetElement);
    LocalDateTime getLastInitTime();
    LocalDateTime getLastSaveTime();
}
