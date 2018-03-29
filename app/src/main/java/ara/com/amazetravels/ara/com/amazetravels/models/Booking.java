package ara.com.amazetravels.ara.com.amazetravels.models;


/**
 * Created by Sathish Babu R on 28-03-2018.
 */

public class Booking {

    private int bookingId;

    private String customerId;

    private String userName;

    private String mobile;

    private String vehicleTypeId;

    private String tariff;

    public Booking(int bookingId, String customerId, String userName, String mobile, String vehicleTypeId, String tariff) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.userName = userName;
        this.mobile = mobile;
        this.vehicleTypeId = vehicleTypeId;
        this.tariff = tariff;
    }
}
