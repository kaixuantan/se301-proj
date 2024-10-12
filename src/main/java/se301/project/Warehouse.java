package se301.project;

import java.util.*;

import lombok.Data;

@Data
public class Warehouse {
    private static Map<String, Integer> inventory = new HashMap<>();
    private List<Shelf> shelves = new ArrayList<>();
    private List<ChargingStation> chargingStations = new ArrayList<>();

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
        Warehouse warehouse = new Warehouse();
        List<Shelf> shelves = new ArrayList<>();
        shelves.add(new Shelf(1, "ItemA", 10));
        shelves.add(new Shelf(2, "ItemB", 10));
        shelves.add(new Shelf(3, "ItemC", 10));
        warehouse.setShelves(shelves);
        ChargingStation chargingStation1 = new ChargingStation(1);

        // Robot tasks include picking items
        Robot robot1 = new Robot(1, shelves.get(0), chargingStation1, "ItemA", 3);
        Robot robot2 = new Robot(2, shelves.get(0), chargingStation1, "ItemA", 3);

        // Run robots in parallel
        Thread robotThread1 = new Thread(robot1);
        Thread robotThread2 = new Thread(robot2);

        robotThread1.start();
        robotThread2.start();
    }
}

