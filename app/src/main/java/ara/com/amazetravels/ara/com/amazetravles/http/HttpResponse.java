package ara.com.amazetravels.ara.com.amazetravles.http;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sathishbabur on 1/31/2018.
 */

public class HttpResponse {
    public static final int ERROR = 1;
    public static final int Success = 2;

    private int status = ERROR;
    private String messsage;


    public int getStatus() {
        return status;
    }

    public void setSuccess() {
        setStatus(Success);
    }

    public void setError() {
        setStatus(ERROR);
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMesssage() {
        return messsage;
    }

    public JSONObject getJSONObject() throws JSONException {
        if (status == ERROR)
            return null;
        JSONObject jsonObject = new JSONObject(messsage);

        return jsonObject;
    }

    public void setMesssage(String messsage) {
        this.messsage = messsage;
    }

    public void setSuccessMessage(String messsge) {
        setSuccess();
        setMesssage(messsge);
    }

    public void setErrorMessage(String messsage) {
        setError();
        setMesssage(messsage);
    }


}
