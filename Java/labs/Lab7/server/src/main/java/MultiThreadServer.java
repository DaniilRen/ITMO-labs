import java.io.IOException;
import commands.*;
import network.MultiThreadNetwork;
import network.callback.CommonCallback;
import util.database.api.PostgresApi;
import util.local.LocalEnvironment;
import common.exceptions.RuntimeInitException;
import managers.database.PostgresManager;


public class MultiThreadServer extends AbstractServer {
    private final MultiThreadNetwork networkManager;

    public MultiThreadServer(int port) throws RuntimeInitException {
        super(port, new PostgresManager(
            new PostgresApi(),
            LocalEnvironment.getDatabaseURL(),
            LocalEnvironment.getDatabaseUser(),
            LocalEnvironment.getDatabasePassword()
        ));
        this.networkManager = new MultiThreadNetwork(port, logger, 50);
        this.networkManager.setMessageCallback(new CommonCallback(requestHandler, logger));
    }

    public void run() {
        try {
            networkManager.connect();
            boolean running = true;
            logger.info("Server started on port " + port);
            while (running) {
                Thread.sleep(1000);
            }
        } catch (IOException e) {
            logger.error("Server failed to start: ", e);
        } catch (InterruptedException e) {
            logger.info("Server stopped");
            Thread.currentThread().interrupt();
        }
    }

    protected void registerCommands() {
        commandManager.register("help", new Help(commandManager));
        commandManager.register("info", new Info(collectionManager));
        commandManager.register("show", new Show(collectionManager));
        commandManager.register("add", new Add(databaseManager, collectionManager));
        commandManager.register("update", new Update(databaseManager, collectionManager));
        commandManager.register("remove_by_id", new RemoveById(collectionManager));
        commandManager.register("clear", new Clear(collectionManager));
        commandManager.register("remove_lower", new RemoveLower(collectionManager));
        commandManager.register("sort", new Sort(collectionManager));
        commandManager.register("history", new History(commandManager));
        commandManager.register("filter_starts_with_name", new FilterStartsWithName(collectionManager));
        commandManager.register("print_unique_distance", new PrintUniqueDistances(collectionManager));
        commandManager.register("print_field_descending_distance", new PrintFieldDescendingDistance(collectionManager));
        commandManager.register("register", new Register(authManager));
        commandManager.register("login", new Authenticate(authManager));
    }
}