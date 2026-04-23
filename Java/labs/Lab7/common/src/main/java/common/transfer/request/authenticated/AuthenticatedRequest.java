package common.transfer.request.authenticated;

import common.transfer.request.Request;

public class AuthenticatedRequest extends RequestWrapper {
    protected final String userName;
    protected final String password;

    public AuthenticatedRequest(Request wrappedRequest, String userName, String password) {
        super(wrappedRequest);
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
