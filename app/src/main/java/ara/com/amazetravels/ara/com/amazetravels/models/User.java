package ara.com.amazetravels.ara.com.amazetravels.models;

public class User {
    int userId;
    String userName;
    String mobileNo;

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public User(int userId, String userName, String mobileNo) {
        this.userId = userId;
        this.userName = userName;
        this.mobileNo = mobileNo;
    }

}