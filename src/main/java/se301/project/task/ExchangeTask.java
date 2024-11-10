package se301.project.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import se301.project.shelf.Shelf;
import se301.project.warehouse.Warehouse;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ExchangeTask implements Task {
    private final int shelfId1;
    private final int shelfId2;
    private final Warehouse warehouse;

    @Override
    public void execute() {
        Shelf shelfImpl1 = warehouse.getInventory().get(shelfId1);
        Shelf shelfImpl2 = warehouse.getInventory().get(shelfId2);

        Shelf firstShelf = shelfImpl1;
        Shelf secondShelf = shelfImpl2;

        if (shelfImpl1.hashCode() > shelfImpl2.hashCode()) {
            firstShelf = shelfImpl2;
            secondShelf = shelfImpl1;
        }

        try {
            firstShelf.getWriteLock().lock();
            try {
                secondShelf.getWriteLock().lock();
                if (shelfImpl1 == null || shelfImpl2 == null) {
                    System.out.println("Shelf " + shelfId1 + " or " + shelfId2 + " does not exist.");
                }

                // exchange the items between the shelves
                Map<String, Integer> item1 = shelfImpl1.takeItem();
                Map<String, Integer> item2 = shelfImpl2.takeItem();

                shelfImpl1.putItem(item2.keySet().iterator().next(), item2.values().iterator().next());
                shelfImpl2.putItem(item1.keySet().iterator().next(), item1.values().iterator().next());
                System.out.println("Swapped " + item1.values().iterator().next() + " of " + item1.keySet().iterator().next()
                        + " from shelf "
                        + shelfId1 + " with " + item2.values().iterator().next() + " of "
                        + item2.keySet().iterator().next()
                        + " from shelf " + shelfId2);
            } finally {
                secondShelf.getWriteLock().unlock();
            }
        } finally {
            firstShelf.getWriteLock().unlock();
        }
    }
}