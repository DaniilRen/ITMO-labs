package util.forms.buidler;

import java.util.Scanner;

import common.models.Entity;
import common.models.User;
import view.console.ui.IOConsole;
import util.forms.RouteForm;
import util.forms.UserDataForm;

public class ScriptFormBuidler {
    private final IOConsole console;

    public ScriptFormBuidler(Scanner scriptScanner) {
        this.console = new IOConsole();
        console.setUserScanner(scriptScanner);
    }

    public Entity buildEntity(String author) {
        return new RouteForm(console, author, true).build();
    };

    public User buildUser(boolean newUser) {
        return new UserDataForm(console, true, newUser).build();
    };
}
