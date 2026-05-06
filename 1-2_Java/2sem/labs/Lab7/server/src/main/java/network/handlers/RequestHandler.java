package network.handlers;

import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import auth.AuthManager;
import commands.AuthAwareCommand;
import commands.Command;
import commands.interfaces.Executable;
import commands.manager.CommandManager;
import common.exceptions.AuthException;
import common.models.User;
import common.transfer.Status;
import common.transfer.request.Request;
import common.transfer.request.standart.AuthRequest;
import common.transfer.request.standart.InitRequest;
import common.transfer.request.standart.NextChunkRequest;
import common.transfer.request.standart.StandartRequest;
import common.transfer.request.wrapped.AuthenticatedRequest;
import common.transfer.request.wrapped.HeaderRequest;
import common.transfer.response.Response;

public class RequestHandler {
    protected final CommandManager commandManager;
    protected final AuthManager authManager;
    private final ReentrantLock collectionLock;

    public RequestHandler(CommandManager commandManager, AuthManager authManager) {
        this.commandManager = commandManager;
        this.authManager = authManager;
        this.collectionLock = new ReentrantLock();  // Created here
    }

    public Response<?> handleRequest(Request request) {
        if (!(request instanceof HeaderRequest)) {
            return new Response<>(List.of("request has no required headers. Possibly you need to login"), Status.ERROR);
        }

        if (request instanceof AuthenticatedRequest) {
            AuthenticatedRequest authenticatedRequest = (AuthenticatedRequest) request;
            Request originalRequest = authenticatedRequest.unwrap();

            Response<?> response = handlePublicRequest(originalRequest, originalRequest.getClass());
            if (response.getBody().size() != 0) {
                return response;
            }

            try {
                authManager.authenticate(authenticatedRequest.getUserName(), authenticatedRequest.getPassword());
                if (originalRequest instanceof NextChunkRequest) {
                    return ChunkHandler.handleNextChunk((NextChunkRequest) originalRequest);
                } else if (originalRequest instanceof StandartRequest) {
                    return executeCommand((StandartRequest) originalRequest, authenticatedRequest.getHeaders());
                }
            } catch (AuthException e) {
                return new Response<>(List.of(e.getMessage()), Status.ERROR);
            }
        } 

        return new Response<>(List.of("Unknown request"), Status.ERROR);
    }

    private Response<?> handlePublicRequest(Request request, Class <? extends Request> requestClass) {
        if (requestClass.equals(AuthRequest.class)) {
            return executeCommand((AuthRequest) request, null);
        } else if (requestClass.equals(InitRequest.class)) {
            return new Response<>(List.of(commandManager.getCommandAttributes()));
        }
        return new Response<>();
    }

    private Response<?> executeCommand(StandartRequest request, User userData) {
        String commandName = request.getName();
        if (!validateCommandName(commandName)) {
            return new Response<>(List.of("Unknown command"), Status.ERROR);
        }
        Executable command = commandManager.getCommands().get(commandName);
        commandManager.addToHistory(command.getAttribute().getName());
        
        @SuppressWarnings("unchecked")
        Command<StandartRequest> typedCommand = (Command<StandartRequest>) command;
        
        collectionLock.lock();
        try {
            if (command instanceof AuthAwareCommand && userData != null) {
                return handleAuthAwareCommand(typedCommand, request, userData);
            } else if (command instanceof Command) {
                return handleOrdinaryCommand(typedCommand, request);
            } else {
                return new Response<>(List.of("invalid command signature"), Status.ERROR);
            }
        } finally {
            collectionLock.unlock();
        }
    }

    private Response<?> handleAuthAwareCommand(Command<StandartRequest> command, StandartRequest request, User userData) {
        AuthAwareCommand<StandartRequest> authCommand = (AuthAwareCommand<StandartRequest>) command;

        Response<?> response = authCommand.execute(request, userData);
        if (ChunkHandler.shouldChunkify(response)) return ChunkHandler.chunkify(response);
        return response;
    }

    private Response<?> handleOrdinaryCommand(Command<StandartRequest> command, StandartRequest request) {
        Response<?> response = command.execute(request);
        if (ChunkHandler.shouldChunkify(response)) return ChunkHandler.chunkify(response);
        return response;
    }

    private boolean validateCommandName(String command) {
        Set<String> commandsNames = commandManager.getCommands().keySet();
        return commandsNames.contains(command);
    }
    
    public ReentrantLock getCollectionLock() {
        return collectionLock;
    }
}