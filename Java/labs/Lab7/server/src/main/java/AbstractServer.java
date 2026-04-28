import java.util.ArrayList;
import java.util.Collection;

import common.exceptions.CollectionLoadException;
import common.exceptions.RuntimeInitException;
import common.models.Entity;

import util.logging.ServerLogger;
import managers.auth.AbstractAuthManager;
import managers.auth.AuthManager;
import managers.collection.AbstractCollectionManager;
import managers.collection.CollectionManager;
import managers.commands.AbstractCommandManager;
import managers.commands.CommandManager;
import managers.database.AbstractDatabaseManager;
import managers.database.PostgresManager;
import network.handlers.RequestHandler;
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
    protected final AbstractAuthManager authManager;
    protected final AbstractLogger logger;
    protected final RequestHandler requestHandler;
    protected final AbstractCommandManager commandManager;


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
        logger.info("collection loaded from database");

        this.authManager = new AuthManager(logger, databaseManager, LocalEnvironment.getPepper());
        this.requestHandler = new RequestHandler(commandManager, this.authManager);
        registerCommands();
    }

    protected abstract void registerCommands();

    public abstract void run();
}
