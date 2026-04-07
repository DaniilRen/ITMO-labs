import common.transfer.Status;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import console.IOConsole;
import common.exceptions.IncorrectRequestException;
import common.exceptions.RuntimeInitException;
import common.exceptions.ScriptSyntaxException;
import common.transfer.request.Request;
import util.RequestBuilder;
import common.transfer.request.empty.InitRequest;
import common.transfer.request.standart.NextChunkRequest;
import common.transfer.response.Response;
import controller.RecursionController;
import network.ClientNetwork;


public class NetClient implements Client {
    private final IOConsole console;
    private final Scanner scanner;
    private Map<String, Class<? extends Request>> commandsAttributes = new HashMap<>(); 
    private final List<String> scriptStack = new ArrayList<>();
    private final ClientNetwork network;
    private boolean attributesLoaded = false;

    public NetClient(String address, int port) {
        this.console = new IOConsole();
        this.network = new ClientNetwork(address, port, console);
        console.setUserScanner(new Scanner(System.in));
        this.scanner = this.console.getUserScanner();
    }


    public void run() {
        try {
            network.connect();
        } catch (IOException e) {
            console.printConnectionError("Failed to connect to server: " + e.getMessage());
            return;
        }
        runInteractiveMode();
        network.close();
    }
    

    private void runScriptMode(String fileName) {
        Status commandStatus = Status.OK;
        scriptStack.add(fileName);

        Scanner oldScanner = console.getUserScanner();
        
        try (Scanner scriptScanner = new Scanner(new File(fileName))) {
            console.println(String.format("--> RUNNING SCRIPT: %s ...", fileName));

            File file = new File(fileName);
            if (!file.exists()) throw new FileNotFoundException("File does not exist");
            if (!file.canRead()) throw new SecurityException("No read permission for file: " + file.getAbsolutePath());
            if (!scriptScanner.hasNext()) throw new NoSuchElementException();

            console.setUserScanner(scriptScanner);
            console.setFileMode();

            while (commandStatus != Status.EXIT && scriptScanner.hasNextLine()) {
                String line = scriptScanner.nextLine().trim();
                
                while (line.isEmpty() && scriptScanner.hasNextLine()) {
                    line = scriptScanner.nextLine().trim();
                }
                if (line.isEmpty()) continue;
                
                console.println(console.getScriptPromptSymbol() + line);

                String[] parts = line.split(" ", 2);
                String commandName = parts[0];
                String arg = parts.length > 1 ? parts[1] : "";
                
                List<?> args = arg.isEmpty() ? List.of() : List.of(arg);
                
                commandStatus = executeCommand(commandName, args);
            }

        } catch (FileNotFoundException exception) {
            console.printError("File not found");
        } catch (NoSuchElementException exception) {
            console.printError("File is empty");
        } catch (IllegalStateException exception) {
            console.printError("Unknown error");
            System.exit(0);
        } catch (SecurityException e) {
            console.printError(e.getMessage());
        } finally {
            console.setUserScanner(oldScanner);
            console.setUserMode();
            scriptStack.remove(scriptStack.size() - 1);
        }
    }

    
    private void runInteractiveMode() {
        console.println("<----- COLLECTION MANAGER CLIENT ----->");

        String[] userCommand = {"", ""};
        
        if (!attributesLoaded) {
            Status status = setCommandAttributes();
            if (status != Status.OK) {
                console.printError("Failed to load command attributes");
                return;
            }
            attributesLoaded = true;
        }

        while (true) {
            console.printPromptSymbol();

            userCommand = (scanner.nextLine().trim() + " ").split(" ", 2);
            userCommand[1] = userCommand[1].trim();
            
            String commandName = userCommand[0];
            List<?> args = List.of(userCommand[1]);
            if (args.size() == 1 && args.get(0).equals("")) {
                args = List.of();
            }

            Status commandStatus = executeCommand(commandName, args);
            if (commandStatus == Status.EXIT) {
                break;
            }
        }
    }

    private Status executeCommand(String commandName, List<?> args) {
        if (commandName == null || commandName.isEmpty()) return Status.ERROR;

        if (commandName.equals("exit")) {
            return Status.EXIT;
        }
        
        if (commandName.equals("execute_script")) {
            if (args.isEmpty()) {
                console.printError("No script file specified");
                return Status.ERROR;
            }
            
            String fileName = (String) args.get(0);
            
            if (fileName == null || fileName.isEmpty()) {
                console.printError("Invalid script name");
                return Status.ERROR;
            }
            
            if (RecursionController.checkRecursion(fileName)) {
                console.printError("Script has recursion!");
                return Status.ERROR;
            }
            
            RecursionController.pushScript(fileName);
            runScriptMode(fileName);
            RecursionController.popScript(fileName);
            
            return Status.OK;
        }

        return processCommandResponse(makeRequest(commandName, args));
    }

