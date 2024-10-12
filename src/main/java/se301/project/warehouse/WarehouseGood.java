package se301.project.warehouse;

public class WarehouseGood extends AbstractWarehouse {
    public static WarehouseGood getInstance() {
        return SingletonHelper.INSTANCE;
    }

    private static class SingletonHelper {
        private static final WarehouseGood INSTANCE = new WarehouseGood();
    }
}
