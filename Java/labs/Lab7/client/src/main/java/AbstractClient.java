import java.util.List;
import java.util.Scanner;

import common.transfer.Status;
import console.IOConsole;

public abstract class AbstractClient {
    protected IOConsole console;
    protected Scanner scanner;
    
    public AbstractClient() {
        this.console = new IOConsole();
        this.scanner = new Scanner(System.in);
        console.setUserScanner(scanner);
    }

    public abstract void run();

    protected void runConsoleParsing() {
        String[] userCommand = {"", ""};

        while (true) {
            console.printPromptSymbol();

            userCommand = (scanner.nextLine().trim() + " ").split(" ", 2);
            userCommand[1] = userCommand[1].trim();
            
            String commandName = userCommand[0];
            List<?> args = List.of(userCommand[1]);
            if (args.size() == 1 && args.get(0).equals("")) args = List.of();

            Status commandStatus = executeCommand(commandName, args);
            if (commandStatus == Status.EXIT) break;
        }
    };

    protected abstract Status executeCommand(String commandName, List<?> args);
}
