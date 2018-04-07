package ara.com.amazetravels.ara.com.amazetravels.models;

public enum  BookingTypes {
    EMERGENCY(0,"Emergency Booking"),
    VOICE(1,"Voice Booking"),
    NON_VOICE(2,"Normal Booking");

    private int id;
    private String typeName;

    public int getId(){
        return id;
    }

    BookingTypes(int id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }
}
