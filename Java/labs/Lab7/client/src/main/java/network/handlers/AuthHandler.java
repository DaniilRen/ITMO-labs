package network.handlers;

import common.models.User;
import common.transfer.request.Request;
import common.transfer.request.wrapped.AuthenticatedRequest;

public class AuthHandler {
    private boolean authenticated = false;
    private User credentials;

    public AuthHandler() {
        this.credentials = new User(null, null);
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setCredentials(String name, String password) {
        this.credentials = new User(name, password);
        authenticated = true;
    }

    public User getCredentials() {
        return credentials;
    }

    public void logOut() {
        this.credentials = new User(null, null);
        authenticated = false;
    }

    public AuthenticatedRequest wrapCredentials(Request request) {
        return new AuthenticatedRequest(request, getCredentials());
    }
}
