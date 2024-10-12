package se301.project;

import java.util.*;

public class Warehouse {
    private static Map<String, Integer> inventory = new HashMap<>();

    // Initialize warehouse with some items
    static {
        inventory.put("ItemA", 10);
        inventory.put("ItemB", 5);
        inventory.put("ItemC", 8);
    }

    // Method to access the warehouse inventory (no synchronization here to simulate race condition)
    public static boolean pickItem(String itemName, int quantity) {
        Integer currentQuantity = inventory.get(itemName);
        if (currentQuantity == null || currentQuantity < quantity) {
            System.out.println("Item " + itemName + " is out of stock or insufficient quantity.");
            return false;
        }

        // Simulate a delay to highlight race conditions
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Update inventory without synchronization (race condition occurs here)
        inventory.put(itemName, currentQuantity - quantity);
        System.out.println("Picked " + quantity + " of " + itemName + ". Remaining: " + inventory.get(itemName));
        return true;
    }

    public static Map<String, Integer> getInventory() {
        return inventory;
    }

    public static void main(String[] args) {
        // Create shared resources and robots
        Shelf shelf1 = new Shelf(1);
        ChargingStation chargingStation1 = new ChargingStation(1);

        // Robot tasks include picking items
        Robot robot1 = new Robot(1, shelf1, chargingStation1, "ItemA", 3);
        Robot robot2 = new Robot(2, shelf1, chargingStation1, "ItemA", 3);

        // Run robots in parallel
        Thread robotThread1 = new Thread(robot1);
        Thread robotThread2 = new Thread(robot2);

        robotThread1.start();
        robotThread2.start();
    }
}

