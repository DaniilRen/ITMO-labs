import common.exceptions.RuntimeInitException;
import util.LocalEnvironment;

public class Main {
    public static void main(String[] args) {
        String filePath = LocalEnvironment.getCollectionPath();
        if (filePath == null) {
            System.exit(0);
        }

        try {
            Server server = new NetServer(filePath, 8080); 
            server.run();
        } catch (RuntimeInitException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }
}
