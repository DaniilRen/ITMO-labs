package common.transfer.request.standart;

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
}
