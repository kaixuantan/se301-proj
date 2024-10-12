package se301.project;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.ExecutorService;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Warehouse warehouse = Warehouse.getInstance();
        warehouse.clear();
        warehouse.getInventory().put(1, new Shelf(1, "ItemA", 100));

        int threadCount = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < 1000; i++) {
            final int idx = i;
            
            // Modify inventory
            executorService.submit(() -> {
                warehouse.getInventory().get(1).putItem(String.valueOf(idx), idx);
                
                String str = warehouse.getInventory().get(1).viewItem();
                Pattern pattern = Pattern.compile("Item: (.+?) Quantity: (\\d+)");
                Matcher matcher = pattern.matcher(str);

                if (matcher.find()) {
                    String itemName = matcher.group(1);
                    int quantity = Integer.parseInt(matcher.group(2));
                    if (Integer.valueOf(itemName) != quantity) {
                        System.out.println("Inconsistent: " + itemName + " " + quantity);
                    }
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(60, TimeUnit.SECONDS);
    }
}