package se301.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.Future;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConcurrencyTest {
    private Warehouse warehouse;

    @BeforeEach
    public void setUp() {
        warehouse = Warehouse.getInstance();
        warehouse.clear();
    }

    @AfterEach
    public void tearDown() {
        warehouse.clear();
    }

    @Test
    // comment out the writeLock in Shelf.deductQty() to make the test fail
    public void concurrentPickItem_whenCheckInventory_InventoryConsistent() throws InterruptedException {
        int threadCount = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        warehouse.getInventory().put(1, new Shelf(1, "ItemA", 100));

        for (int i = 0; i < 100; i++) {
            Task pickItem = new PickTask(1, 1);
            List<Task> taskQueue = Collections.singletonList(pickItem);
            executorService.submit(
                    new Robot(i, taskQueue));
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
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            Task pickItem = new PickTask(1, 10);
            List<Task> taskQueue = Collections.singletonList(pickItem);
            futures.add(executorService.submit(
                    new Robot(i, taskQueue)));
        }

        executorService.shutdown();
        boolean terminated = executorService.awaitTermination(60, TimeUnit.SECONDS);
        assertTrue(terminated);

        boolean exceptionThrown = false;
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                if (e.getCause() instanceof IllegalArgumentException) {
                    exceptionThrown = true;
                    break;
                }
            }
        }

        assertTrue(exceptionThrown, "Expected IllegalArgumentException was not thrown.");
        assertEquals(0, warehouse.getInventory().get(1).getItemQty());
    }

    @Test
    // comment out the readLock in Shelf.viewItem() to make the test fail
    // comment out the delay in Shelf.putItem() to make the test faster
    public void concurrentReadWriteToShelf_whenCheckShelf_ShelfDataConsistent() throws InterruptedException {
        warehouse.getInventory().put(1, new Shelf(1, "ItemA", 100));

        int threadCount = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            final int idx = i;
            // Modify inventory
            futures.add(
                    executorService.submit(() -> {
                        warehouse.getInventory().get(1).putItem(String.valueOf(idx), idx);

                        String str = warehouse.getInventory().get(1).viewItem();
                        Pattern pattern = Pattern.compile("Item: (.+?) Quantity: (\\d+)");
                        Matcher matcher = pattern.matcher(str);

                        if (matcher.find()) {
                            String itemName = matcher.group(1);
                            int quantity = Integer.parseInt(matcher.group(2));
                            assertEquals(Integer.valueOf(itemName), quantity);
                            if (Integer.valueOf(itemName) != quantity) {
                                System.out.println("Inconsistent: " + itemName + " " + quantity);
                            }
                        }
                    }));

        }

        executorService.shutdown();
        executorService.awaitTermination(60, TimeUnit.SECONDS);

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                if (e.getCause() instanceof AssertionError) {
                    assertTrue(false, "AssertionError thrown");
                }
            }
        }
    }
}
