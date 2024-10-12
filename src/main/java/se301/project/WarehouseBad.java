package se301.project;

import java.util.HashMap;
import java.util.Map;

public class WarehouseBad {
    private Map<Integer, Shelf> inventory = new HashMap<>();
    private static WarehouseBad warehouseObj;

    public String display() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Shelf> entry : inventory.entrySet()) {
            sb.append("Shelf ID: ").append(entry.getKey()).append(", Item: ").append(entry.getValue().getItemName())
                    .append(", Quantity: ").append(entry.getValue().getItemQty()).append("\n");
        }
        return sb.toString();
    }

    public static WarehouseBad getInstance() {
        if (warehouseObj == null) {
            warehouseObj = new WarehouseBad();
        }
        return warehouseObj;
    }
}
