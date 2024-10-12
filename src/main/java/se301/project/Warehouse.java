package se301.project;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Data;

@Data
public class Warehouse {
    private Map<Integer, Shelf> inventory = new ConcurrentHashMap<>();

    public String display() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Shelf> entry : inventory.entrySet()) {
            sb.append("Shelf ID: ").append(entry.getKey()).append(", Item: ").append(entry.getValue().getItemName())
                    .append(", Quantity: ").append(entry.getValue().getItemQty()).append("\n");
        }
        return sb.toString();
    }

    public static Warehouse getInstance() {
        return SingletonHelper.INSTANCE;
    }

    private static class SingletonHelper {
        private static final Warehouse INSTANCE = new Warehouse();
    }

    public void clear() {
        this.inventory.clear();
    }
}
