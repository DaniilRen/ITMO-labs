package view;

import java.util.List;

import common.models.Entity;
import common.models.User;

public interface View {
    void executeCommand(String commandName, List<?> args, boolean fileMode);

    void onCreate();

    void onDestroy();

    void onLogOut();

    void displayError(String errorMessage);

    void displayMessage(String message);

    Entity onEntityAdd(String author);

    User onLogin();

    User onRegister();

    User getCurrentUser();

    default void setPendingAuthUser(User user) {}

    default void onCollectionReceived(List<Entity> entities) {
        entities.forEach(entity -> displayMessage(entity.toString()));
    }

    default void showTextDialog(String title, String content) {
        displayMessage(title + ":\n" + content);
    }

    default void refreshCollectionView() {}
}
