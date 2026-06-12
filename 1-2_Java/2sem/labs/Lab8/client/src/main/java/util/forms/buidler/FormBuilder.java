package util.forms.buidler;

import common.models.Entity;
import common.models.User;

public abstract class FormBuilder {
    abstract public Entity buildEntity(String author);

    abstract public User buildUser(boolean newUser);
}
