package se301.project;

import java.util.Map;
import java.util.List;

public class Robot implements Runnable {
  private int id;
  private String itemName = "";
  private int quantity = 0;
  private Warehouse warehouse;
  private List<Task> taskQueue;

  public Robot(int id, List<Task> taskQueue) {
    this.id = id;
    this.taskQueue = taskQueue;
    this.warehouse = Warehouse.getInstance(); // singleton
    // this.warehouse = warehouse;
  }

  public void setItemName(String itemName) {
    this.itemName = itemName;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
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
    if (shelf1 == null || shelf2 == null) {
      System.out.println("Shelf " + shelfId1 + " or " + shelfId2 + " does not exist.");
      return;
    }

    // exchange the item between the shelves
    if (shelf1.getItemQty() == 0 || shelf1.getItemName() == null) {
      System.out.println("Shelf " + shelfId1 + " is empty.");
      return;
    }

    Map<String, Integer> item1 = shelf1.takeItem();

    // check if there is item in shelf2
    if (shelf2.getItemQty() != 0 && shelf2.getItemName() != null) {
      System.out.println("Shelf " + shelfId2 + " is not empty.");
      return;
    }

    shelf2.putItem(item1.keySet().iterator().next(), item1.values().iterator().next());

    // print the exchange
    System.out.println("Robot " + id + " has put items from shelf " + shelfId1 + " to shelf " + shelfId2);
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
