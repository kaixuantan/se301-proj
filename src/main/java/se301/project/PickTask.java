package se301.project;

public class PickTask implements Task {
  private int shelfId;
  private int quantity;

  public PickTask(int shelfId, int quantity) {
    this.shelfId = shelfId;
    this.quantity = quantity;
  }

  @Override
  public int getShelfId() {
    return shelfId;
  }

  public int getQuantity() {
    return quantity;
  }

}
