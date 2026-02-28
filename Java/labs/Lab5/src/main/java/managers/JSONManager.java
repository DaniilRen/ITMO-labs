package managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.Strictness;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    private final Gson writeGson = new GsonBuilder()
        .setPrettyPrinting()
        .serializeNulls()
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
        .create();

    private final Gson readGson = new GsonBuilder()
        .setStrictness(Strictness.LENIENT)
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
        .create();

    public JSONManager(String fileName) {
        this.fileName = fileName;
    }
    
    public Collection<Entity> readCollectionFromFile() throws CollectionLoadException {
        if (fileName == null || fileName.isEmpty()) {
            return new ArrayList<>();
        }
        
        File file = new File(fileName);
        if (!file.exists()) {
            throw new CollectionLoadException("File does not exist");
        }
        if (!file.canRead()) {
            throw new SecurityException("No read permission: " + file.getAbsolutePath());
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder jsonString = readFileContent(reader);
            if (jsonString.length() == 0) {
                jsonString = new StringBuilder("[]");
            }
            return parseValidRoutes(jsonString.toString());
            
        } catch (IOException e) {
            throw new CollectionLoadException("Failed to read file: " + e.getMessage());
        } catch (SecurityException e) {
            throw new CollectionLoadException(e.getMessage());
        }
    }

    private StringBuilder readFileContent(BufferedReader reader) throws IOException {
        StringBuilder jsonString = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty()) {
                jsonString.append(line);
            }
        }
        return jsonString;
    }

    private Collection<Entity> parseValidRoutes(String jsonString) {
        try {
            JsonElement jsonArray = JsonParser.parseString(jsonString);
            if (!jsonArray.isJsonArray()) {
                return new ArrayList<>();
            }
                
            List<Entity> validRoutes = new ArrayList<>();
            for (JsonElement element : jsonArray.getAsJsonArray()) {
                try {
                    Route route = readGson.fromJson(element, Route.class);
                    if (route != null && route.validate()) {
                        validRoutes.add(route);
                    }
                } catch (Exception e) {}
            }
            return validRoutes;
        } catch (JsonParseException e) {
            return new ArrayList<>();
        }
    }

    public void writeCollectionToFile(Collection<? extends Entity> collection) throws CollectionWriteException {
        try (PrintWriter collectionPrintWriter = new PrintWriter(new File(fileName))) {
            collectionPrintWriter.println(writeGson.toJson(collection));
        } catch (IOException exception) {
            throw new CollectionWriteException("Cannot write collection to file");
        }
    }
}
