package se301.project;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Robot implements Runnable {
    private int id;
    private Shelf shelf;
    private ChargingStation chargingStation;
    private Lock robotLock = new ReentrantLock();
    private String itemName;
    private int quantity;


    public Robot(int id, Shelf shelf, ChargingStation chargingStation, String itemName, int quantity) {
        this.id = id;
        this.shelf = shelf;
        this.chargingStation = chargingStation;
        this.itemName = itemName;
        this.quantity = quantity;
    }

    public void pickItemFromShelf() throws InterruptedException {
        if (shelf.getShelfLock().tryLock()) {
            try {
                System.out.println("Robot " + id + " is picking " + itemName + " from Shelf " + shelf.getShelfId());
                Thread.sleep(1000); // Simulate time to pick item
            } finally {
                shelf.getShelfLock().unlock();
            }
        } else {
            System.out.println("Robot " + id + " could not access Shelf " + shelf.getShelfId());
        }
    }

    public void chargeBattery() throws InterruptedException {
        if (chargingStation.getChargingLock().tryLock()) {
            try {
                System.out.println("Robot " + id + " is charging at Station " + chargingStation.getStationId());
                Thread.sleep(1000); // Simulate charging time
            } finally {
                chargingStation.getChargingLock().unlock();
            }
        } else {
            System.out.println("Robot " + id + " could not access Charging Station " + chargingStation.getStationId());
        }
    }

    @Override
    public void run() {
        try {
            // Trying to access shelf and charging station simultaneously (potential deadlock)
            pickItemFromShelf();
            chargeBattery();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
