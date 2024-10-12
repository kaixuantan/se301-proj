package se301.project;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Shelf {
    private int shelfId;
    private Lock shelfLock = new ReentrantLock();

    public Shelf(int shelfId) {
        this.shelfId = shelfId;
    }

    public int getShelfId() {
        return shelfId;
    }

    public Lock getShelfLock() {
        return shelfLock;
    }
}

