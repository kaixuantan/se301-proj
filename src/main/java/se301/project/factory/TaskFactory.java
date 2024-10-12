package se301.project.factory;

import lombok.RequiredArgsConstructor;
import se301.project.task.ExchangeTask;
import se301.project.task.PickTask;
import se301.project.task.Task;
import se301.project.warehouse.Warehouse;

import java.util.Random;

@RequiredArgsConstructor
public class TaskFactory implements Factory<Task, String> {
    private static final Random RANDOM = new Random();
    private final Warehouse warehouse;

    @Override
    public Task create(String type) {
        switch (type) {
            case "exchange":
                return createExchangeTask(RANDOM.nextInt(4) + 1, RANDOM.nextInt(4) + 1);
            case "pick":
                return createPickTask(RANDOM.nextInt(4) + 1, RANDOM.nextInt(10));
            case "random":
                return createRandomTask();
            default:
                throw new IllegalArgumentException("Unknown task type");
        }
    }

    private Task createExchangeTask(int shelfId1, int shelfId2) {
        return new ExchangeTask(shelfId1, shelfId2, warehouse);
    }

    private Task createPickTask(int shelfId, int quantity) {
        return new PickTask(shelfId, quantity, warehouse);
    }

    private Task createRandomTask() {
        int taskType = RANDOM.nextInt(2);
        switch (taskType) {
            case 0:
                return new PickTask(RANDOM.nextInt(4) + 1, RANDOM.nextInt(10), warehouse);
            case 1:
                int shelfId1 = RANDOM.nextInt(4) + 1;
                int shelfId2;
                do {
                    shelfId2 = RANDOM.nextInt(4) + 1;
                } while (shelfId1 == shelfId2);
                return new ExchangeTask(shelfId1, shelfId2, warehouse);
            default:
                throw new IllegalArgumentException("Unknown task type");
        }
    }
}

