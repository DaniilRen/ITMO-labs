package util.forms;

import common.exceptions.InvalidFormException;
import common.exceptions.InvalidScriptException;
import common.models.User;
import view.console.ui.IOConsole;

public class UserDataForm extends Form<User> {
    private final IOConsole console;
    private final boolean fileMode;
    private final boolean newUser;

    public UserDataForm(IOConsole console, boolean fileMode, boolean newUser) {
        this.console = console;
        this.fileMode = fileMode;
        this.newUser = newUser;
    }

    public User build() {
        try {
            User userData = new User(
                askName(),
                askPassword(),
                askIsAdmin()
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

    private boolean askIsAdmin() throws InvalidScriptException {
        if (!(newUser)) return false;

        Boolean isAdmin = false;
        boolean asked = false;
        do {
            try {
                console.print("Is admin? (y, n): ");
                String isAdminStr;
                isAdminStr = console.getUserScanner().nextLine().trim();
                if (fileMode) console.println(isAdminStr);
                isAdmin = checkBooleanFormat(isAdminStr);
                break;
            } catch (InvalidFormException e) {
                if (fileMode) {
                    asked = true;
                    throw new InvalidScriptException("Invalid input data in script -> operation stopped");
                } else {
                    console.printError("isAdmin not recognized, enter it again");
                }
            }
        } while (!asked);

        return isAdmin;
    }

    private boolean checkBooleanFormat(String booleanStr) throws InvalidFormException {
        switch (booleanStr.trim().toLowerCase()) {
            case "true":
            case "yes":
            case "y":
            case "t":
            case "1":
            case "on":
                return true;
            case "false":
            case "no":
            case "n":
            case "f":
            case "0":
            case "off":
                return false;  
            default:
                throw new InvalidFormException("invalid boolean value");
        }
    }
}
