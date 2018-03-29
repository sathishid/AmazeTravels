package ara.com.amazetravels.ara.com.amazetravles.http;


import java.util.HashMap;

/**
 * Created by sathishbabur on 1/31/2018.
 */

public class HttpRequest {
    public static final int GET = 1;

    public static final int POST = 2;


    public HttpRequest() {
        methodtype = POST;
    }

    private String url;

    private int methodtype;


    private HashMap<String, String> params;


    public String getUrl() {

        return url;
    }


    public void setUrl(String url) {

        this.url = url;

    }


    public int getMethodType() {

        return methodtype;

    }


    public void setMethodtype(int methodtype) {

        this.methodtype = methodtype;

    }

    public String getMethodName() {
        switch (methodtype) {
            case GET:
                return "GET";
            case POST:
                return "POST";
            default:
                throw new UnsupportedOperationException("MethodType is not supported");
        }

    }

    public HashMap<String, String> getParams() {

        if (params == null) {
            params = new HashMap<>();
        }
        return params;

    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }
}

