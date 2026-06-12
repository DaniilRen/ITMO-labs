package model.local;

import java.util.ArrayList;
import java.util.List;

import common.models.Entity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CollectionStore {
  private final ObservableList<Entity> items = FXCollections.observableArrayList();
  private List<Entity> master = new ArrayList<>();

  public ObservableList<Entity> getItems() {
    return items;
  }

  public void setAll(List<Entity> entities) {
    master = new ArrayList<>(entities);
    items.setAll(master);
  }

  public List<Entity> getMaster() {
    return master;
  }

  public void applyFiltered(List<Entity> filtered) {
    items.setAll(filtered);
  }
}
