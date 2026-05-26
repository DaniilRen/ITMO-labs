package model;

import java.io.IOException;
import java.util.List;

import common.exceptions.InvalidAttributesException;
import common.models.User;
import model.local.CommandHandler;
import model.network.AbstractClientNetwork;
import model.network.ClientNetwork;
import model.network.handlers.AuthHandler;
import view.View;

public class Model {
    private AbstractClientNetwork network;
    private AuthHandler authHandler;
    private View currentView;
    private CommandHandler commandHandler;

    public Model(View currentView) {
        this.currentView = currentView;
    }

    public void startConnection(String address, int port) {
        try {
            this.network = new ClientNetwork(address, port, currentView);
            network.connect();
            
            this.authHandler = new AuthHandler();

            this.commandHandler = new CommandHandler(currentView, authHandler, network);
            commandHandler.setCommandAttributes();

        } catch (IOException e) {
            currentView.displayError("Failed to connect to server: " + e.getMessage());
            return;
        } catch (InvalidAttributesException e) {
            currentView.displayError("(Init Request): " + e.getMessage());
            return;
        }
        currentView.displayMessage(String.format("<----- client started on port: %d ----->", port));
    }

    public void stopConnection() {
        network.close();
    }

    public void executeCommand(String commandName, List<?> args, boolean fileMode) {
        commandHandler.handleCommand(commandName, args, fileMode);
    }

    public void logOut() {
        authHandler.logOut();
    }

    public User getCurrentUser() {
        return authHandler.getCredentials();
    }
}
