package common.transfer.request.wrapped;

import common.models.User;
import common.transfer.request.Request;

public class AuthenticatedRequest extends HeaderRequest<User> {
    private static final long serialVersionUID = 27632999L;

    public AuthenticatedRequest(Request innerRequest) {
        super(innerRequest);
    }

    public AuthenticatedRequest(Request innerRequest, User headers) {
        super(innerRequest, headers);
    }

    public String getUserName() {
        return headers.getName();
    }

    public String getPassword() {
        return headers.getPassword();
    }
}
