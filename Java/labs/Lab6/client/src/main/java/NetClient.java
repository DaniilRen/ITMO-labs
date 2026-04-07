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
import common.transfer.response.Response;
import common.network.Network;
import controller.RecursionController;
import network.ClientNetwork;


/**
 * Считывает команды из консоли или скрипта, выводит результат.
 * @author Septyq
 */
public class NetClient implements Client {
    private final IOConsole console;
    private final Scanner scanner;
    private Map<String, Class<? extends Request>> commandsAttributes = new HashMap<>(); 
    private final List<String> scriptStack = new ArrayList<>();
    private final String address;
    private final int port;

    public NetClient(String address, int port) {
        this.address = address;
        this.port = port;
        this.console = new IOConsole();
        console.setUserScanner(new Scanner(System.in));
        this.scanner = this.console.getUserScanner();
    }


    public void run(String... args) {
        if (args.length == 0) {
            runInteractiveMode();
        } else {
            String fileName = args[0];
            if (fileName == "") {
                console.printError("Invalid script file name: "+fileName);
                System.exit(0);
            }
            runScriptMode(fileName);
        }
    }
    

    private void runScriptMode(String fileName) {
        String[] userCommand = {"", ""};
        Status commandStatus = setCommandAttributes();
        scriptStack.add(fileName);

        try (Scanner scriptScanner = new Scanner(new File(fileName))) {
            console.println(String.format("--> RUNNING SCRIPT: %s ...", fileName));

            File file = new File(fileName);
            if (!file.exists()) throw new FileNotFoundException("File does not exist");
            if (!file.canRead()) throw new SecurityException("No read permission for file: " + file.getAbsolutePath());
            if (!file.canWrite()) throw new SecurityException("No write permission for file: " + file.getAbsolutePath());
            if (!scriptScanner.hasNext()) throw new NoSuchElementException();

            Scanner tmpScanner = console.getUserScanner();
            console.setUserScanner(scriptScanner);
            console.setFileMode();

            while (commandStatus != Status.EXIT && scriptScanner.hasNextLine()) {
                userCommand = (scriptScanner.nextLine().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                while (scriptScanner.hasNextLine() && userCommand[0].isEmpty()) {
                    userCommand = (scriptScanner.nextLine().trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                }
                console.println(console.getScriptPromptSymbol() + String.join(" ", userCommand));

                String commandName = userCommand[0];
                List<?> args = List.of(userCommand[1]);
                if (args.size() == 1 && args.get(0) == "") {
                    args = List.of();
                }
                commandStatus = executeCommand(commandName, args);
            }
            console.setUserScanner(tmpScanner);
            console.setUserMode();

            if (commandStatus == Status.ERROR && !(userCommand[0].equals("execute_script") && !userCommand[1].isEmpty())) {
                console.println("Invalid script");
            }

        } catch (FileNotFoundException exception) {
            console.printError("File not found");
        } catch (NoSuchElementException exception) {
            console.printError("File is empty");
        } catch (IllegalStateException exception) {
            console.printError("Unknowm error");
            System.exit(0);
        } catch (SecurityException e) {
            console.printError(e.getMessage());
        } finally {
            scriptStack.remove(scriptStack.size() - 1);
        }
    }

    
    private void runInteractiveMode() {
        console.println("<----- COLLECTION MANAGER CLIENT ----->");

        String[] userCommand = {"", ""};
        Status commandStatus = setCommandAttributes();

        while (commandStatus != Status.EXIT) {
            console.printPromptSymbol();

            userCommand = (scanner.nextLine().trim() + " ").split(" ", 2);
            userCommand[1] = userCommand[1].trim();
            
            String commandName = userCommand[0];
            List<?> args = List.of(userCommand[1]);
            if (args.size() == 1 && args.get(0) == "") {
                args = List.of();
            }

            commandStatus = executeCommand(commandName, args);
        }
    };

    private Status executeCommand(String commandName, List<?> args) {
        if (commandName == "" || commandName == null) return Status.ERROR;

        if (commandName.equals("exit")) {
            return Status.EXIT;
        }
        if (commandName.equals("execute_script")) {
            Response<?> scriptResponse = new Response<>();
            String fileName = (String) args.get(0);
            if (RecursionController.checkRecursion(fileName)) {
                scriptResponse = new Response<>(List.of("Script has recursion!"), Status.ERROR);
            } else {
                if (fileName == "") scriptResponse  = new Response<>(List.of("Invalid script name"), Status.ERROR);

                RecursionController.pushScript(fileName);

                NetClient nestedRuntime = new NetClient(address, port);
                nestedRuntime.run(fileName);

                RecursionController.popScript(fileName);

                scriptResponse  = new Response<>();
            }

            return processCommandResponse(scriptResponse);
        }

        return processCommandResponse(makeRequest(commandName, args));
    }

    private Status processCommandResponse(Response<?> response) {
        Status status = response.getStatus();
        if (status == Status.OK) {
            printCommandResponse(response.getBody());
        } else if (status == Status.ERROR) {
            console.printError(response.getBody().getFirst());
        }
        return status;
    }

    private void printCommandResponse(List<?> body) {
        if (!(body == null || body.isEmpty())) {
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
        if (name == "init") {
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

            Network tempManager = null;
            try {
                tempManager = new ClientNetwork(address, port);
                tempManager.connect(); // Подключаемся
                
                tempManager.writeObject(request); // Отправляем запрос
                Response<?> response = (Response<?>) tempManager.read(); // Читаем ответ
                
                return response;
                
            } catch (IOException e) {
                console.printError("Server error: " + e.getMessage());
                return new Response<>(List.of("Server unavailable"), Status.ERROR);
            } catch (ClassNotFoundException e) {
                return new Response<>(List.of("Protocol error"), Status.ERROR);
            } finally {
                if (tempManager != null) {
                    try { tempManager.close(); } catch (IOException e) {}
                }
            }
    
    }


}
