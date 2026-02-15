package runtime.client;

import util.Request;
import util.Response;
import util.Status;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import commands.Command;
import managers.CommandManager;
import runtime.Runtime;
import runtime.server.RemoteRuntime;
import util.console.Console;
import util.console.DefaultConsole;

public class LocalRuntime extends Runtime{
    private final Console console;
    private final RemoteRuntime remoteRuntime;


    public LocalRuntime(RemoteRuntime remoteRuntime) {
        this.console = new DefaultConsole();
        this.remoteRuntime = remoteRuntime;
    }


    public void run(String... args) {
        String mode = args[0];

        if (mode == "interactive") {
            runInteractiveMode();
        } else if (mode == "script") {
            runScriptMode();
        } else {
            console.printError("Invalid run mode");
        }
    }
    

    public void runScriptMode(){}

    
    public void runInteractiveMode(){
        Scanner scanner = new Scanner(System.in);
        String[] userCommand = {"", ""};
        Status currentStatus = Status.OK;

        do {
            userCommand = (scanner.nextLine().trim() + " ").split(" ", 2);
            userCommand[1] = userCommand[1].trim();
            executeCommand(userCommand);
        } while (currentStatus != Status.EXIT);

        scanner.close();
    };


    private void executeCommand(String[] command) {
        if (validateRequest(command) == false) {
            return;
        }
        Response response = makeRequest(command[0], command[1]);

        console.print(response.status());
        var body = response.body();
        
        if (body.isEmpty() == false) {
            body.forEach((element) -> {
                console.print(element);
            });
        }
    }


    private boolean validateRequest(String[] command) {
        return (!(command.length == 0 || command[0].isEmpty()));
    }


    private Response makeRequest(String commandName, String argument) {
        Request request = new Request(commandName, argument);
        return remoteRuntime.proccessRequest(request);
    }
}
