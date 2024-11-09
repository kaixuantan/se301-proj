package se301.project;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import se301.project.factory.TaskFactory;
import se301.project.factory.WarehouseFactory;
import se301.project.robot.Robot;
import se301.project.shelf.ShelfImpl;
import se301.project.task.Task;
import se301.project.warehouse.Warehouse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConcurrencyTest {
    private WarehouseFactory warehouseFactory = new WarehouseFactory();
    private Warehouse warehouse;
    private TaskFactory taskFactory;

    @BeforeEach
    public void setUp() {
        this.warehouse = warehouseFactory.create("good");
        warehouse.clear();
        this.taskFactory = new TaskFactory(warehouse);
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
        warehouse.getInventory().put(1, new ShelfImpl(1, "ItemA", 100));

        for (int i = 0; i < 100; i++) {
            Task pickItem = taskFactory.createPickTask(1,1);
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
        warehouse.getInventory().put(1, new ShelfImpl(1, "ItemA", 90));
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            Task pickItem = taskFactory.createPickTask(1,10);
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

//        assertTrue(exceptionThrown, "Expected IllegalArgumentException was not thrown.");
        assertEquals(0, warehouse.getInventory().get(1).getItemQty());
    }

    @Test
    // comment out the readLock in Shelf.viewItem() to make the test fail
    // comment out the delay in Shelf.putItem() to make the test faster
    public void concurrentReadWriteToShelf_whenCheckShelf_ShelfDataConsistent() throws InterruptedException {
        warehouse.getInventory().put(1, new ShelfImpl(1, "Irrelevant", 0));

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
                    System.out.println(str);
                    Pattern pattern = Pattern.compile("Item: (.+?), Quantity: (\\d+)");
                    Matcher matcher = pattern.matcher(str);

                    if (matcher.find()) {
                        String itemName = matcher.group(1);
                        int quantity = Integer.parseInt(matcher.group(2));
                        assertEquals(Integer.valueOf(itemName), quantity);
                        if (Integer.valueOf(itemName) != quantity) {
                            System.out.println("Inconsistent: " + itemName + " " + quantity);
                        }
                    }
                })
            );
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

    @Test
    // comment our line 25 to 28 in ExchangeTask.execute() to make the
    // test fail (deadlock)
    // comment out delay() in Shelf.takeItem() and Shelf.putItem() to make the test
    // faster
    public void concurrentExchangeItem_NoDeadlocks() throws InterruptedException {
        warehouse.getInventory().put(1, new ShelfImpl(1, "ItemA", 100));
        warehouse.getInventory().put(2, new ShelfImpl(2, "ItemB", 50));
        warehouse.getInventory().put(3, new ShelfImpl(3, "ItemC", 20));

        int threadCount = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < 50; i++) {
            int robotId = i + 1;
            Task task1 = taskFactory.createExchangeTask(1, 2);
            Task task2 = taskFactory.createExchangeTask(2, 3);
            Task task3 = taskFactory.createExchangeTask(3, 1);
            executorService.submit(
                    new Robot(robotId, Collections.singletonList(task1)));
            executorService.submit(
                    new Robot(robotId, Collections.singletonList(task2)));
            executorService.submit(
                    new Robot(robotId, Collections.singletonList(task3)));
        }

        executorService.shutdown();
        executorService.awaitTermination(60, TimeUnit.SECONDS);
    }
}
