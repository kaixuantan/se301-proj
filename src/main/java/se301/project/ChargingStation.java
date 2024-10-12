package se301.project;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ChargingStation {
    private int stationId;
    private Lock chargingLock = new ReentrantLock();

    public ChargingStation(int stationId) {
        this.stationId = stationId;
    }

    public int getStationId() {
        return stationId;
    }

    public Lock getChargingLock() {
        return chargingLock;
    }
}
