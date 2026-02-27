package util;

public class LocalEnvironment {
    public static String getCollectionPath() {
        String fileName = System.getenv("COLLECTION");
        if (fileName == null) {return null;}
        String filePath = "data/" + fileName;
        return filePath;
    }
}
