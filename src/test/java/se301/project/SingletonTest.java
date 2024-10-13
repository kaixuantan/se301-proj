package se301.project;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import se301.project.factory.WarehouseFactory;
import se301.project.warehouse.Warehouse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SingletonTest {
    private static WarehouseFactory warehouseFactory;

    @BeforeAll
    public static void setUp() {
        warehouseFactory = new WarehouseFactory();
    }

    @Test
    public void testSingletonBehaviour_WhenGetInstance_ThenIsSameAsFirstGet() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        Map<Integer, Warehouse> map = new ConcurrentHashMap<>(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executorService.submit(() -> {
                map.put(index, warehouseFactory.create("good"));
            });
        }

        executorService.shutdown();
        boolean terminated = executorService.awaitTermination(60, TimeUnit.SECONDS);
        assertTrue(terminated);

        for (int i = 0; i < threadCount; i++) {
            assertEquals(map.get(0), map.get(i));
        }
    }

    // Test fails because multiple threads can enter the getInstance method simultaneously and create multiple instances of LRUCacheBadSingleton.
}
