package managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

import models.Entity;
import models.Route;
import util.LocalDateTimeAdapter;
import util.exceptions.CollectionLoadException;
import util.exceptions.CollectionWriteException;


/**
 * Использует файл для сохранения и загрузки коллекции.
 * @author Septyq
 */
public class JSONManager implements FileManager {
    private final String fileName;

    private final Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .serializeNulls()
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
        .create();

    public JSONManager(String fileName) {
        this.fileName = fileName;
    }

    
    public Collection<Entity> readCollectionFromFile() throws CollectionLoadException {
        if (fileName != null && !fileName.isEmpty()) {
            try (var fileReader = new FileReader(fileName)) {
                File file = new File(fileName); 
                if (!(file.exists())) {
                    throw new FileNotFoundException("File does not exist");
                }
                if (!file.canRead()) {
                    throw new SecurityException("No read permission for file: " + file.getAbsolutePath());
                }

                if (!file.canWrite()) {
                    throw new SecurityException("No write permission for file: " + file.getAbsolutePath());
                }
                
                var collectionType = new TypeToken<ArrayList<Route>>() {}.getType();
                var reader = new BufferedReader(fileReader);

                var jsonString = new StringBuilder();

                String line;
                while((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.equals("")) {
                        jsonString.append(line);
                    }
                }

                if (jsonString.length() == 0) {
                    jsonString = new StringBuilder("[]");
                }

                Collection<Entity> collection = gson.fromJson(jsonString.toString(), collectionType);

                return collection;
            } catch (FileNotFoundException exception) {
                throw new CollectionLoadException("File not found");
            } catch (NoSuchElementException exception) {
                throw new CollectionLoadException("File is empty");
            } catch (JsonParseException exception) {
                throw new CollectionLoadException("File parsing error");
            } catch (IllegalStateException | IOException exception) {
                throw new CollectionLoadException("Unknown error");
            } catch (SecurityException e) {
                throw new CollectionLoadException(e.getMessage());
            }
    }
        return new ArrayList<>();
    }


    public void writeCollectionToFile(Collection<? extends Entity> collection) throws CollectionWriteException {
        try (PrintWriter collectionPrintWriter = new PrintWriter(new File(fileName))) {
            collectionPrintWriter.println(gson.toJson(collection));
        } catch (IOException exception) {
            throw new CollectionWriteException("Cannot write collection to file");
        }
    }
}
