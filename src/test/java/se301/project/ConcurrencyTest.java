package se301.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConcurrencyTest {
    private Warehouse warehouse;

    @BeforeEach
    public void setUp() {
        warehouse = Warehouse.getInstance();
    }

    @AfterEach
    public void tearDown() {
        warehouse.clear();
    }

    @Test
    public void concurrentPickItem_whenCheckInventory_InventoryConsistent() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        warehouse.getInventory().put(1, new Shelf(1, "ItemA", 100));

        for (int i = 0; i < threadCount; i++) {
            Task pickItem = new PickTask(1, 10);
            List<Task> taskQueue = Collections.singletonList(pickItem);
            executorService.submit(
                new Robot(i, taskQueue, warehouse)
            );
        }

        executorService.shutdown();
        boolean terminated = executorService.awaitTermination(60, TimeUnit.SECONDS);
        assertTrue(terminated);

        assertEquals(0, warehouse.getInventory().get(1).getItemQty());
    }

    @Test
    public void concurrentPickItem_whenNotEnoughStock_DoesNotDeduct() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        warehouse.getInventory().put(1, new Shelf(1, "ItemA", 90));

        for (int i = 0; i < threadCount; i++) {
            Task pickItem = new PickTask(1, 10);
            List<Task> taskQueue = Collections.singletonList(pickItem);
            executorService.submit(
                new Robot(i, taskQueue, warehouse)
            );
        }

        executorService.shutdown();
        boolean terminated = executorService.awaitTermination(60, TimeUnit.SECONDS);
        assertTrue(terminated);

        assertEquals(0, warehouse.getInventory().get(1).getItemQty());
    }
}
