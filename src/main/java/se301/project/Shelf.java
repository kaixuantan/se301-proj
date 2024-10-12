package se301.project;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Shelf {
    private int shelfId;
    private String itemName;
    private int quantity;
    private ReadWriteLock shelfLock = new ReentrantReadWriteLock();
    private Lock rLock = shelfLock.readLock();
    private Lock wLock = shelfLock.writeLock();

    public Shelf(int shelfId, String itemName, Integer quantity) {
        this.shelfId = shelfId;
        this.itemName = itemName;
        this.quantity = quantity;
    }

    public boolean deductQty(int quantity) {
        wLock.lock();
        try {
            if (this.quantity < quantity) {
                System.out.println(itemName + " is out of stock or insufficient quantity.");
                throw new IllegalArgumentException("Item " + itemName + " is out of stock or insufficient quantity.");
            }
    
            // Simulate a delay to simulate the time taken to pick the item
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.quantity -= quantity;
            System.out.println("Deducted " + quantity + " of " + itemName + " from shelf " + shelfId + ". Remaining: " + this.quantity);
            return true;
        } finally {
            wLock.unlock();
        }
    }

    public boolean putItem(String itemName, int quantity) {
        wLock.lock();
        try {
            this.itemName = itemName;
            this.quantity = quantity;

            // Simulate a delay to simulate the time taken to exchange the item
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return true;
        } finally {
            wLock.unlock();
        }
    }

    public Map<String, Integer> takeItem() {
        wLock.lock();
        try {
           Map<String, Integer> item = new HashMap<>();
            item.put(itemName, quantity);
            this.itemName = null;
            this.quantity = 0;

            // Simulate a delay to simulate the time taken to clear the shelf
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return item;
        } finally {
            wLock.unlock();
        }
    }

    public String getItemName() {
        rLock.lock();
        try {
            return itemName;
        } finally {
            rLock.unlock();
        }
    }

    public int getItemQty() {
        rLock.lock();
        try {
            return quantity;
        } finally {
            rLock.unlock();
        }
    }

    public int getShelfId() {
        return shelfId;
    }
}

