package se301.project.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import se301.project.Shelf;
import se301.project.warehouse.Warehouse;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ExchangeTask implements Task {
  private final int shelfId1;
  private final int shelfId2;
  private final Warehouse warehouse;

  @Override
  public String execute() {
    Shelf shelf1 = warehouse.getInventory().get(shelfId1);
    Shelf shelf2 = warehouse.getInventory().get(shelfId2);

    Shelf firstShelf = shelf1;
    Shelf secondShelf = shelf2;

    if (shelf1.hashCode() > shelf2.hashCode()) {
      firstShelf = shelf2;
      secondShelf = shelf1;
    }

    firstShelf.getWriteLock().lock();
    secondShelf.getWriteLock().lock();
    try {
      if (shelf1 == null || shelf2 == null) {
        return "Shelf " + shelfId1 + " or " + shelfId2 + " does not exist.";
      }

      // exchange the items between the shelves
      Map<String, Integer> item1 = shelf1.takeItem();
      Map<String, Integer> item2 = shelf2.takeItem();

      shelf1.putItem(item2.keySet().iterator().next(), item2.values().iterator().next());
      shelf2.putItem(item1.keySet().iterator().next(), item1.values().iterator().next());
      return "Swapped " + item1.values().iterator().next() + " of " + item1.keySet().iterator().next() + " from shelf "
          + shelfId1 + " with " + item2.values().iterator().next() + " of " + item2.keySet().iterator().next()
          + " from shelf " + shelfId2;
    } finally {
      firstShelf.getWriteLock().unlock();
      secondShelf.getWriteLock().unlock();
    }
  }
}
