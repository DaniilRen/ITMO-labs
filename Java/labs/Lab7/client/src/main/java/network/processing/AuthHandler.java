package network.processing;

import common.transfer.request.Request;
import common.transfer.request.authenticated.AuthenticatedRequest;

public class AuthHandler {
    private boolean authenticated = false;
    private String name;
    private String password;

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public void setCredentials(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String[] getCredentials() {
        return new String[] {name, password};
    }

    public AuthenticatedRequest addCredentials(Request request) {
        String[] credentials = getCredentials();
        return new AuthenticatedRequest(request, credentials[0], credentials[1]);
    }
}
