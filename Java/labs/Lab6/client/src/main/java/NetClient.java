import common.transfer.Status;

import java.io.IOException;
import java.util.List;

import common.exceptions.InvalidAttributesException;
import common.exceptions.InvalidScriptException;
import common.transfer.response.Response;
import network.ClientNetwork;
import network.processing.RequestProcessor;
import network.processing.ResponseProcessor;
import util.ScriptProcessor;


public class NetClient extends AbstractClient {
    private final ScriptProcessor scriptProcessor;
    private final ClientNetwork network;
    private ResponseProcessor responseProcessor;
    private RequestProcessor requestProcessor;
    private final int port;

    public NetClient(String address, int port) {
        super();
        this.network = new ClientNetwork(address, port, console);
        this.port = port;
        this.scriptProcessor = new ScriptProcessor(console, this::executeCommand);
    }


    public void run() {
        try {
            this.responseProcessor = new ResponseProcessor(console, network);
            this.requestProcessor = new RequestProcessor(console, network);
            network.connect();
            requestProcessor.setCommandAttributes();
        } catch (IOException e) {
            console.printConnectionError("Failed to connect to server: " + e.getMessage());
            return;
        } catch (InvalidAttributesException e) {
            console.printConnectionError("[Init Request]: " + e.getMessage());
            return;
        }
        runConsoleParsing();
        network.close();
    }

    @Override
    protected void runConsoleParsing() {
        console.println(String.format("<----- client started on port: %d ----->", port));
        super.runConsoleParsing();
    }

    protected Status executeCommand(String commandName, List<?> args) {
        if (commandName == null || commandName.isEmpty()) return Status.ERROR;

        if (commandName.equals("exit")) {
            return Status.EXIT;
        }
        
        if (commandName.equals("execute_script")) {
            if (args.isEmpty()) {
                console.printError("No script file specified");
                return Status.ERROR;
            }
            try {
                scriptProcessor.executeScript((String) args.get(0));
                return Status.OK;
            } catch (InvalidScriptException | ClassCastException e) {
                console.printError(e.getMessage());
                return Status.ERROR;
            }
        }
        Response<?> response = requestProcessor.makeRequest(commandName, args);
        return responseProcessor.processCommandResponse(response);
    }
}