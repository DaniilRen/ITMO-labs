import commands.*;
import managers.CollectionManager;
import managers.CommandManager;
import managers.DatabaseManager;
import runtime.client.LocalRuntime;
import runtime.server.RemoteRuntime;
import util.console.*;


public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Введите имя загружаемого файла как аргумент командной строки");
            System.exit(0);
        }

        LocalRuntime localRuntime = new LocalRuntime();
        RemoteRuntime remoteRuntime = new RemoteRuntime(args[0]);

        

        String fileName = args[0];
        DatabaseManager databaseManager = new DatabaseManager(fileName, console);

        CollectionManager collectionManager = new CollectionManager(console, databaseManager);

        CommandManager commandManager = new CommandManager();
        commandManager.register("add", new Add(collectionManager));

        currentRuntime.interactiveMode();
    }
}
