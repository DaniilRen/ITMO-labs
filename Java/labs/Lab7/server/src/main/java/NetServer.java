import java.io.IOException;
import java.util.List;
import commands.*;
import network.ChunkUtil;
import network.ServerNetwork;
import common.network.Network;
import common.transfer.Status;
import common.exceptions.RuntimeInitException;
import common.transfer.request.empty.InitRequest;
import common.transfer.request.standart.StandartRequest;
import common.transfer.request.standart.NextChunkRequest;
import common.transfer.response.Response;
import common.transfer.request.Request;

/**
 * TCP сервер
 * @author Septyq
 */
public class NetServer extends AbstractServer {
    private final Network networkManager;
    private final int port;

    public NetServer(int port) throws RuntimeInitException {
        super();
        this.port = port;
        this.networkManager = new ServerNetwork(port, logger);
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

                    Response<?> response = processRequest(request);
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

    protected Response<?> processRequest(Request request) {
        if (request instanceof InitRequest) {
            return new Response<>(List.of(commandManager.getCommandAttributes()));
        } else if (request instanceof NextChunkRequest) {
            return ChunkUtil.handleNextChunk((NextChunkRequest) request);
        } else if (request instanceof StandartRequest) {
            return executeCommand((StandartRequest) request);
        } else {
            return new Response<>(List.of("Unknown request"), Status.ERROR);
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
    }

    protected Response<?> executeCommand(StandartRequest request) {
        Response<?> response = super.executeCommand(request);
        if (ChunkUtil.shouldChunkify(response)) return ChunkUtil.chunkify(response);
        return response;
    }
}