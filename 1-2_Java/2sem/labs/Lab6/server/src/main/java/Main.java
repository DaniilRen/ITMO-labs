import common.exceptions.RuntimeInitException;


public class Main {
    public static void main(String[] args) {
        try {
            AbstractServer server = new NetServer(9001); 
            server.run();
        } catch (RuntimeInitException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }
}
