package view.console;

import java.util.List;
import java.util.Scanner;

import common.models.Entity;
import common.models.User;
import common.transfer.Status;
import util.forms.buidler.ConsoleFormBuidler;
import view.View;
import view.console.ui.IOConsole;
import presenter.PresenterFactory;

public class ConsoleView extends View {
    protected IOConsole console;
    protected Scanner scanner;
    protected ConsoleFormBuidler formBuidler;

    public void onCreate() {
        this.console = new IOConsole();
        this.formBuidler = new ConsoleFormBuidler(console);
        this.scanner = new Scanner(System.in);
        console.setUserScanner(scanner);

        this.presenter = PresenterFactory.providePresenter(this);
        runConsole();
    }

    public void onDestroy() {
        this.presenter.disconnect();
    }

    private void runConsole() {
        presenter.connect("localhost", 9000);

        String[] userCommand = {"", ""};

        while (true) {
            console.printPromptSymbol();

            userCommand = (scanner.nextLine().trim() + " ").split(" ", 2);
            userCommand[1] = userCommand[1].trim();
            
            String commandName = userCommand[0];
            List<?> args = List.of(userCommand[1]);
            if (args.size() == 1 && args.get(0).equals("")) args = List.of();

            Status commandStatus = executeCommand(commandName, args, false);
            if (commandStatus == Status.EXIT) break;
        }

        onDestroy();
    }

    public void displayError(String errorMessage) {
        this.console.printError(errorMessage);
    }

    public void displayMessage(String message) {
        this.console.println(message);
    }

    public Entity onEntityAdd(String author) {
        return formBuidler.buildEntity(author);
    };

    public User onUserAdd() {
        return formBuidler.buildUser();
    }
}
