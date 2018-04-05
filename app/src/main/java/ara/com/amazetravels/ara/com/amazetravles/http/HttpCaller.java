package ara.com.amazetravels.ara.com.amazetravles.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ara.com.amazetravels.LoginActivity;
import ara.com.amazetravels.R;


/**
 * Created by sathishbabur on 1/31/2018.
 */

public class HttpCaller extends AsyncTask<HttpRequest, String, HttpResponse> {


    private static final String UTF_8 = "UTF-8";
    Context context;
    ProgressDialog progressDialog;
    public HttpCaller(Context context){
        super();
       this.context=context;


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
    }



    public void onPre(){

    }
    @Override
    protected HttpResponse doInBackground(HttpRequest... httpRequests) {
        HttpRequest httpRequest = httpRequests[0];
        HttpURLConnection urlConnection = null;
        HttpResponse response = null;
        try {
            urlConnection = createULRConnection(httpRequest);
            urlConnection.setConnectTimeout(5000);
            prepareBody(urlConnection, httpRequest);
            response = getResponse(urlConnection);

        } catch (Exception e) {
            progressDialog.dismiss();
            Log.e("Http URL", e.toString());
            response=new HttpResponse();
            response.setError();
            response.setMesssage(e.getMessage());
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return response;

    }


    @Override

    protected void onPostExecute(HttpResponse response) {

        super.onPostExecute(response);
        progressDialog.dismiss();
        onResponse(response);

    }


    public void onResponse(HttpResponse response) {


    }

    private HttpResponse getResponse(HttpURLConnection urlConnection) throws
            IOException {
        HttpResponse httpResponse = new HttpResponse();
        InputStream inputStream;
        // get stream
        if (urlConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
            httpResponse.setSuccess();

            inputStream = urlConnection.getInputStream();
        } else {
            httpResponse.setError();
            inputStream = urlConnection.getErrorStream();
        }
        // parse stream
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String tmpString;
        StringBuilder responseBuilder = new StringBuilder();
        while ((tmpString = bufferedReader.readLine()) != null) {
            responseBuilder.append(tmpString);
        }
        bufferedReader.close();
        inputStream.close();
        httpResponse.setMesssage(responseBuilder.toString());
        return httpResponse;
    }

    private void prepareBody(HttpURLConnection urlConnection, HttpRequest httpRequest) throws
            IOException {
        String requestBody = getDataString(httpRequest.getParams(), httpRequest.getMethodType());
        if(requestBody!=null) {
            OutputStream outputStream = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
            writer.write(requestBody);
            writer.flush();
            writer.close();
            outputStream.close();
        }
    }

    private HttpURLConnection createULRConnection(HttpRequest httpRequest) throws
            IOException {

        URL url = new URL(httpRequest.getUrl());
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        urlConnection.setRequestMethod(httpRequest.getMethodName());
        return urlConnection;
    }

    private String getDataString(HashMap<String, String> params, int methodType)
            throws UnsupportedEncodingException {
        switch (methodType) {
            case HttpRequest.GET:
                throw new UnsupportedEncodingException("Get Not supported yet");
            case HttpRequest.POST:
                Uri.Builder builder = new Uri.Builder();

                // encode parameters
                Iterator entries = params.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry entry = (Map.Entry) entries.next();
                    builder.appendQueryParameter(entry.getKey().toString(), entry.getValue().toString());
                    entries.remove();
                }
                String requestBody = builder.build().getEncodedQuery();
                return requestBody;
            default:
                throw new UnsupportedEncodingException("Method Type not support. Provide Get or POST");
        }
    }

}