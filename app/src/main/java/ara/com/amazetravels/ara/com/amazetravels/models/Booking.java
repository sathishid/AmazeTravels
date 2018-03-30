package ara.com.amazetravels.ara.com.amazetravels.models;


import java.util.HashMap;

/**
 * Created by Sathish Babu R on 28-03-2018.
 */

public class Booking {

    private int bookingId;

    private String customerId;

    private String userName;

    private String mobile;

    private String vehicleTypeId;



    public Booking(int bookingId, String customerId, String userName, String mobile, String vehicleTypeId) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.userName = userName;
        this.mobile = mobile;
        this.vehicleTypeId = vehicleTypeId;

    }
    public HashMap<String, String> toHashMap() {
        HashMap<String, String> bookingHasMap = new HashMap<>(5);
        bookingHasMap.put("bookingId", this.bookingId + "");
        bookingHasMap.put("name", this.userName);
        bookingHasMap.put("customerid", this.customerId);
        bookingHasMap.put("mobileno", this.mobile);
        bookingHasMap.put("vehicletype", this.vehicleTypeId);


        return bookingHasMap;
    }
}
