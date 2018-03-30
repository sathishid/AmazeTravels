package ara.com.amazetravels.ara.com.amazetravels.models;

/**
 * Created by sathi on 30-03-2018.
 */

public class VehicleType {
    int vehicleId;
    String vehicleName;

    @Override
    public String toString() {
        return "VehicleType{" +
                "vehicleId=" + vehicleId +
                ", vehicleName='" + vehicleName + '\'' +
                '}';
    }

    public VehicleType(int vehicleId, String vehicleName) {
        this.vehicleId = vehicleId;
        this.vehicleName = vehicleName;
    }

    public int getVehicleId() {

        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }
}
