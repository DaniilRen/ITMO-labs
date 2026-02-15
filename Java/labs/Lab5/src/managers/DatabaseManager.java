package managers;

import java.io.File;
import java.util.Collection;
import models.Entity;


public class DatabaseManager {
    private final String fileName;

    public DatabaseManager(String fileName) {
        if (!(new File(fileName).exists())) {
            fileName = "../" + fileName;
        }
        this.fileName = fileName;
    }
    
    public Collection<Entity> readCollectionFromFile() {}

    public void writeCollectionToFile() {};
}
