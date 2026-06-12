package view;

import java.util.List;

import common.models.Entity;
import common.models.User;

public interface View {
    public void executeCommand(String commandName, List<?> args, boolean fileMode);

    public void onCreate();

    public void onDestroy();

    public void onLogOut();

    public void displayError(String errorMessage);

    public void displayMessage(String errorMessage);

    public Entity onEntityAdd(String author);

    public User onLogin();

    public User onRegister();

    public User getCurrentUser();
}
