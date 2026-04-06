import java.util.Set;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import commands.*;
import managers.*;
import common.models.Entity;
import common.network.NetworkManager;
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
    private final NetworkManager networkManager;
    

    public NetServer(String collectionFile, int port) throws RuntimeInitException {
        this.commandManager = new DefaultCommandManager();
        this.fileManager = new JSONManager(collectionFile);
        this.networkManager = new ServerNetworkManager(port);

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
        boolean ok = true;
        try {
            do { 
                networkManager.writeObject(proccessRequest((Request) networkManager.read()));
            } while (ok);
        } catch (IOException | ClassNotFoundException e) {}
    };


    private void registerCommands() {
        commandManager.register("help", new Help(commandManager));
        commandManager.register("info", new Info(collectionManager));
        commandManager.register("show", new Show(collectionManager));
        commandManager.register("add", new Add(collectionManager));
        commandManager.register("update", new Update(collectionManager));
        commandManager.register("remove_by_id", new RemoveById(collectionManager));
        commandManager.register("clear", new Clear(collectionManager));
        commandManager.register("exit", new Exit());
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
