package network.handlers;

import common.blueprints.UserData;
import common.transfer.request.Request;
import common.transfer.request.wrapped.AuthenticatedRequest;

public class AuthHandler {
    private boolean authenticated = false;
    private UserData credentials;

    public AuthHandler() {
        this.credentials = new UserData(null, null);
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setCredentials(String name, String password) {
        this.credentials = new UserData(name, password);
        authenticated = true;
    }

    public UserData getCredentials() {
        return credentials;
    }

    public void logOut() {
        this.credentials = new UserData(null, null);
        authenticated = false;
    }

    public AuthenticatedRequest wrapCredentials(Request request) {
        return new AuthenticatedRequest(request, getCredentials());
    }
}
