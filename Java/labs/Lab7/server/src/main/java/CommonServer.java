import java.io.IOException;
import commands.*;
import network.CommonNetwork;
import common.network.Network;
import common.exceptions.RuntimeInitException;
import common.transfer.response.Response;
import common.transfer.request.Request;

/**
 * TCP сервер
 * @author Septyq
 */
public class CommonServer extends AbstractServer {
    private final Network networkManager;
    private final int port;

    public CommonServer(int port) throws RuntimeInitException {
        super();
        this.port = port;
        this.networkManager = new CommonNetwork(port, logger);
    }


    public void run() {
        try {
            boolean running = true;
            networkManager.connect();
            logger.info("Server started on port " + port);
            
            while (running) {
                try {
                    Request request = (Request) networkManager.read();
										logger.info("new request: " + request);

                    Response<?> response = requestHandler.handleRequest(request);
										logger.info("sending response: " + response);
                    networkManager.write(response);
                    
                } catch (IOException e) {
                    networkManager.close();
										logger.warn("connection closed. Reconnecting");
                    networkManager.connect();
                    
                } catch (ClassNotFoundException e) {
                    logger.error("Protocol error: ", e);
                }
            }
        } catch (IOException e) {
            logger.error("Server failed to start: ", e);
        }
    }

    protected void registerCommands() {
        commandManager.register("help", new Help(commandManager));
        commandManager.register("info", new Info(collectionManager));
        commandManager.register("show", new Show(collectionManager));
        commandManager.register("add", new Add(collectionManager));
        commandManager.register("update", new Update(collectionManager));
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