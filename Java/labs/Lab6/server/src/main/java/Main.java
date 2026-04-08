import common.exceptions.RuntimeInitException;
import common.runtime.Runtime;


public class Main {
    public static void main(String[] args) {
        try {
            Runtime server = new NetServer(8080); 
            server.run();
        } catch (RuntimeInitException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }
}
