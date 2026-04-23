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
import util.logging.ServerLogger;
import managers.auth.AbstractAuthManager;
import managers.auth.AuthManager;
import managers.collection.AbstractCollectionManager;
import managers.collection.CollectionManager;
import managers.commands.AbstractCommandManager;
import managers.commands.CommandManager;
import managers.database.AbstractDatabaseManager;
import managers.database.PostgresManager;
import util.logging.AbstractLogger;
import util.database.handlers.database.PostgresHandler;
import util.local.LocalEnvironment;

/**
 * Абстрактный сервер
 * @author Septyq
 */
public abstract class AbstractServer {
    protected final AbstractCollectionManager<Entity> collectionManager;
    protected final AbstractDatabaseManager databaseManager;
    protected final AbstractCommandManager commandManager;
    protected final AbstractAuthManager authManager;
    protected final AbstractLogger logger;


    public AbstractServer() throws RuntimeInitException {
        this.logger = new ServerLogger("Server standart logger");
        this.commandManager = new CommandManager();
        this.databaseManager = new PostgresManager(
            new PostgresHandler(),
            LocalEnvironment.getDatabaseURL(),
            LocalEnvironment.getDatabaseUser(),
            LocalEnvironment.getDatabasePassword()
        );

        Collection<Entity> collection = new ArrayList<>();
        try {
            collection = databaseManager.readCollection();
        } catch (CollectionLoadException e) {
            throw new RuntimeInitException(e.getMessage());
        }
        this.collectionManager = new CollectionManager<Entity>(collection);
        registerCommands();
        logger.info("collection loaded from database");


        this.authManager = new AuthManager(logger, databaseManager, LocalEnvironment.getPepper());
    }

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

    protected boolean validateCommandName(String command) {
        Set<String> commandsNames = commandManager.getCommands().keySet();
        return commandsNames.contains(command);
    }

    protected abstract void registerCommands();

    protected abstract Response<?> processRequest(Request request);

    public abstract void run();
}
