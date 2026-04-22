public class Main {
    public static void main(String[] args) {
        AbstractClient client = new NetClient("localhost", 9001);
        client.run();
    }
}
