package presenter;

import java.util.List;

import common.models.User;
import common.transfer.Status;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import model.Model;

public class Presenter {
    private final Model model;

		private final ExecutorService requestExecutor =
			Executors.newSingleThreadExecutor();

    public Presenter(Model model) {
        this.model = model;
    }

    public void connect(String address, int port) {
        this.model.startConnection(address, port);
    }

    public void disconnect() {
        this.model.stopConnection();
    }

    public void logOut() {
        model.logOut();
    }

    public void executeCommand(String commandName, List<?> args, boolean fileMode) {
    requestExecutor.submit(() -> {
        try {
            model.executeCommand(commandName, args, fileMode);
        } catch (Exception e) {
            model.getView().displayError(e.getMessage());
        }
    });
}

    public Status executeCommandWithStatus(String commandName, List<?> args, boolean fileMode) {
        return model.executeCommandWithStatus(commandName, args, fileMode);
    }

		public void shutdown() {
    requestExecutor.shutdownNow();
}

    public User getCurrentUser() {
        return model.getCurrentUser();
    }

    public boolean isAuthenticated() {
        return model.isAuthenticated();
    }

    public boolean isConnected() {
        return model.isConnected();
    }

    public boolean login(User user) {
        model.getView().setPendingAuthUser(user);
        Status status = model.executeCommandWithStatus("login", List.of(), false);
        return status == Status.LOGIN || model.isAuthenticated();
    }

    public boolean register(User user) {
        model.getView().setPendingAuthUser(user);
        Status status = model.executeCommandWithStatus("register", List.of(), false);
        return status == Status.LOGIN || model.isAuthenticated();
    }

    public void refreshCollection() {
        executeCommand("show", List.of(), false);
    }
}
