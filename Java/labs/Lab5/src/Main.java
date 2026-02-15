import commands.*;
import managers.CollectionManager;
import managers.CommandManager;
import managers.DatabaseManager;
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


        String fileName = args[0];
        DatabaseManager databaseManager = new DatabaseManager(fileName);

        CollectionManager collectionManager = new CollectionManager(databaseManager);

        CommandManager commandManager = new CommandManager();
        commandManager.register("add", new Add(collectionManager));

        localRuntime.run("interactuve");
    }
}
