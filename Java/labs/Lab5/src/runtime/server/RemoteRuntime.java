package runtime.server;

import java.util.Set;
import java.util.List;
import commands.Add;
import commands.Command;
import commands.Help;
import managers.CollectionManager;
import managers.CommandManager;
import managers.DatabaseManager;
import runtime.Runtime;
import util.Request;
import util.Response;
import util.Payload;
import util.Status;


public class RemoteRuntime extends Runtime {
    private final String fileName;
    private final DatabaseManager databaseManager;
    private final CollectionManager collectionManager;
    private final CommandManager commandManager;

    public RemoteRuntime(String fileName) {
        this.fileName = fileName;
        this.databaseManager = new DatabaseManager(fileName);
        this.collectionManager = new CollectionManager(databaseManager);
        this.commandManager = new CommandManager();
        registerCommands();
    }

    public void run(String... args) {};

    private void registerCommands() {
        commandManager.register("help", new Help(commandManager));
        commandManager.register("add", new Add(collectionManager));
    }

    public <T> Response<?> proccessRequest(Request<T> request) {
        return (Response<?>) executeCommand(request.getName(), request.getBody());
    }

    private Payload<?> executeCommand(String commandName, List<?> args){
        if (validateCommandName(commandName) == false) {
            return new Response<>(Status.ERROR);
        }
        Command command = commandManager.getCommands().get(commandName);
        commandManager.addToHistory(command.getName());
        return command.execute(args);
    };

    private boolean validateCommandName(String command) {
        Set<String> commandsNames = commandManager.getCommands().keySet();
        return commandsNames.contains(command);
    }
}
