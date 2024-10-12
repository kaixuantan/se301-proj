package se301.project;

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

    public boolean updateQty(int quantity) {
        wLock.lock();
        try {
            if (this.quantity < quantity) {
                System.out.println("Item " + itemName + " is out of stock or insufficient quantity.");
                return false;
            }
    
            // Simulate a delay to simulate the time taken to pick the item
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

    public boolean changeItem(String itemName, int quantity) {
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

