package runtime.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.text.html.parser.Entity;

import commands.Add;
import commands.Command;
import commands.Help;
import managers.CollectionManager;
import managers.CommandManager;
import managers.DatabaseManager;
import runtime.Runtime;
import util.Request;
import util.Response;
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

    private ArrayList<?> executeCommand(String commandName, String argument){
        Command command = commandManager.getCommands().get(commandName);
        commandManager.addToHistory(command.getName());
        return command.execute(argument);
    };

    public Response proccessRequest(Request request) {
        String commandName = request.name();
        String argument = request.argument();

        if (validateCommand(commandName) == false) { 
            return makeResponse(new ArrayList<>(), Status.ERROR);
        }
        ArrayList<?> responseBody = executeCommand(commandName, argument);
        return makeResponse(responseBody, Status.OK);
    }

    private boolean validateCommand(String command) {
        Set<String> commandsNames = commandManager.getCommands().keySet();
        return commandsNames.contains(command);
    }

    private Response makeResponse(ArrayList<?> body, Status status) {
        return new Response(body, status);
    }
}
