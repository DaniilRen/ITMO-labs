package common.blueprints;

import java.io.Serializable;

public record UserData(String user, String password) implements Serializable {
    private static final long serialVersionUID = 9724818544L;

    public boolean validate() {
        return (user.length() > 0 && password.length() > 0);
    }
};
