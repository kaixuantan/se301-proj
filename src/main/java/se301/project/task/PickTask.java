package se301.project.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import se301.project.shelf.Shelf;
import se301.project.warehouse.Warehouse;

@Getter
@RequiredArgsConstructor
public class PickTask implements Task {
  private final int shelfId;
  private final int quantity;
  private final Warehouse warehouse;

  @Override
  public String execute() {
    Shelf shelf = warehouse.getInventory().get(shelfId);
    if (shelf == null) {
       return "Shelf " + shelfId + " does not exist.";
    }

    // get the item from the shelf
    return shelf.deductQty(quantity);
  }
}
