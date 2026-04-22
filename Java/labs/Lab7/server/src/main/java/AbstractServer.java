import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import commands.Command;
import common.exceptions.CollectionLoadException;
import common.exceptions.RuntimeInitException;
import common.models.Entity;
import common.transfer.Status;
import common.transfer.request.Request;
import common.transfer.request.standart.StandartRequest;
import common.transfer.response.Response;
import logging.ServerLogger;
import managers.ArrayListCollectionManager;
import managers.CollectionManager;
import managers.CommandManager;
import managers.DefaultCommandManager;
import managers.SourceManager;
import managers.PostgresManager;
import logging.LoggerBlueprint;
import util.LocalEnvironment;


public abstract class AbstractServer {
    protected final CollectionManager<Entity> collectionManager;
    protected final SourceManager fileManager;
    protected final CommandManager commandManager;
    protected final LoggerBlueprint logger;


    public AbstractServer() throws RuntimeInitException {
        this.commandManager = new DefaultCommandManager();
        this.logger = new ServerLogger("Server log");

        this.fileManager = new PostgresManager(
            LocalEnvironment.getDatabaseURL(),
            LocalEnvironment.getDatabaseUser(),
            LocalEnvironment.getDatabasePassword()
        );

        Collection<Entity> collection = new ArrayList<>();
        try {
            collection = fileManager.readCollection();
        } catch (CollectionLoadException e) {
            throw new RuntimeInitException(e.getMessage());
        }
        this.collectionManager = new ArrayListCollectionManager<Entity>(collection);
        registerCommands();
        logger.info("collection loaded from database");
    }

    protected boolean validateCommandName(String command) {
        Set<String> commandsNames = commandManager.getCommands().keySet();
        return commandsNames.contains(command);
    }

    protected abstract void registerCommands();

    protected abstract Response<?> processRequest(Request request);

    protected Response<?> executeCommand(StandartRequest request) {
        String commandName = request.getName();
        if (!validateCommandName(commandName)) {
            return new Response<>(List.of("Unknown command"), Status.ERROR);
        }
        Command<?> command = commandManager.getCommands().get(commandName);
        commandManager.addToHistory(command.getAttribute().getName());
        
        @SuppressWarnings("unchecked")
        Command<StandartRequest> typedCommand = (Command<StandartRequest>) command;
        Response<?> response = typedCommand.execute(request);
        return response;
    };

    public abstract void run();
}
