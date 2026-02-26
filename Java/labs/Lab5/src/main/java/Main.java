import runtime.client.LocalRuntime;
import runtime.server.RemoteRuntime;


public class Main {
    public static void main(String[] args) {
        String fileName = System.getenv("COLLECTION");
        if (fileName == null) {
            System.out.println("Введите имя загружаемого файла как аргумент командной строки");
            System.exit(0);
        }
        String filePath = "data/" + fileName;
        System.out.println("Collection path: " + filePath);

        RemoteRuntime remoteRuntime = new RemoteRuntime(filePath);
        LocalRuntime localRuntime = new LocalRuntime(remoteRuntime);

        localRuntime.run("interactive");
    }
}
