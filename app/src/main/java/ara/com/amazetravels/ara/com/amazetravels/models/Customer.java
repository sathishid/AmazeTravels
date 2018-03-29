package ara.com.amazetravels.ara.com.amazetravels.models;

import java.io.Serializable;
import java.util.HashMap;

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

    public Customer(int customerId, String name, String password, String mobile,
                    String address, String area, String city) {
        this.customerId = customerId;
        this.name = name;
        this.password = password;
        this.mobile = mobile;
        this.address = address;
        this.area = area;
        this.city = city;
    }

    public HashMap<String, String> toHashMap() {
        HashMap<String, String> customerHasMap = new HashMap<>(10);
        customerHasMap.put("customerId", this.customerId + "");
        customerHasMap.put("name", this.name);
        customerHasMap.put("password", this.password);
        customerHasMap.put("mobileno", this.mobile);
        customerHasMap.put("address", this.address);
        customerHasMap.put("area", this.area);
        customerHasMap.put("city", this.city);

        return customerHasMap;
    }
}
