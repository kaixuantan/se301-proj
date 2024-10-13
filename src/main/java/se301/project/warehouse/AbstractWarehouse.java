package se301.project.warehouse;

import lombok.Getter;
import se301.project.shelf.Shelf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public abstract class AbstractWarehouse implements Warehouse {
    private final Map<Integer, Shelf> inventory = new HashMap<>();

    public String display() {
        // Get the keys and sort them
        List<Integer> sortedKeys = new ArrayList<>(inventory.keySet());
        Collections.sort(sortedKeys);

        StringBuilder displayResult = new StringBuilder();
        for (Integer key : sortedKeys) {
            Shelf shelf = inventory.get(key);
            try {
                shelf.getReadLock().lock();
                // Assuming Shelf has a toString method or similar to display its content
                displayResult.append("Shelf ").append(key).append(": ").append(shelf.viewItem()).append("\n");
            } finally {
                shelf.getReadLock().unlock();
            }
        }
        return displayResult.toString();
    }

    public void clear() {
        inventory.clear();
    }
}
