package util.forms.buidler;

import java.util.Optional;

import common.models.Entity;
import common.models.Route;
import common.models.User;
import javafx.stage.Stage;
import view.gui.dialogs.RouteFormDialog;

public class GraphicsFormBuilder extends FormBuilder {
    private Stage ownerStage;
    private Route editingRoute;

    public void setOwnerStage(Stage ownerStage) {
        this.ownerStage = ownerStage;
    }

    public void setEditingRoute(Route route) {
        this.editingRoute = route;
    }

    @Override
    public Entity buildEntity(String author) {
        Optional<Route> route = RouteFormDialog.show(ownerStage, author, editingRoute);
        editingRoute = null;
        return route.orElse(null);
    }

  @Override
  public User buildUser(boolean newUser) {
    return new User(null, null, newUser);
  }
}
