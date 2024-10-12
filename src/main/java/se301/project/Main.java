package se301.project;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException{
        Warehouse warehouse = Warehouse.getInstance();

        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        warehouse.getInventory().put(1, new Shelf(1, "ItemA", 80));
        // warehouse.getInventory().put(2, new Shelf(2, "ItemB", 100));
        // warehouse.getInventory().put(3, new Shelf(3, "ItemC", 100));

        System.out.println(warehouse.getInventory().get(1).getItemQty());

        for (int i = 0; i < threadCount; i++) {
            Task pickItem = new PickTask(1, 10);
            List<Task> taskQueue = Collections.singletonList(pickItem);
            executorService.submit(
                new Robot(i, taskQueue, warehouse)
            );
        }
        
        // Thread t1 = new Thread(new Robot(1, Collections.singletonList(new PickTask(1, 10)), warehouse));
        // Thread t2 = new Thread(new Robot(2, Collections.singletonList(new PickTask(1, 10)), warehouse));
       
        // t1.run();
        // t2.run();

        // t1.join();
        // t2.join();

        
        executorService.shutdown();
        boolean terminated = executorService.awaitTermination(60, TimeUnit.SECONDS);
        

        System.out.println(warehouse.getInventory().get(1).getItemQty());
    }
    
}
