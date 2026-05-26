package model.network.handlers;

import common.models.User;
import common.transfer.request.Request;
import common.transfer.request.wrapped.AuthenticatedRequest;

public class AuthHandler {
    private boolean authenticated = false;
    private User credentials;

    public AuthHandler() {
        this.credentials = new User(null, null, false);
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setCredentials(String name, String password, boolean isAdmin) {
        this.credentials = new User(name, password, isAdmin);
        authenticated = true;
    }

    public User getCredentials() {
        return credentials;
    }

    public void logOut() {
        this.credentials = new User(null, null, false);
        authenticated = false;
    }

    public AuthenticatedRequest wrapCredentials(Request request) {
        return new AuthenticatedRequest(request, getCredentials());
    }
}
