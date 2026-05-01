import java.util.ArrayList;
import java.util.Collection;

import auth.AuthManager;
import auth.AuthManagerImpl;
import collection.CollectionManager;
import collection.ArrayListManager;
import commands.manager.CommandManager;
import commands.manager.CommandManagerImpl;
import common.exceptions.CollectionLoadException;
import common.exceptions.RuntimeInitException;
import common.models.Entity;
import database.loader.DatabaseLoader;
import database.pool.ConnectionPool;
import database.service.DatabaseService;
import database.writer.DatabaseWriter;
import logging.ServerLogger;
import network.handlers.RequestHandler;
import logging.LoggingManager;
import util.local.LocalEnvironment;

/**
 * Главный класс сервера
 * @author Septyq
 */
public abstract class Server {
    protected final CollectionManager<Entity> collectionManager;
    protected final DatabaseService databaseService;
    private final DatabaseLoader<Entity> databaseLoader;
    private final DatabaseWriter databaseWriter;
    protected final AuthManager authManager;
    protected final LoggingManager logger;
    protected final RequestHandler requestHandler;
    protected final CommandManager commandManager;
    protected final int port;

    public Server(int port) throws RuntimeInitException {
        this.port = port;
        this.logger = new ServerLogger("Server standart logger");

        ConnectionPool.initialize(
            LocalEnvironment.getDatabaseURL(),
            LocalEnvironment.getDatabaseUser(),
            LocalEnvironment.getDatabasePassword(),
            10
        );
        
        this.databaseService = new DatabaseService();
        this.databaseLoader = new DatabaseLoader<Entity>(databaseService);
        this.databaseWriter = new DatabaseWriter(databaseService);

        Collection<Entity> collection = new ArrayList<>();
        this.collectionManager = new ArrayListManager<Entity>(collection, databaseLoader);
        try {
            collectionManager.loadCollection();
        } catch (CollectionLoadException e) {
            logger.error("cannot load collection to memory");
        }
        logger.info("collection loaded to memory");

        this.authManager = new AuthManagerImpl(logger, databaseService, LocalEnvironment.getPepper());
        this.commandManager = new CommandManagerImpl();
        this.requestHandler = new RequestHandler(commandManager, this.authManager);
        
        registerCommands();
    }

    protected abstract void registerCommands();

    public abstract void run();
}
