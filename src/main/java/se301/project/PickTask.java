package se301.project;

public class PickTask implements Task {
  private int shelfId;
  private int quantity;
  private Warehouse warehouse = Warehouse.getInstance();

  public PickTask(int shelfId, int quantity) {
    this.shelfId = shelfId;
    this.quantity = quantity;
  }

  public int getShelfId() {
    return shelfId;
  }

  public int getQuantity() {
    return quantity;
  }

  @Override
  public void execute() {
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

}
