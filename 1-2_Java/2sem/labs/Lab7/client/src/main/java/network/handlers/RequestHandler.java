package network.handlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.exceptions.IncorrectRequestException;
import common.exceptions.InvalidAttributesException;
import common.exceptions.InvalidScriptException;
import common.transfer.Status;
import common.transfer.request.Request;
import common.transfer.request.standart.AuthRequest;
import common.transfer.request.standart.InitRequest;
import common.transfer.response.Response;
import console.IOConsole;
import network.AbstractClientNetwork;
import util.RequestBuilder;

public class RequestHandler {
    private Map<String, Class<? extends Request>> commandsAttributes = new HashMap<>(); 
    private final IOConsole console;
    private final AbstractClientNetwork network;
    private final AuthHandler authHandler;
    private final List<Class<? extends Request>> publicRequests = new ArrayList<>(Arrays.asList(AuthRequest.class, InitRequest.class));

    public RequestHandler(IOConsole console, AbstractClientNetwork network, AuthHandler authHandler) {
        this.console = console;
        this.network = network;
        this.authHandler = authHandler;
    }

    public Response<?> makeRequest(String name, List<?> args) {
        try {
            if (name.equals("logout") && args.size() == 0) {
                authHandler.logOut();
                return new Response<>(List.of("Logged out"));
            }

            Class<? extends Request> requestType = getRequestType(name);
            if (requestType == null) {
                return new Response<>(List.of("Invalid command!"), Status.ERROR);
            }
  
            if (!(authHandler.isAuthenticated()) && (!(publicRequests.contains(requestType)))) {
                return new Response<>(List.of("You must 'login' or 'register' before executing commands!"), Status.ERROR);
            }
            RequestBuilder requestBuilder = new RequestBuilder(console, authHandler.getCredentials().getName());
            Request request = requestBuilder.buildRequest(requestType, name, args);
            request = authHandler.wrapCredentials(request);

            return sendRequest(request);
        } catch (IncorrectRequestException e) {
            return new Response<>(List.of(e.getMessage()), Status.ERROR);
        } catch (InvalidScriptException e) {
            return new Response<>(List.of(e.getMessage()), Status.EXIT);
        }
    }

    private Response<?> sendRequest(Request request) {
        try {
            network.write(request);
            return (Response<?>) network.read();
        } catch (IOException e) {            
            try {
                console.printConnectionError("Connection lost: attempting to reconnect...");
                network.close();
                network.connect();
                
                network.write(request);
                Response<?> response = (Response<?>) network.read();
                return response;
                
            } catch (IOException | ClassNotFoundException ex) {
                console.printConnectionError("Failed to reconnect: " + ex.getMessage());
                return new Response<>(List.of("Server unavailable"), Status.ERROR);
            }
            
        } catch (ClassNotFoundException e) {
            return new Response<>(List.of("Protocol error"), Status.ERROR);
        }
    }

    @SuppressWarnings("unchecked")
    public void setCommandAttributes() throws InvalidAttributesException {
        Response<?> response = makeRequest("init", new ArrayList<>());

        List<?> body = response.getBody();
    
        if (body == null || body.isEmpty()) {
            throw new InvalidAttributesException("Empty response body");
        }

        Object item = body.get(0);
        if (!(item instanceof Map<?, ?>)) {
            throw new InvalidAttributesException("Expected attributes Map<?, ?>, but received: " + item);
        }

        commandsAttributes = (Map<String, Class<? extends Request>>) item;
    }

    private Class<? extends Request> getRequestType(String name) {
        Class<? extends Request> requestType = commandsAttributes.get(name);
        if ((requestType == null) && (name.equals("init"))) return InitRequest.class;
        return requestType;
    }
}
