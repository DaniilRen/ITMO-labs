package common.transfer.request.wrapped;

import common.blueprints.UserData;
import common.transfer.request.Request;

public class AuthenticatedRequest extends HeaderRequest<UserData> {
    private static final long serialVersionUID = 27632999L;

    public AuthenticatedRequest(Request innerRequest) {
        super(innerRequest);
    }

    public AuthenticatedRequest(Request innerRequest, UserData headers) {
        super(innerRequest, headers);
    }

    public String getUserName() {
        return headers.user();
    }

    public String getPassword() {
        return headers.password();
    }
}
