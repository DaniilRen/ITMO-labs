package managers;

import java.util.Collection;

import common.models.Entity;
import common.exceptions.CollectionLoadException;
import common.exceptions.CollectionWriteException;


/**
 * Асбрактный класс для сохранения и загрузки коллекции из файла.
 * @author Septyq
 */
public interface FileManager {
    Collection<Entity> readCollectionFromFile() throws CollectionLoadException;
    void writeCollectionToFile(Collection<? extends Entity> collection) throws CollectionWriteException;
}
