package managers;

import java.util.Collection;

import models.Entity;
import util.exceptions.CollectionLoadException;
import util.exceptions.CollectionWriteException;

public interface FileManager {
    Collection<Entity> readCollectionFromFile() throws CollectionLoadException;
    void writeCollectionToFile(Collection<? extends Entity> collection) throws CollectionWriteException;
}
