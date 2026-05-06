package common.transfer.request.standart;

import java.util.List;

/**
 * Запрос с данными пользователя
 * @author Septyq
 */
public class AuthRequest extends StandartRequest  {
    private final String userName;
    private final String passsword;

    public AuthRequest(String name, String userName, String passsword) {
        super(name);
        this.userName = userName;
        this.passsword = passsword;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return passsword;
    }

    public static boolean validate(List<?> args) {
        return (args.size() == 2 && args.get(0).toString().length() > 0 && args.get(1).toString().length() > 0);
    }
}
