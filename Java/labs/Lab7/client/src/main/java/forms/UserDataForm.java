package forms;

import common.exceptions.InvalidFormException;
import common.exceptions.InvalidScriptException;
import console.IOConsole;
import util.UserData;

public class UserDataForm extends Form<UserData> {
    private final IOConsole console;
    private final boolean fileMode;

    public UserDataForm(IOConsole console) {
        this.console = console;
        this.fileMode = console.fileMode();
    }

    public UserData build() {
        try {
            UserData userData = new UserData(
                askName(),
                askPassword()
            );
            if (!(userData.validate())) throw new InvalidFormException("Final Route validation failed");
            return userData;
        } catch (InvalidFormException | InvalidScriptException e) {
            console.printError(e.getMessage());
            return null;
        }
    }

    private String askName() throws InvalidScriptException {
        String name = "";
        boolean asked = false;
        do {
            try {
                console.print("Enter username: ");
                name = console.getUserScanner().nextLine().trim();
                if (fileMode) console.println(name);
                if (name.equals("")) throw new InvalidFormException("");
                break;
            } catch (InvalidFormException e) {
                if (fileMode) {
                    asked = true;
                    throw new InvalidScriptException("Invalid input data in script -> operation stopped");
                } else {
                    console.printError("username not recognized, enter it again");
                }
            }
        } while (!asked);

        return name;
    };

    private String askPassword() throws InvalidScriptException {
        String name = "";
        boolean asked = false;
        do {
            try {
                console.print("Enter password: ");
                name = console.getUserScanner().nextLine().trim();
                if (fileMode) console.println(name);
                if (name.equals("")) throw new InvalidFormException("");
                break;
            } catch (InvalidFormException e) {
                if (fileMode) {
                    asked = true;
                    throw new InvalidScriptException("Invalid input data in script -> operation stopped");
                } else {
                    console.printError("password not recognized, enter it again");
                }
            }
        } while (!asked);

        return name;
    };
}
