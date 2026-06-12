package util.access;

import common.models.Route;
import common.models.User;

public final class ClientAccess {
  private ClientAccess() {}

  public static boolean canModify(Route route, User user) {
    if (route == null || user == null || user.getName() == null) {
      return false;
    }
    if (user.getIsAdmin()) {
      return true;
    }
    return route.getAuthor().equals(user.getName());
  }
}
