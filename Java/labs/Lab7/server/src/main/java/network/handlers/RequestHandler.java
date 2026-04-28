package network.handlers;

import java.util.List;
import java.util.Set;

import commands.Command;
import common.exceptions.AuthException;
import common.transfer.Status;
import common.transfer.request.Request;
import common.transfer.request.standart.AuthRequest;
import common.transfer.request.standart.InitRequest;
import common.transfer.request.standart.NextChunkRequest;
import common.transfer.request.standart.StandartRequest;
import common.transfer.request.wrapped.AuthenticatedRequest;
import common.transfer.request.wrapped.HeaderRequest;
import common.transfer.response.Response;
import managers.auth.AbstractAuthManager;
import managers.commands.AbstractCommandManager;

/**
 * Класс обработки запросов с клиента
 * @author Septyq
 */
public class RequestHandler {
    protected final AbstractCommandManager commandManager;
    protected final AbstractAuthManager authManager;

    public RequestHandler(AbstractCommandManager commandManager, AbstractAuthManager authManager) {
        this.commandManager = commandManager;
        this.authManager = authManager;
    }

    public Response<?> handleRequest(Request request) {
        if (!(request instanceof HeaderRequest)) {
            return new Response<>(List.of("request has no required headers. Possibly you need to login"), Status.ERROR);
        }

        if (request instanceof AuthenticatedRequest) {
            AuthenticatedRequest authenticatedRequest = (AuthenticatedRequest) request;
            Request originalRequest = authenticatedRequest.unwrap();
            Class <? extends Request> originalRequestClass = originalRequest.getClass();

            Response<?> response = handlePublicRequest(originalRequest, originalRequestClass);
            if (response.getBody().size() != 0) {
                return response;
            }

            try {
                authManager.authenticate(authenticatedRequest.getUserName(), authenticatedRequest.getPassword());
                if (originalRequestClass.equals(NextChunkRequest.class)) {
                    return ChunkHandler.handleNextChunk((NextChunkRequest) originalRequest);
                } else if (originalRequestClass.equals(StandartRequest.class)) {
                    return executeCommand((StandartRequest) originalRequest);
                }
            } catch (AuthException e) {
                return new Response<>(List.of(e.getMessage()), Status.ERROR);
            }
        } 

        return new Response<>(List.of("Unknown request"), Status.ERROR);
    }

    private Response<?> handlePublicRequest(Request request, Class <? extends Request> requestClass) {
        if (requestClass.equals(AuthRequest.class)) {
            return executeCommand((AuthRequest) request);
        } else if (requestClass.equals(InitRequest.class)) {
            return new Response<>(List.of(commandManager.getCommandAttributes()));
        }
        return new Response<>();
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
        if (ChunkHandler.shouldChunkify(response)) return ChunkHandler.chunkify(response);
        return response;
    };

    private boolean validateCommandName(String command) {
        Set<String> commandsNames = commandManager.getCommands().keySet();
        return commandsNames.contains(command);
    }
}
