package se301.project;

import java.util.*;

import lombok.Data;

@Data
public class Warehouse {
    private Map<Integer, Shelf> inventory = new HashMap<>();

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

    public static void main(String[] args) {
        // Create shared resources and robots
        Warehouse warehouse = new Warehouse();
        Map<Integer, Shelf> inventory = new HashMap<>();
        inventory.put(1, new Shelf(1, "ItemA", 10));
        inventory.put(2, new Shelf(2, "ItemB", 10));
        inventory.put(3, new Shelf(3, "ItemC", 10));
        warehouse.setInventory(inventory);

        // Robot tasks include picking items
        // Robot robot1 = new Robot(1, shelves.get(0), "ItemA", 3);
        // Robot robot2 = new Robot(2, shelves.get(0), "ItemA", 3);

        // Run robots in parallel
        // Thread robotThread1 = new Thread(robot1);
        // Thread robotThread2 = new Thread(robot2);

        // robotThread1.start();
        // robotThread2.start();
    }
}
