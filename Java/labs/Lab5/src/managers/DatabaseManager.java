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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

import models.Entity;
import models.Route;


public class DatabaseManager {
    private final String fileName;
    
    private final Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .serializeNulls()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
        .create();


    public DatabaseManager(String fileName) {
        if (!(new File(fileName).exists())) {
            fileName = "../" + fileName;
        }
        this.fileName = fileName;
    }
    
    public Collection<Entity> readCollectionFromFile() {
        if (fileName != null && !fileName.isEmpty()) {
            try (var fileReader = new FileReader(fileName)) {
                var collectionType = new TypeToken<PriorityQueue<Product>>() {}.getType();
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

                ArrayList<Route> collection = gson.fromJson(jsonString.toString(), collectionType);

                console.println("Коллекция успешна загружена!");
                return collection;
            } catch (FileNotFoundException exception) {
                console.printError("Загрузочный файл не найден!");
            } catch (NoSuchElementException exception) {
                console.printError("Загрузочный файл пуст!");
            } catch (JsonParseException exception) {
                console.printError("В загрузочном файле не обнаружена необходимая коллекция!");
            } catch (IllegalStateException | IOException exception) {
                console.printError("Непредвиденная ошибка!");
            }
    } else {
        console.printError("Аргумент командной строки с загрузочным файлом не найден!");
    }
        return new ArrayList<>();
    }


    public void writeCollectionToFile() {
        try (PrintWriter collectionPrintWriter = new PrintWriter(new File(fileName))) {
            collectionPrintWriter.println(gson.toJson(collection));
            
        } catch (IOException exception) {
            console.printError("Загрузочный файл не может быть открыт!");
        }
    }
}
