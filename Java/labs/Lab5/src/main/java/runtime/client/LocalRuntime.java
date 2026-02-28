package runtime.client;

import util.Request;
import util.Response;
import util.Status;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import runtime.Runtime;
import runtime.server.RemoteRuntime;
import util.console.IOConsole;
import util.exceptions.CollectionLoadException;
import util.exceptions.InvalidFormException;
import util.exceptions.ScriptRecursionException;
import util.forms.RouteForm;


public class LocalRuntime extends Runtime{
    private final IOConsole console;
    private final Scanner scanner;
    private final RemoteRuntime remoteRuntime;
    private String lastExecutedCommandName;
    private ArrayList<Object> lastExecutedCommandArgs = new ArrayList<>();
    private final List<String> scriptStack = new ArrayList<>();

    public LocalRuntime(RemoteRuntime remoteRuntime) {
        this.console = new IOConsole();
        console.setUserScanner(new Scanner(System.in));
        this.scanner = this.console.getUserScanner();
        this.remoteRuntime = remoteRuntime;
    }


    public void run(String... args) {
        try {
            remoteRuntime.registerCommands();
        } catch (CollectionLoadException e) {
            console.printError(e.getMessage());
            System.exit(0);
        }
        
        String mode = args[0].toLowerCase();

        if (mode == "interactive") {
            runInteractiveMode();
        } else if (mode == "script") {
            String fileName = args[1];
            if (fileName == "") {
                console.printError("Invalid script file name: "+mode);
                System.exit(0);
            }
            Status scriptModeStatus = runScriptMode(fileName);
            console.println("--- FINISHED SCRIPT WITH STATUS: "+scriptModeStatus+" ---");
        } else {
            console.printError("Invalid local runtime mode: "+mode);
        }
    }
    

    private Status runScriptMode(String fileName) {
        String[] userCommand = {"", ""};
        Status commandStatus;
        scriptStack.add(fileName);

        try (Scanner scriptScanner = new Scanner(new File(fileName))) {
            console.println(String.format(">--- RUNNING SCRIPT %s ---", fileName));

            File file = new File(fileName);

            if (!file.exists()) {
                throw new FileNotFoundException("File does not exist");
            }

            if (!file.canRead()) {
                throw new SecurityException("No read permission for file: " + file.getAbsolutePath());
            }

            if (!file.canWrite()) {
                throw new SecurityException("No write permission for file: " + file.getAbsolutePath());
            }
            
            if (!scriptScanner.hasNext()) throw new NoSuchElementException();

            Scanner tmpScanner = console.getUserScanner();
            console.setUserScanner(scriptScanner);
            console.setFileMode();

            do {
                userCommand = (scriptScanner.nextLine().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                while (scriptScanner.hasNextLine() && userCommand[0].isEmpty()) {
                    userCommand = (scriptScanner.nextLine().trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                }
                console.println(console.getPromptSymbol() + String.join(" ", userCommand));

                if (userCommand[0].equals("execute_script")) {
                    for (String script : scriptStack) {
                        if (userCommand[1].equals(script)) throw new ScriptRecursionException();
                    }
                }
                String commandName = userCommand[0];
                List<?> args = List.of(userCommand[1]);
                if (args.size() == 1 && args.get(0) == "") {
                    args = List.of();
                }
                commandStatus = executeCommand(commandName, args);

            } while (commandStatus != Status.EXIT && scriptScanner.hasNextLine());

            console.setUserScanner(tmpScanner);
            console.setUserMode();

            if (commandStatus == Status.ERROR && !(userCommand[0].equals("execute_script") && !userCommand[1].isEmpty())) {
                console.println("Invalid script");
            }

            return commandStatus;

        } catch (FileNotFoundException exception) {
            console.printError("File not found");
        } catch (NoSuchElementException exception) {
            console.printError("File is empty");
        } catch (ScriptRecursionException exception) {
            console.printError("Script has recursion");
        } catch (IllegalStateException exception) {
            console.printError("Unknowm error");
            System.exit(0);
        } catch (SecurityException e) {
            console.printError(e.getMessage());
        } finally {
            scriptStack.remove(scriptStack.size() - 1);
        }
        return Status.ERROR;
    }

    
    private void runInteractiveMode(){
        console.println(">----- COLLECTION MANAGER CLI -----");
        String[] userCommand = {"", ""};
        Status commandStatus = Status.OK;

        do {
            console.printPromptSymbol();

            userCommand = (scanner.nextLine().trim() + " ").split(" ", 2);
            userCommand[1] = userCommand[1].trim();
            
            String commandName = userCommand[0];
            List<?> args = List.of(userCommand[1]);
            if (args.size() == 1 && args.get(0) == "") {
                args = List.of();
            }

            commandStatus = executeCommand(commandName, args);
        } while (commandStatus != Status.EXIT);

        scanner.close();
    };

    private Status executeCommand(String commandName, List<?> args) {
        if (commandName.isEmpty()) {
            return Status.ERROR;
        }
        Response<?> response = makeRequest(commandName, args);

        this.lastExecutedCommandName = commandName;
        this.lastExecutedCommandArgs = new ArrayList<>(args);
        Status status = response.getStatus();
        if (status == Status.OK) {
            var body = response.getBody();
            if (!body.isEmpty()) {
                body.forEach((element) -> {
                    console.println(element);
                });
            }
        } else if (status == Status.ERROR) {
            console.printError(response.getBody().getFirst());
        } else if (status == Status.INPUT) {
            try {
                RouteForm form = new RouteForm(console);
                ArrayList<Object> newArgs = lastExecutedCommandArgs != null 
                    ? new ArrayList<>(lastExecutedCommandArgs) 
                    : new ArrayList<>();
  
                var result = form.build();
                if (result == null) {
                   throw new InvalidFormException("Entity wasn`t built due to main form validation error");
                }
    
                newArgs.add(result);
                executeCommand(lastExecutedCommandName, newArgs);   
            } catch (InvalidFormException e) {
                console.printError(e.getMessage());
                return Status.ERROR;
            }
        }
        return status;
    }


    private Response<?> makeRequest(String commandName, List<?> body) {
        Request<?> request = new Request<>(commandName, body);
        return remoteRuntime.proccessRequest(request);
    }
}
