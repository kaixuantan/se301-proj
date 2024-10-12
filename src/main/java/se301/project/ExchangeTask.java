package se301.project;

import java.util.Map;

public class ExchangeTask implements Task {
  private int shelfId1;
  private int shelfId2;
  private Warehouse warehouse = Warehouse.getInstance();

  public ExchangeTask(int shelfId1, int shelfId2) {
    this.shelfId1 = shelfId1;
    this.shelfId2 = shelfId2;
  }

  @Override
  public void execute() {
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
        System.out.println("Robot took " + item1.values().iterator().next() + " of "
            + item1.keySet().iterator().next() + " from shelf " + shelfId1);
        Map<String, Integer> item2 = shelf2.takeItem();
        System.out.println("Robot took " + item2.values().iterator().next() + " of "
            + item2.keySet().iterator().next() + " from shelf " + shelfId2);

        shelf1.putItem(item2.keySet().iterator().next(), item2.values().iterator().next());
        shelf2.putItem(item1.keySet().iterator().next(), item1.values().iterator().next());
      }
    }
  }

  public int getShelfId() {
    return shelfId1;
  }

  public int getShelfId2() {
    return shelfId2;
  }
}
