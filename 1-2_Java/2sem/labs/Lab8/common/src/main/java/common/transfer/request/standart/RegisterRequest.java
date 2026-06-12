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
        if (args.size() != 3) {
            return false;
        }
        String name = toNonNullString(args.get(0)).trim();
        String password = toNonNullString(args.get(1));
        if (name.isEmpty() || password.isEmpty()) {
            return false;
        }
        Object adminArg = args.get(2);
        if (adminArg instanceof Boolean) {
            return true;
        }
        if (adminArg == null) {
            return false;
        }
        String adminStr = adminArg.toString().trim();
        return "true".equalsIgnoreCase(adminStr) || "false".equalsIgnoreCase(adminStr);
    }

    private static String toNonNullString(Object value) {
        return value == null ? "" : value.toString();
    }
}
