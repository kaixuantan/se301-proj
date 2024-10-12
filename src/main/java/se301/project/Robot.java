package se301.project;

import java.util.Map;
import java.util.List;

public class Robot implements Runnable {
  private int id;
  private String itemName = null;
  private int quantity = 0;
  private Warehouse warehouse;
  private List<Task> taskQueue;

  public Robot(int id, List<Task> taskQueue) {
    this.id = id;
    this.taskQueue = taskQueue;
    this.warehouse = Warehouse.getInstance(); // singleton
  }

  public void setItemName(String itemName) {
    this.itemName = itemName;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public String getItemName() {
    return itemName;
  }

  public int getQuantity() {
    return quantity;
  }

  public void pickItemFromShelf(int shelfId, int quantity) {
    Shelf shelf = warehouse.getInventory().get(shelfId);
    if (shelf == null) {
      System.out.println("Shelf " + shelfId + " does not exist.");
      return;
    }

    // get the item from the shelf
    boolean deducted = shelf.deductQty(quantity);
    if (!deducted) {
      throw new IllegalArgumentException("Item " + shelf.getItemName() + " is out of stock or insufficient quantity.");
    }

  }

  public void exchangeItemBetweenShelf(int shelfId1, int shelfId2) {
    Shelf shelf1 = warehouse.getInventory().get(shelfId1);
    Shelf shelf2 = warehouse.getInventory().get(shelfId2);

    Shelf first = shelf1;
    Shelf second = shelf2;

    if (shelf1.hashCode() > shelf2.hashCode()) {
      first = shelf2;
      second = shelf1;
    }

    synchronized (first) {
      synchronized (second) {

        if (shelf1 == null || shelf2 == null) {
          System.out.println("Shelf " + shelfId1 + " or " + shelfId2 + " does not exist.");
          return;
        }

        // exchange the items between the shelves
        Map<String, Integer> item1 = shelf1.takeItem();
        System.out.println("Robot " + id + " took " + item1.values().iterator().next() + " of "
            + item1.keySet().iterator().next() + " from shelf " + shelfId1);
        Map<String, Integer> item2 = shelf2.takeItem();
        System.out.println("Robot " + id + " took " + item2.values().iterator().next() + " of "
            + item2.keySet().iterator().next() + " from shelf " + shelfId2);

        shelf1.putItem(item2.keySet().iterator().next(), item2.values().iterator().next());
        shelf2.putItem(item1.keySet().iterator().next(), item1.values().iterator().next());
      }
    }
  }

  @Override
  public void run() {
    for (Task task : taskQueue) {
      if (task instanceof PickTask) {
        PickTask pickTask = (PickTask) task;
        pickItemFromShelf(pickTask.getShelfId(), pickTask.getQuantity());
      } else if (task instanceof ExchangeTask) {
        ExchangeTask exchangeTask = (ExchangeTask) task;
        exchangeItemBetweenShelf(exchangeTask.getShelfId(), exchangeTask.getShelfId2());
      }
    }
  }
}
