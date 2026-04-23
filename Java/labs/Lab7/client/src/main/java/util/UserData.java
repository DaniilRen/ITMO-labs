package util;

public record UserData(String user, String password) {
    public boolean validate() {
        return (user.length() > 0 && password.length() > 0);
    }
};
