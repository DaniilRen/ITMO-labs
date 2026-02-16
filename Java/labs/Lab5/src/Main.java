import runtime.client.LocalRuntime;
import runtime.server.RemoteRuntime;


public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Введите имя загружаемого файла как аргумент командной строки");
            System.exit(0);
        }

        RemoteRuntime remoteRuntime = new RemoteRuntime(args[0]);
        LocalRuntime localRuntime = new LocalRuntime(remoteRuntime);

        localRuntime.run("interactuve");
    }
}
