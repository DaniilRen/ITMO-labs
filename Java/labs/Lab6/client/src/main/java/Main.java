public class Main {
    public static void main(String[] args) {
        Client client = new NetClient("localhost", 8080);
        client.run();
    }
}
