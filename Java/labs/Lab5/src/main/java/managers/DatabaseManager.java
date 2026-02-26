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

import models.Route;
import util.LocalDateTimeAdapter;
import util.exceptions.CollectionLoadException;


public class DatabaseManager {
    private final String fileName;
    
    private final Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .serializeNulls()
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
        .create();


    public DatabaseManager(String fileName) {
        if (!(new File(fileName).exists())) {
            fileName = "../" + fileName;
        }
        this.fileName = fileName;
    }
    
    public Collection<Route> readCollectionFromFile() throws CollectionLoadException {
        if (fileName != null && !fileName.isEmpty()) {
            try (var fileReader = new FileReader(fileName)) {
                var collectionType = new TypeToken<ArrayList<Route>>() {}.getType();
                var reader = new BufferedReader(fileReader);

                var jsonString = new StringBuilder();

                String line;
                while((line = reader.readLine()) != null) {
                    line = line.trim();
                    System.out.println("--- "+line);
                    if (!line.equals("")) {
                        jsonString.append(line);
                    }
                }

                if (jsonString.length() == 0) {
                    System.out.println("<Empty JSON file>");
                    jsonString = new StringBuilder("[]");
                }

                ArrayList<Route> collection = gson.fromJson(jsonString.toString(), collectionType);

                return collection;
            } catch (FileNotFoundException exception) {
                throw new CollectionLoadException("File not found");
            } catch (NoSuchElementException exception) {
                throw new CollectionLoadException("File is empty");
            } catch (JsonParseException exception) {
                throw new CollectionLoadException("Invalid collection in file");
            } catch (IllegalStateException | IOException exception) {
                throw new CollectionLoadException("Unknown error");
            }
    }
        return new ArrayList<>();
    }


    public void writeCollectionToFile(Collection<Route> collection) {
        try (PrintWriter collectionPrintWriter = new PrintWriter(new File(fileName))) {
            collectionPrintWriter.println(gson.toJson(collection));
            
        } catch (IOException exception) {
            System.err.println("Unable to load target file");
        }
    }
}
