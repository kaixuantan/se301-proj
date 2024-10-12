package se301.project.warehouse;

import lombok.Getter;
import se301.project.shelf.Shelf;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class AbstractWarehouse implements Warehouse {
    private final Map<Integer, Shelf> inventory = new HashMap<>();

    public String display() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Shelf> entry : inventory.entrySet()) {
            sb.append("Shelf ID: ").append(entry.getKey()).append(", Item: ").append(entry.getValue().getItemName())
                    .append(", Quantity: ").append(entry.getValue().getItemQty()).append("\n");
        }
        return sb.toString();
    }

    public void clear() {
        inventory.clear();
    }
}
