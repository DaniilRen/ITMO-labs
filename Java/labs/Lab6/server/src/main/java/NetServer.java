import java.util.Set;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
import common.transfer.response.Response;
import common.transfer.request.Request;


/**
 * Обрабатывает запросы на исполнение комманд.
 * @author Septyq
 */
public class NetServer implements Server {
    private final FileManager fileManager;
    private final CollectionManager<Entity> collectionManager;
    private final CommandManager commandManager;
    private final Network networkManager;
    private final int port;
    

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
                    Response<?> response = proccessRequest(request);
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
        if (request instanceof InitRequest) {
            return new Response<>(List.of(commandManager.getCommandAttributes()));
        } else if (request instanceof StandartRequest) {
            return executeCommand((StandartRequest) request);
        } else {
            return new Response<>(List.of("Unknowm request"), Status.ERROR);
        }
    }

    private Response<?> executeCommand(StandartRequest request){
        String commandName = request.getName();
        if (!validateCommandName(commandName)) {
            return new Response<>(List.of("Unknown command"), Status.ERROR);
        }
        Command<?> command = commandManager.getCommands().get(commandName);
        commandManager.addToHistory(command.getAttribute().getName());
        
        @SuppressWarnings("unchecked")
        Command<StandartRequest> typedCommand = (Command<StandartRequest>) command;
        return typedCommand.execute(request);
    };

    private boolean validateCommandName(String command) {
        Set<String> commandsNames = commandManager.getCommands().keySet();
        return commandsNames.contains(command);
    }
}
