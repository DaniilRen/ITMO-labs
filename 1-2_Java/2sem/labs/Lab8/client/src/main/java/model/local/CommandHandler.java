package model.local;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.command.CommandAttribute;
import common.command.PublicityMarker;
import common.exceptions.InvalidAttributesException;
import common.transfer.Status;
import common.transfer.request.Request;
import common.transfer.request.standart.StandartRequest;
import common.transfer.response.Response;
import model.network.AbstractClientNetwork;
import model.network.handlers.AuthHandler;
import model.network.handlers.RequestHandler;
import model.network.handlers.ResponseHandler;
import model.local.localActions.ExecuteScriptAction;
import model.local.localActions.ExitAction;
import model.local.localActions.LogOutAction;
import model.local.script.ScriptProcessor;
import view.View;

public class CommandHandler {
    private final LocalCommandHandler localCommandHandler;
    private final View view;
    private final ResponseHandler responseHandler;
    private final RequestHandler requestHandler;
    private final AuthHandler authHandler;
    private final ScriptProcessor scriptProcessor;

    private Map<String, CommandAttribute> commandsAttributes = new HashMap<>(); 

    public CommandHandler(View view, AuthHandler authHandler, AbstractClientNetwork network) {
        this.view = view;
        this.scriptProcessor = new ScriptProcessor(view, this::handleCommandWithStatus);
        this.authHandler = authHandler;
        
        this.localCommandHandler = new LocalCommandHandler(view, scriptProcessor);
        registerLocalCommands();

        this.responseHandler = new ResponseHandler(view, network, authHandler);
        this.requestHandler = new RequestHandler(view, network, authHandler, scriptProcessor);
    }

    public void handleCommand(String commandName, List<?> args, boolean fileMode) {
        handleCommandWithStatus(commandName, args, fileMode);
    }

    public Status handleCommandWithStatus(String commandName, List<?> args, boolean fileMode) {
        if (localCommandHandler.isLocalCommand(commandName)) {
            return localCommandHandler.handleLocalCommand(commandName, args);
        }

        if (!(validateCommandName(commandName)) || !(validateCommandPublicity(commandName))) {
            view.displayError("Unknown command");
            return Status.ERROR;
        }

        if (!(authHandler.isAuthenticated()) && getCommandPublicity(commandName) != PublicityMarker.PUBLIC) {
            view.displayError("You must 'login' or 'register' before executing commands!");
            return Status.ERROR;
        }

        Response<?> response = requestHandler.makeRequest(commandName, getRequestType(commandName), args, fileMode);
        Status status = responseHandler.handleResponse(response, commandName);
        return status;
    }

    @SuppressWarnings("unchecked")
    public void setCommandAttributes() throws InvalidAttributesException {
        Response<?> response = requestHandler.makeRequest("init", StandartRequest.class, new ArrayList<>(), false);

        List<?> body = response.getBody();
    
        if (body == null || body.isEmpty()) {
            throw new InvalidAttributesException("Empty response body");
        }

        Object item = body.get(0);
        if (!(item instanceof Map<?, ?>)) {
            throw new InvalidAttributesException("Expected attributes Map<?, ?>, but received: " + item);
        }

        commandsAttributes = (Map<String, CommandAttribute>) item;
    }

    private Class<? extends Request> getRequestType(String name) {
        return commandsAttributes.get(name).getArgsType();
    }

    private PublicityMarker getCommandPublicity(String name) {
        return commandsAttributes.get(name).getPublicity();
    }

    private boolean validateCommandName(String commandName) {
        return (!(commandName == null) && !commandName.isEmpty() && commandsAttributes.containsKey(commandName));
    }

    private boolean validateCommandPublicity(String commandName) {
        return (getCommandPublicity(commandName) != PublicityMarker.SERVICE);
    }

    private void registerLocalCommands() {
        localCommandHandler.setLocalCommands(Map.of(
            "exit", new ExitAction(),
            "logout", new LogOutAction(),
            "execute_script", new ExecuteScriptAction()
        ));
    }
}