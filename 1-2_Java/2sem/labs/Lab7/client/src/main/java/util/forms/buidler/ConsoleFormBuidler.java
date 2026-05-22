package util.forms.buidler;

import common.models.Entity;
import common.models.User;
import util.forms.RouteForm;
import util.forms.UserDataForm;
import view.console.ui.IOConsole;

public class ConsoleFormBuidler extends FormBuidler {
    private final IOConsole console;

    public ConsoleFormBuidler(IOConsole console) {
        this.console = console;
    }

    public Entity buildEntity(String author) {
        return new RouteForm(console, author, false).build();
    };

    public User buildUser() {
        return new UserDataForm(console, false).build();
    };
}
