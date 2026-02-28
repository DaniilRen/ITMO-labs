package runtime.server;

import java.util.Set;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import commands.*;
import managers.*;
import models.Entity;
import runtime.Runtime;
import util.Request;
import util.Response;
import util.Status;
import util.exceptions.CollectionLoadException;
import util.exceptions.RuntimeInitException;


/**
 * Обрабатывает запросы на исполнение комманд.
 * @author Septyq
 */
public class RemoteRuntime extends Runtime {
    private final FileManager fileManager;
    private final CollectionManager<Entity> collectionManager;
    private final CommandManager commandManager;

    public RemoteRuntime(String fileName) throws RuntimeInitException {
        this.commandManager = new DefaultCommandManager();
        this.fileManager = new JSONManager(fileName);

        Collection<Entity> collection = new ArrayList<>();
        try {
            collection = fileManager.readCollectionFromFile();
        } catch (CollectionLoadException e) {
            throw new RuntimeInitException(e.getMessage());
        }
        
        this.collectionManager = new ArrayListCollectionManager<Entity>(collection);
    }


    public void run(String... args) {};


    public void registerCommands() {
        commandManager.register("help", new Help(commandManager));
        commandManager.register("info", new Info(collectionManager));
        commandManager.register("show", new Show(collectionManager));
        commandManager.register("add", new Add(collectionManager));
        commandManager.register("update", new Update(collectionManager));
        commandManager.register("remove_by_id", new RemoveById(collectionManager));
        commandManager.register("clear", new Clear(collectionManager));
        commandManager.register("save", new Save(collectionManager, fileManager));
        commandManager.register("execute_script", new ExecuteScript(this));
        commandManager.register("exit", new Exit());
        commandManager.register("remove_lower", new RemoveLower(collectionManager));
        commandManager.register("sort", new Sort(collectionManager));
        commandManager.register("history", new History(commandManager));
        commandManager.register("filter_starts_with_name", new FilterStartsWithName(collectionManager));
        commandManager.register("print_unique_distance", new PrintUniqueDistances(collectionManager));
        commandManager.register("print_field_descending_distance", new PrintFieldDescendingDistance(collectionManager));
    }

    public <T> Response<?> proccessRequest(Request<T> request) {
        return (Response<?>) executeCommand(request.getName(), request.getBody());
    }

    private Response<?> executeCommand(String commandName, List<?> args){
        if (!validateCommandName(commandName)) {
            return new Response<>(List.of("Unknown command"), Status.ERROR);
        }
        if (commandName.equals("execute_script")) {
            if (commandManager.checkRecursion((String) args.get(0))) {
                return new Response<>(List.of("Script has recursion"), Status.ERROR);
            }
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
