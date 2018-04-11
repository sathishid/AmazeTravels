package ara.com.amazetravels.ara.com.amazetravels.models;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;

import static ara.com.amazetravels.ara.com.utils.AppConstants.CUSTOMER_ID;
import static ara.com.amazetravels.ara.com.utils.AppConstants.CUSTOMER_NAME;
import static ara.com.amazetravels.ara.com.utils.AppConstants.MOBILE_NUMBER;

/**
 * Created by sathi on 28-03-2018.
 */

public class Customer implements Serializable {

    private int customerId;

    private String name;

    private String password;

    private String mobile;

    private String address;

    private String area;

    private String city;


    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", mobile='" + mobile + '\'' +
                ", address='" + address + '\'' +
                ", area='" + area + '\'' +
                ", city='" + city + '\'' +
                '}';
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public Customer(@NonNull int customerId, @NonNull String name, String password,@NonNull String mobile,
                    String address, String area, String city) {
        this.customerId = customerId;
        this.name = name;
        this.password = password;
        this.mobile = mobile;
        this.address = address;
        this.area = area;
        this.city = city;
    }

    public Customer(int customerId, String name, String mobile) {
        this(customerId, name, null, mobile, null, null, null);
    }

    public HashMap<String, String> toHashMap() {
        HashMap<String, String> customerHasMap = new HashMap<>(10);
        customerHasMap.put(CUSTOMER_ID, this.customerId + "");
        customerHasMap.put(CUSTOMER_NAME, this.name);
        customerHasMap.put("password", this.password);
        customerHasMap.put(MOBILE_NUMBER, this.mobile);
        customerHasMap.put("address", this.address);
        customerHasMap.put("area", this.area);
        customerHasMap.put("city", this.city);

        return customerHasMap;
    }
}
