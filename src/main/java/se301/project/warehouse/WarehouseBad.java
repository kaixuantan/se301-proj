package se301.project.warehouse;

public class WarehouseBad extends AbstractWarehouse {
    private static WarehouseBad warehouseObj;

    public static WarehouseBad getInstance() {
        if (warehouseObj == null) {
            warehouseObj = new WarehouseBad();
        }
        return warehouseObj;
    }
}
