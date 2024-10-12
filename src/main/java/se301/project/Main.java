package se301.project;

import se301.project.factory.Factory;
import se301.project.factory.RobotFactory;
import se301.project.factory.TaskFactory;
import se301.project.factory.WarehouseFactory;
import se301.project.robot.Robot;
import se301.project.shelf.ShelfImpl;
import se301.project.task.Task;
import se301.project.warehouse.Warehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        // get args
        if (args.length != 3) {
            System.out.println("Usage: mvn exec:java -Dexec.mainClass=\"se301.project.Main\" -Dexec.args=\"<good | bad> <number of threads> <number of tasks per robot>\"");
            System.out.println("Example: mvn exec:java -Dexec.mainClass=\"se301.project.Main\" -Dexec.args=\"good 50 5\"");
            System.exit(1);
        }

        String warehouseType = args[0];
        int threadCount;
        int numTasksPerRobot;
        try {
            threadCount = Integer.parseInt(args[1]);
            numTasksPerRobot = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid arguments. Please provide a valid number for thread count and number of tasks per robot.");
            System.exit(1);
            return;
        }

        Factory<Warehouse, String> warehouseFactory = new WarehouseFactory();
        Warehouse warehouse = warehouseFactory.create(warehouseType);
        Factory<Task, String> taskFactory = new TaskFactory(warehouse);

        setUpWarehouse(warehouse);
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        setUpTasks(threadCount, numTasksPerRobot, executorService, taskFactory);

        executorService.shutdown();
        boolean terminated = executorService.awaitTermination(60, TimeUnit.SECONDS);
        if (!terminated) {
            System.out.println("Some robots are still working after 60 seconds... shutting down now.");
            executorService.shutdownNow();
        }

        System.out.println(warehouse.display());
    }

    private static void setUpWarehouse(Warehouse warehouse) {
        warehouse.clear();
        warehouse.getInventory().put(1, new ShelfImpl(1, "ItemA", 1000));
        warehouse.getInventory().put(2, new ShelfImpl(2, "ItemB", 500));
        warehouse.getInventory().put(3, new ShelfImpl(3, "ItemC", 50));
        warehouse.getInventory().put(4, new ShelfImpl(4, "ItemD", 100));
    }

    private static void setUpTasks(int threadCount, int numTasksPerRobot, ExecutorService executorService, Factory<Task, String> taskFactory) {
        for (int i = 0; i < threadCount; i++) {
            int robotId = i + 1;
            List<Task> taskQueue = new ArrayList<>();

            for (int j = 0; j < numTasksPerRobot; j++) {
                taskQueue.add(taskFactory.create("random"));
            }

            Factory<Robot, Integer> robotFactory = new RobotFactory(taskQueue);
            Robot robot = robotFactory.create(robotId);
            executorService.submit(robot);
        }
    }
}