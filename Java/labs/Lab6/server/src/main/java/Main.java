import common.exceptions.RuntimeInitException;


public class Main {
    public static void main(String[] args) {
        try {
            Server server = new NetServer(8080); 
            server.run();
        } catch (RuntimeInitException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }
}
