package se301.project.warehouse;

import se301.project.shelf.Shelf;

import java.util.Map;

public interface Warehouse {
    String display();

    Map<Integer, Shelf> getInventory();

    void clear();
}
