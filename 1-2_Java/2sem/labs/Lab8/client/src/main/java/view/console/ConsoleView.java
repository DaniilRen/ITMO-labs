package view.console;

import java.util.List;
import java.util.Scanner;

import common.models.Entity;
import common.models.User;
import util.forms.buidler.ConsoleFormBuilder;
import view.View;
import view.console.ui.IOConsole;
import presenter.Presenter;
import presenter.PresenterFactory;

public class ConsoleView implements View {
    private Presenter presenter;
    private IOConsole console;
    private Scanner scanner;
    private ConsoleFormBuilder formBuidler;
    private boolean running = false;

    public void onCreate() {
        this.console = new IOConsole();
        this.scanner = new Scanner(System.in);
        console.setUserScanner(scanner);

        this.presenter = PresenterFactory.providePresenter(this);

        this.formBuidler = new ConsoleFormBuilder(console);
        runConsole();
    }

    public void executeCommand(String commandName, List<?> args, boolean fileMode) {
        this.presenter.executeCommand(commandName, args, fileMode);
    }

    public void onDestroy() {
        displayMessage("Bye!");
        this.presenter.disconnect();
        running = false;
    }

    public void onLogOut() {
        displayMessage("Logged out");
        this.presenter.logOut();
    }

    private void runConsole() {
        presenter.connect("localhost", 9000);

        String[] userCommand = {"", ""};

        this.running = true;
        while (running) {
            console.printPromptSymbol();

            userCommand = (scanner.nextLine().trim() + " ").split(" ", 2);
            userCommand[1] = userCommand[1].trim();
            
            String commandName = userCommand[0];
            List<?> args = List.of(userCommand[1]);
            if (args.size() == 1 && args.get(0).equals("")) args = List.of();

            executeCommand(commandName, args, false);
        }
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

    public User onRegister() {
        return formBuidler.buildUser(true);
    }

    public User onLogin() {
        return formBuidler.buildUser(false);
    }

    public User getCurrentUser() {
        return presenter.getCurrentUser();
    };
}
