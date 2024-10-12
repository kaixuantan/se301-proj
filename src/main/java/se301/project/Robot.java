package se301.project;

import java.util.List;

public class Robot implements Runnable {
  private int id;
  private String itemName = null;
  private int quantity = 0;
  private List<Task> taskQueue;

  public Robot(int id, List<Task> taskQueue) {
    this.id = id;
    this.taskQueue = taskQueue;
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

  @Override
  public void run() {
    for (Task task : taskQueue) {
      task.execute();
    }
  }
}
