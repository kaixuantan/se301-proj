package se301.project;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import java.util.concurrent.ExecutorService;
import java.util.Collections;

public class Main {
  public static void main(String[] args) throws InterruptedException {
    Warehouse warehouse = Warehouse.getInstance();
    warehouse.clear();
    warehouse.getInventory().put(1, new Shelf(1, "ItemA", 100));
    warehouse.getInventory().put(2, new Shelf(2, "ItemB", 10));
    warehouse.getInventory().put(3, new Shelf(3, "ItemC", 50));
    warehouse.getInventory().put(4, new Shelf(4, "ItemD", 20));

    int threadCount = 50;
    ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
    for (int i = 0; i < 50; i++) {
    int robotId = i + 1;
      executorService.submit(
          new Robot(robotId, Collections.singletonList(new ExchangeTask(1, 2))));
      executorService.submit(
          new Robot(robotId, Collections.singletonList(new ExchangeTask(2, 3))));
      executorService.submit(
          new Robot(robotId, Collections.singletonList(new ExchangeTask(3, 1))));
    }

    executorService.shutdown();
    executorService.awaitTermination(60, TimeUnit.SECONDS);

    System.out.println(warehouse.display());
  }
}