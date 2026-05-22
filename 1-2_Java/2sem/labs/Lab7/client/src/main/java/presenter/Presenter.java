package presenter;

import java.util.List;

import common.transfer.Status;
import model.Model;

public class Presenter {
    private final Model model;

    public Presenter(Model model) {
        this.model = model;
    }

    public void connect(String address, int port) {
        this.model.startConnection(address, port);
    }

    public void disconnect() {
        this.model.stopConnection();
    }

    public Status executeCommand(String commandName, List<?> args, boolean fileMode) {
        return this.model.executeCommand(commandName, args, fileMode);
    }
}
