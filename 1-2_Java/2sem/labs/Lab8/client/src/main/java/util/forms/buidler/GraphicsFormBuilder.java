package util.forms.buidler;

import common.models.Entity;
import common.models.Route;
import common.models.User;

public class GraphicsFormBuilder extends FormBuilder {
    public Entity buildEntity(String author) {
        return new Route(author, null, null, null, null, 0, author);
    };

    public User buildUser(boolean newUser) {
        return new User(null, null, newUser);
    };
}
