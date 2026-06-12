package common.transfer.request.standart;

import java.util.List;

import common.models.User;

public class RegisterRequest extends StandartRequest {
    private final String userName;
    private final String passsword;
    private final boolean isAdmin;

    public RegisterRequest(String name, String userName, String passsword, boolean isAdmin) {
        super(name);
        this.userName = userName;
        this.passsword = passsword;
        this.isAdmin = isAdmin;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return passsword;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public User getCredetials() {
        return new User(userName, passsword, isAdmin);
    }

    public static boolean validate(List<?> args) {
        return (args.size() == 3 && args.get(0).toString().length() > 0 && args.get(1).toString().length() > 0
            && args.get(2).toString().length() > 0);
    }
}
