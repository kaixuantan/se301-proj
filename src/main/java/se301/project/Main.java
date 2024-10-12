package se301.project;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import java.util.concurrent.ExecutorService;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.clear();
        warehouse.getInventory().put(1, new Shelf(1, "ItemA", 1000));
        warehouse.getInventory().put(2, new Shelf(2, "ItemB", 500));
        warehouse.getInventory().put(3, new Shelf(3, "ItemC", 50));
        warehouse.getInventory().put(4, new Shelf(4, "ItemD", 100));

        int threadCount = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < 50; i++) {
            int robotId = i + 1;
            List<Task> taskQueue = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                taskQueue.add(TaskFactory.createRandomTask());
            }
            executorService.submit(
                new Robot(robotId, taskQueue)
            );
        }

        executorService.shutdown();
        executorService.awaitTermination(60, TimeUnit.SECONDS);

        System.out.println(warehouse.display());
    }
}