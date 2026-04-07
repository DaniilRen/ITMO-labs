import java.util.Set;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import commands.*;
import managers.*;
import network.ServerNetwork;
import util.LocalEnvironment;
import common.models.Entity;
import common.network.Network;
import common.transfer.Status;
import common.exceptions.CollectionLoadException;
import common.exceptions.RuntimeInitException;
import common.transfer.request.empty.InitRequest;
import common.transfer.request.standart.StandartRequest;
import common.transfer.request.standart.NextChunkRequest;
import common.transfer.response.Response;
import common.transfer.request.Request;


public class NetServer implements Server {
    private final FileManager fileManager;
    private final CollectionManager<Entity> collectionManager;
    private final CommandManager commandManager;
    private final Network networkManager;
    private final int port;
    private final Map<String, List<Response<?>>> chunkStorage = new ConcurrentHashMap<>();
    private static final int MAX_CHUNK_SIZE_ITEMS = 100;
    private static final int MAX_CHUNK_SIZE_BYTES = 1024 * 1024;

    public NetServer(int port) throws RuntimeInitException {
        this.commandManager = new DefaultCommandManager();
        this.port = port;
        this.networkManager = new ServerNetwork(port);

        String collectionFile = LocalEnvironment.getCollectionPath();
        if (collectionFile == null) {
            throw new RuntimeInitException("Invalid collection path");
        }
        this.fileManager = new JSONManager(collectionFile);

        Collection<Entity> collection = new ArrayList<>();
        try {
            collection = fileManager.readCollectionFromFile();
        } catch (CollectionLoadException e) {
            throw new RuntimeInitException(e.getMessage());
        }
        this.collectionManager = new ArrayListCollectionManager<Entity>(collection);
        registerCommands();
    }


    public void run() {
        try {
            boolean running = true;
            networkManager.connect();
            System.out.println("Server started on port " + port);
            
            while (running) {
                try {
                    Request request = (Request) networkManager.read();
                    Response<?> response = processRequest(request);
                    networkManager.write(response);
                    
                } catch (IOException e) {
                    networkManager.close();
                    networkManager.connect();
                    
                } catch (ClassNotFoundException e) {
                    System.err.println("Protocol error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Server failed to start: " + e.getMessage());
        }
    }

    private Response<?> processRequest(Request request) {
        if (request instanceof InitRequest) {
            return new Response<>(List.of(commandManager.getCommandAttributes()));
        } else if (request instanceof NextChunkRequest) {
            return handleNextChunk((NextChunkRequest) request);
        } else if (request instanceof StandartRequest) {
            return executeCommand((StandartRequest) request);
        } else {
            return new Response<>(List.of("Unknown request"), Status.ERROR);
        }
    }

    private Response<?> handleNextChunk(NextChunkRequest request) {
        String streamId = request.getStreamId();
        int chunkNumber = request.getChunkNumber();
        
        List<Response<?>> chunks = chunkStorage.get(streamId);
        if (chunks == null) {
            return new Response<>(List.of("Stream not found"), Status.ERROR);
        }
        
        if (chunkNumber < 1 || chunkNumber > chunks.size()) {
            return new Response<>(List.of("Invalid chunk number"), Status.ERROR);
        }
        
        Response<?> chunk = chunks.get(chunkNumber - 1);
        
        if (chunkNumber == chunks.size()) {
            chunkStorage.remove(streamId);
        }
        
        return chunk;
    }

    private void registerCommands() {
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

    public Response<?> proccessRequest(Request request) {
        return processRequest(request);
    }

    private Response<?> executeCommand(StandartRequest request) {
        String commandName = request.getName();
        if (!validateCommandName(commandName)) {
            return new Response<>(List.of("Unknown command"), Status.ERROR);
        }
        Command<?> command = commandManager.getCommands().get(commandName);
        commandManager.addToHistory(command.getAttribute().getName());
        
        @SuppressWarnings("unchecked")
        Command<StandartRequest> typedCommand = (Command<StandartRequest>) command;
        Response<?> response = typedCommand.execute(request);
        
        if (shouldChunkify(response)) {
            return chunkifyResponse(response);
        }
        
        return response;
    }

    private boolean shouldChunkify(Response<?> response) {
        List<?> body = response.getBody();
        if (body == null || body.isEmpty()) {
            return false;
        }
        
        if (body.size() <= MAX_CHUNK_SIZE_ITEMS) {
            return false;
        }
        
        long estimatedSize = estimateSize(body);
        return estimatedSize > MAX_CHUNK_SIZE_BYTES;
    }

    private long estimateSize(List<?> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(list);
            oos.close();
            return baos.size();
        } catch (IOException e) {
            return list.size() * 500;
        }
    }

    @SuppressWarnings("unchecked")
    private Response<?> chunkifyResponse(Response<?> response) {
        List<Object> body = (List<Object>) response.getBody();
        
        int dynamicChunkSize = calculateDynamicChunkSize(body);
        
        List<Response<Object>> chunks = Response.splitIntoChunks(body, dynamicChunkSize);
        
        if (chunks.isEmpty()) {
            return response;
        }
        
        String streamId = chunks.get(0).getStreamId();
        chunkStorage.put(streamId, new ArrayList<>(chunks));
        
        return chunks.get(0);
    }

    private int calculateDynamicChunkSize(List<?> body) {
        long totalSize = estimateSize(body);
        if (totalSize <= 0) {
            return MAX_CHUNK_SIZE_ITEMS;
        }
        
        int targetChunks = Math.max(2, (int) (totalSize / MAX_CHUNK_SIZE_BYTES) + 1);
        int chunkSize = Math.max(1, body.size() / targetChunks);
        
        return Math.min(chunkSize, MAX_CHUNK_SIZE_ITEMS);
    }

    private boolean validateCommandName(String command) {
        Set<String> commandsNames = commandManager.getCommands().keySet();
        return commandsNames.contains(command);
    }
}