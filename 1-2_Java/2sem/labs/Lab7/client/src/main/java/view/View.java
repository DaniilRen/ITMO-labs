package view;

import java.util.List;

import common.models.Entity;
import common.models.User;
import common.transfer.Status;
import presenter.Presenter;

public abstract class View {
    protected Presenter presenter;

    public Status executeCommand(String commandName, List<?> args, boolean fileMode) {
        return this.presenter.executeCommand(commandName, args, fileMode);
    }

    abstract public void onCreate();

    abstract public void onDestroy();

    abstract public void displayError(String errorMessage);

    abstract public void displayMessage(String errorMessage);

    abstract public Entity onEntityAdd(String author);

    abstract public User onUserAdd();
}
