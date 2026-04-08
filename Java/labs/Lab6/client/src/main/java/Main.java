import common.runtime.Runtime;

public class Main {
    public static void main(String[] args) {
        Runtime client = new NetClient("localhost", 8080);
        client.run();
    }
}
