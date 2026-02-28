package runtime.server;

import java.util.Set;
import java.util.List;
import commands.*;
import managers.*;
import runtime.Runtime;
import util.Request;
import util.Response;
import util.Status;
import util.exceptions.CollectionLoadException;


/**
 * Обрабатывает запросы на исполнение комманд.
 * @author Septyq
 */
public class RemoteRuntime extends Runtime {
    private final DatabaseManager databaseManager;
    private final CommandManager commandManager;

    public RemoteRuntime(String fileName) {
        this.databaseManager = new DatabaseManager(fileName);
        this.commandManager = new CommandManager();
    }


    public void run(String... args) {};

    public void registerCommands() throws CollectionLoadException {
        try {
            CollectionManager collectionManager = new CollectionManager(this.databaseManager);
            collectionManager.loadCollection();

            commandManager.register("help", new Help(commandManager));
            commandManager.register("info", new Info(collectionManager));
            commandManager.register("show", new Show(collectionManager));
            commandManager.register("add", new Add(collectionManager));
            commandManager.register("update", new Update(collectionManager));
            commandManager.register("remove_by_id", new RemoveById(collectionManager));
            commandManager.register("clear", new Clear(collectionManager));
            commandManager.register("save", new Save(collectionManager));
            commandManager.register("execute_script", new ExecuteScript(this));
            commandManager.register("exit", new Exit());
            commandManager.register("remove_lower", new RemoveLower(collectionManager));
            commandManager.register("sort", new Sort(collectionManager));
            commandManager.register("history", new History(commandManager));
            commandManager.register("filter_starts_with_name", new FilterStartsWithName(collectionManager));
            commandManager.register("print_unique_distance", new PrintUniqueDistances(collectionManager));
            commandManager.register("print_field_descending_distance", new PrintFieldDescendingDistance(collectionManager));
        } catch (CollectionLoadException e) {
            throw e;
        }
    }

    public <T> Response<?> proccessRequest(Request<T> request) {
        return (Response<?>) executeCommand(request.getName(), request.getBody());
    }

    private Response<?> executeCommand(String commandName, List<?> args){
        if (validateCommandName(commandName) == false) {
            return new Response<>(List.of("Unknown command"), Status.ERROR);
        }
        Command command = commandManager.getCommands().get(commandName);
        commandManager.addToHistory(command.getName());
        return (Response<?>) command.execute(args);
    };

    private boolean validateCommandName(String command) {
        Set<String> commandsNames = commandManager.getCommands().keySet();
        return commandsNames.contains(command);
    }
}
