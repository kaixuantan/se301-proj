package se301.project.shelf;

import java.util.Map;
import java.util.concurrent.locks.Lock;

public interface Shelf {
    Lock getWriteLock();

    boolean deductQty(int quantity);

    boolean putItem(String itemName, int quantity);

    Map<String, Integer> takeItem();

    String getItemName();

    int getItemQty();

    String viewItem();
}
