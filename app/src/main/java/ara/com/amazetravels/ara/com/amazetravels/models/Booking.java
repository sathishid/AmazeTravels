package ara.com.amazetravels.ara.com.amazetravels.models;


import java.io.File;
import java.util.Calendar;
import java.util.HashMap;

import ara.com.amazetravels.ara.com.utils.AppConstants;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Calendar getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Calendar appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Calendar getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Calendar appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public int getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(int vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
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
    public MultipartBody toMultipartBody(){
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        File file = new File(getAudioFileName());
        MediaType mediaType = MediaType.parse("audio/mpeg");
        builder.addFormDataPart("record_file", file.getName(), RequestBody.create(mediaType, file));
        builder.addFormDataPart("bookingtypeId", bookingType.getId()+"");
        builder.addFormDataPart("customerid", getCustomer().getCustomerId() + "");
        MultipartBody multipartBody = builder.build();
        return multipartBody;
    }


}