    private Status processCommandResponse(Response<?> response) {
        if (response.isChunked()) {
            return processChunkedResponse(response);
        }
        
        Status status = response.getStatus();
        if (status == Status.OK) {
            printCommandResponse(response.getBody());
        } else if (status == Status.ERROR) {
            List<?> body = response.getBody();
            if (body != null && !body.isEmpty()) {
                console.printError(body.getFirst().toString());
            } else {
                console.printError("Unknown error occurred");
            }
        }
        return status;
    }

    @SuppressWarnings("unchecked")
    private Status processChunkedResponse(Response<?> firstChunk) {
        String streamId = firstChunk.getStreamId();
        int totalChunks = firstChunk.getTotalChunks();
        
        List<Object> allData = new ArrayList<>();
        
        if (firstChunk.getBody() != null) {
            allData.addAll((List<Object>) firstChunk.getBody());
        }
        
        console.println(String.format("Loading data (%d chunks)...", totalChunks));
				int loadBarSectionIdx = totalChunks / 10;
        for (int chunkNum = 2; chunkNum <= totalChunks; chunkNum++) {
            try {
                NextChunkRequest chunkRequest = new NextChunkRequest(streamId, chunkNum);
                network.write(chunkRequest);
                Response<?> nextChunk = (Response<?>) network.read();
                
                if (nextChunk.getStatus() == Status.ERROR) {
                    console.printError("Failed to load chunk " + chunkNum);
                    return Status.ERROR;
                }
                
                if (nextChunk.getBody() != null) {
                    allData.addAll((List<Object>) nextChunk.getBody());
                }
                
								if (chunkNum % loadBarSectionIdx == 0) {
									console.println("o".repeat(chunkNum / loadBarSectionIdx) + "-".repeat(10-(chunkNum / loadBarSectionIdx)));
								}                
            } catch (IOException | ClassNotFoundException e) {
                console.printError("\nFailed to load chunk " + chunkNum + ": " + e.getMessage());
                return Status.ERROR;
            }
        }
        printCommandResponse(allData);
        return Status.OK;
    }

    private void printCommandResponse(List<?> body) {
        if (body != null && !body.isEmpty()) {
            body.forEach((element) -> {
                console.println(element);
            });
        }
    }

    @SuppressWarnings("unchecked")
    private Status setCommandAttributes() {
        try {
            Response<?> response = makeRequest("init", new ArrayList<>());

            List<?> body = response.getBody();
        
            if (body == null || body.isEmpty()) {
                throw new RuntimeInitException("Empty response body");
            }

            Object item = body.get(0);
            if (!(item instanceof Map<?, ?>)) {
                throw new RuntimeInitException("Expected Map, got: " + 
                    (item != null ? item.getClass().getSimpleName() : "null"));
            }

            commandsAttributes = (Map<String, Class<? extends Request>>) item;
            return Status.OK;
        } catch (RuntimeInitException e) {
            console.printError(e.getMessage());
            return Status.EXIT;
        }
    }

    private Response<?> makeRequest(String name, List<?> args) {
        Request request;
        if (name.equals("init")) {
            request = new InitRequest();
        } else {
            try {
                RequestBuilder requestBuilder = new RequestBuilder(console);
                request = requestBuilder.buildRequest(commandsAttributes, name, args);   
            } catch (IncorrectRequestException e) {
                return new Response<>(List.of(e.getMessage()), Status.ERROR);
            } catch (ScriptSyntaxException e) {
                return new Response<>(List.of(e.getMessage()), Status.EXIT);
            }
        }

        try {
            network.write(request);
            return (Response<?>) network.read();
            
        } catch (IOException e) {            
            try {
                console.printConnectionError("Connection lost: attempting to reconnect...");
                network.close();
                network.connect();
                
                network.write(request);
                return (Response<?>) network.read();
                
            } catch (IOException | ClassNotFoundException ex) {
                console.printConnectionError("Failed to reconnect: " + ex.getMessage());
                return new Response<>(List.of("Server unavailable"), Status.ERROR);
            }
            
        } catch (ClassNotFoundException e) {
            return new Response<>(List.of("Protocol error"), Status.ERROR);
        }
    }
}