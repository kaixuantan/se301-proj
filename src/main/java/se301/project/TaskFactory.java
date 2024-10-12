package se301.project;

import java.util.Random;

public class TaskFactory {
    private static final Random RANDOM = new Random();

    public static Task createExchangeTask(int shelfId1, int shelfId2) {
        return new ExchangeTask(shelfId1, shelfId2);
    }

    public static Task createPickTask(int shelfId, int quantity) {
        return new PickTask(shelfId, quantity);
    }

    public static Task createRandomTask() {
        int taskType = RANDOM.nextInt(2); // Assuming we have 2 types of tasks: PickTask and ExchangeTask
        switch (taskType) {
            case 0:
                return new PickTask(RANDOM.nextInt(4) + 1, RANDOM.nextInt(10)); // Random shelfId and quantity
            case 1:
                int shelfId1 = RANDOM.nextInt(4) + 1;
                int shelfId2;
                do {
                    shelfId2 = RANDOM.nextInt(4) + 1;
                } while (shelfId1 == shelfId2);
                return new ExchangeTask(shelfId1, shelfId2);
            default:
                throw new IllegalArgumentException("Unknown task type");
        }
    }
}
