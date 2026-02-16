package runtime.client;

import util.Request;
import util.Response;
import util.Status;

import java.util.List;
import java.util.Scanner;

import runtime.Runtime;
import runtime.server.RemoteRuntime;
import util.console.Console;
import util.console.DefaultConsole;
import util.exceptions.build.BuildException;
import util.Builder;


public class LocalRuntime extends Runtime{
    private final Console console;
    private final Scanner scanner;
    private final RemoteRuntime remoteRuntime;
    private String lastExecutedCommand;

    public LocalRuntime(RemoteRuntime remoteRuntime) {
        this.console = new DefaultConsole();
        this.scanner = new Scanner(System.in);
        this.remoteRuntime = remoteRuntime;
    }


    public void run(String... args) {
        String mode = args[0].toLowerCase();

        if (mode == "interactive") {
            runInteractiveMode();
        } else if (mode == "script") {
            runScriptMode();
        } else {
            console.printError("Invalid mode");
        }
    }
    

    public void runScriptMode(){}

    
    public void runInteractiveMode(){
        String[] userCommand = {"", ""};
        Status currentStatus = Status.OK;

        do {
            userCommand = (scanner.nextLine().trim() + " ").split(" ", 2);
            userCommand[1] = userCommand[1].trim();
            String commandName = userCommand[0];
            List<?> args = List.of(userCommand[1]);
            executeCommand(commandName, args);
        } while (currentStatus != Status.EXIT);

        scanner.close();
    };

    private void executeCommand(String commandName, List<?> args) {
        if (commandName.isEmpty()) {
            return;
        }
        Response<?> response = makeRequest(commandName, args);
        this.lastExecutedCommand = commandName;
        Status status = response.getStatus();
        if (status == Status.OK) {
            var body = response.getBody();
            if (body.isEmpty() == false) {
                body.forEach((element) -> {
                    console.print(element);
                });
            }
        } else if (status == Status.ERROR) {
            console.printError(response.getBody());
        } else if (status == Status.INPUT) {
            try {
                Builder builder = new Builder(console, scanner);
                var result = builder.build();
                // if (result == null) {throw new BuildException("Invalid build form");}
                executeCommand(lastExecutedCommand, List.of(result));   
            } catch (BuildException e) {
                return;
            }
        }
    }


    private Response<?> makeRequest(String commandName, List<?> body) {
        Request<?> request = new Request<>(commandName, body);
        return remoteRuntime.proccessRequest(request);
    }
}
