package commands.util;

import common.models.Route;
import common.models.User;

public class AccessHandler {
    public static boolean accessVerified(Route route, User userData) {
        return route.getAuthor().equals(userData.getName());
    }
}