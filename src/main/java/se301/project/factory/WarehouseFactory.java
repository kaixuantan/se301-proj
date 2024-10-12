package se301.project.factory;

import se301.project.warehouse.Warehouse;
import se301.project.warehouse.WarehouseBad;
import se301.project.warehouse.WarehouseGood;

public class WarehouseFactory implements Factory<Warehouse, String> {

    @Override
    public Warehouse create(String type) {
        if (type.equals("good")) {
            return WarehouseGood.getInstance();
        } else if (type.equals("bad")) {
            return WarehouseBad.getInstance();
        }

        return null;
    }
}
