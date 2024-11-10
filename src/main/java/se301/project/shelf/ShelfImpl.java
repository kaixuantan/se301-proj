package se301.project.shelf;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class ShelfImpl implements Shelf {
  private final ReadWriteLock shelfLock = new ReentrantReadWriteLock();
  @Getter
  private final Lock writeLock = shelfLock.writeLock();
  @Getter
  private final Lock readLock = shelfLock.readLock();

  @Getter
  private final int shelfId;
  private String itemName;
  private int quantity;

  public ShelfImpl(int shelfId, String itemName, Integer quantity) {
    this.shelfId = shelfId;
    this.itemName = itemName;
    this.quantity = quantity;
  }

  @Override
  public void deductQty(int quantity) {
    writeLock.lock();
    try {
      if (this.quantity < quantity) {
        System.out.println(itemName + " is out of stock or insufficient quantity.");
//        return false;
      }

      delay();

      this.quantity -= quantity;
      System.out.println("Picked " + quantity + " of " + itemName + " from shelf " + shelfId);
    } finally {
      writeLock.unlock();
    }
  }

  @Override
  public boolean putItem(String itemName, int quantity) {
    writeLock.lock();
    try {
      this.itemName = itemName;
      // thread switch here will cause the viewItem() 
      // to read inconsistent data without a read lock
    //   delay();
      this.quantity = quantity;

      return true;
    } finally {
      writeLock.unlock();
    }
  }

  @Override
  public Map<String, Integer> takeItem() {
    writeLock.lock();
    try {
      Map<String, Integer> item = new HashMap<>();
      item.put(itemName, quantity);
      this.itemName = null;
      this.quantity = 0;

      // delay();

      return item;
    } finally {
      writeLock.unlock();
    }
  }

  @Override
  public String getItemName() {
    readLock.lock();
    try {
      return itemName;
    } finally {
      readLock.unlock();
    }
  }

  @Override
  public int getItemQty() {
    readLock.lock();
    try {
      return quantity;
    } finally {
      readLock.unlock();
    }
  }

  @Override
  public String viewItem() {
    readLock.lock();
    try {
      return "Item: " + itemName + ", Quantity: " + quantity;
    } finally {
      readLock.unlock();
    }
  }

  private void delay() {
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
