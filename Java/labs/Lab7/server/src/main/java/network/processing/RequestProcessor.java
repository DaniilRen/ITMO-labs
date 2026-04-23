package network.processing;

import java.util.List;
import java.util.Set;

import commands.Command;
import common.exceptions.AuthException;
import common.transfer.Status;
import common.transfer.request.Request;
import common.transfer.request.authenticated.AuthenticatedRequest;
import common.transfer.request.authenticated.RequestWrapper;
import common.transfer.request.empty.InitRequest;
import common.transfer.request.standart.AuthRequest;
import common.transfer.request.standart.NextChunkRequest;
import common.transfer.request.standart.StandartRequest;
import common.transfer.response.Response;
import managers.auth.AbstractAuthManager;
import managers.commands.AbstractCommandManager;

/**
 * Класс обработки запросов с клиента
 * @author Septyq
 */
public class RequestProcessor {
    protected final AbstractCommandManager commandManager;
    protected final AbstractAuthManager authManager;

    public RequestProcessor(AbstractCommandManager commandManager, AbstractAuthManager authManager) {
        this.commandManager = commandManager;
        this.authManager = authManager;
    }

    public Response<?> processRequest(Request request) {
        if (isAuthRequest(request)) {
            return executeCommand((AuthRequest) request);
        } else if (request instanceof InitRequest) {
            return new Response<>(List.of(commandManager.getCommandAttributes()));
        }

        if (request instanceof AuthenticatedRequest) {
            AuthenticatedRequest authenticatedRequest = (AuthenticatedRequest) request;
            try {
                System.out.println(authManager);
                System.out.println(authenticatedRequest.getUserName());
                System.out.println(authenticatedRequest.getPassword());
                authManager.authenticate(authenticatedRequest.getUserName(), authenticatedRequest.getPassword());
            } catch (AuthException e) {
                return new Response<>(List.of(e.getMessage()), Status.ERROR);
            }

            Request originalRequest = authenticatedRequest.getWrappedRequest();
            Class <? extends Request> originalRequestClass = originalRequest.getClass();

            if (originalRequestClass.equals(NextChunkRequest.class)) {
                return ChunkProcessor.handleNextChunk((NextChunkRequest) request);
            } else if (originalRequestClass.equals(StandartRequest.class)) {
                return executeCommand((StandartRequest) request);
            } else {
                return new Response<>(List.of("Unknown request"), Status.ERROR);
            }  
        } 

        return new Response<>(List.of("Unknown request"), Status.ERROR);
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
        if (ChunkProcessor.shouldChunkify(response)) return ChunkProcessor.chunkify(response);
        return response;
    };

    private boolean validateCommandName(String command) {
        Set<String> commandsNames = commandManager.getCommands().keySet();
        return commandsNames.contains(command);
    }

    private boolean isAuthRequest(Request request) {
        return (!(request instanceof RequestWrapper) && (request instanceof AuthRequest));
    }
}
