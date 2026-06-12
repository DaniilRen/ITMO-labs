package commands.util;

import common.models.Route;
import common.models.User;

/**
 * Обработка прав доступа на исполнение команды
 * @author Septyq
 */
public class AccessHandler {
    public static boolean accessVerified(Route route, User userData) {
        
        if (userData.getIsAdmin()) return true;
        return route.getAuthor().equals(userData.getName());
    }
}