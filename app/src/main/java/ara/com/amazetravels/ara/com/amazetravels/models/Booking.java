package ara.com.amazetravels.ara.com.amazetravels.models;


import java.util.Calendar;
import java.util.HashMap;

import ara.com.amazetravels.ara.com.utils.AppConstants;

/**
 * Created by Sathish Babu R on 28-03-2018.
 */

public class Booking {

    private int bookingId;

    private Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    private String userName;

    private String mobile;

    private int vehicleTypeId;

    private BookingTypes bookingType;

    private String place;

    private Calendar appointmentDate;

    private Calendar appointmentTime;

    private String audioFileName;

    public BookingTypes getBookingType() {
        return bookingType;
    }

    public void setBookingTypes(BookingTypes bookingType) {
        this.bookingType = bookingType;
    }


    public String getAudioFileName() {
        return audioFileName;
    }

    public Booking(Customer customer, BookingTypes bookingType, String audioFileName) {
        this(customer, bookingType);
        this.audioFileName = audioFileName;
    }

    public Booking(Customer customer, BookingTypes bookingType) {
        this(1, customer, null, null, -1, bookingType, null, null, null);
    }

    public Booking(int bookingId, Customer customer, String userName, String mobile, int vehicleTypeId,
                   BookingTypes bookingType, String place, Calendar date, Calendar time) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.userName = userName;
        this.mobile = mobile;
        this.vehicleTypeId = vehicleTypeId;
        this.bookingType = bookingType;
        this.place = place;
        this.appointmentDate = date;
        this.appointmentTime = time;
    }

    public HashMap<String, String> toHashMap() {
        HashMap<String, String> bookingHasMap = new HashMap<>(5);
        bookingHasMap.put("bookingId", this.bookingId + "");

        bookingHasMap.put("customerid", customer.getCustomerId() + "");

        if (this.userName != null && !this.userName.isEmpty())
            bookingHasMap.put("name", this.userName);

        if (this.mobile != null && !this.mobile.isEmpty())
            bookingHasMap.put("mobileno", this.mobile);

        if (vehicleTypeId != -1)
            bookingHasMap.put("vehicletype", this.vehicleTypeId + "");

        bookingHasMap.put("bookingtypeId", this.bookingType.getId() + "");

        if (this.place != null && !this.place.isEmpty())
            bookingHasMap.put("place", this.place);
        if (this.appointmentDate != null)
            bookingHasMap.put("appointmentDate", AppConstants.getStringDate(appointmentDate));
        if (appointmentTime != null) {
            bookingHasMap.put("appointmentTime", AppConstants.getStringTime(appointmentTime));
        }
        return bookingHasMap;
    }


}
