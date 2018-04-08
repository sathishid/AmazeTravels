package ara.com.amazetravels;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

import ara.com.amazetravels.ara.com.amazetravels.models.Booking;
import ara.com.amazetravels.ara.com.amazetravels.models.BookingTypes;
import ara.com.amazetravels.ara.com.amazetravles.http.HttpResponse;
import ara.com.amazetravels.ara.com.utils.AppConstants;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class VoiceBookingActivity extends AppCompatActivity {
    private final String TAG = "Voice Booking Activity";
    private final String AUDIO_EXTENSION = ".mp3";


    Button buttonStart;

    Button buttonStop;
    Button buttonBookVoice;
    ScrollView scrollViewRoot;
    Button buttonPlayLastRecordAudio;

    Button buttonStopPlayingRecording;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder;
    Random random;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_booking);

        buttonStart = (Button) findViewById(R.id.btn_record);

        buttonStop = (Button) findViewById(R.id.btn_stop);
        buttonPlayLastRecordAudio = (Button) findViewById(R.id.btn_play);
        buttonStopPlayingRecording = (Button) findViewById(R.id.btn_stop_recording);
        scrollViewRoot = (ScrollView) findViewById(R.id.layout_scroll_view_voice_book);
        buttonBookVoice = (Button) findViewById(R.id.btn_book_voice);
        buttonStop.setEnabled(false);
        buttonPlayLastRecordAudio.setEnabled(false);
        buttonStopPlayingRecording.setEnabled(false);

        random = new Random();
        buttonBookVoice.setEnabled(false);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermission()) {

                    AudioSavePathInDevice =
                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                    CreateRandomAudioFileName(5) + "AudioRecording.mp3";

                    MediaRecorderReady();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    buttonStart.setEnabled(false);
                    buttonPlayLastRecordAudio.setEnabled(false);
                    buttonStop.setEnabled(true);

                    Toast.makeText(VoiceBookingActivity.this, "Recording started",
                            Toast.LENGTH_LONG).show();
                } else {
                    requestPermission();
                }

            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonBookVoice.setEnabled(true);
                mediaRecorder.stop();
                buttonStop.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);

                Toast.makeText(VoiceBookingActivity.this, "Recording Completed",
                        Toast.LENGTH_LONG).show();
            }
        });

        buttonPlayLastRecordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException,
                    SecurityException, IllegalStateException {
                buttonBookVoice.setEnabled(false);
                buttonStop.setEnabled(false);
                buttonStart.setEnabled(false);
                buttonStopPlayingRecording.setEnabled(true);

                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(AudioSavePathInDevice);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();
                Toast.makeText(VoiceBookingActivity.this, "Recording Playing",
                        Toast.LENGTH_LONG).show();
            }
        });

        buttonStopPlayingRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonStop.setEnabled(false);
                buttonBookVoice.setEnabled(true);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);

                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    MediaRecorderReady();
                }
            }
        });

    }

    public void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    public String CreateRandomAudioFileName(int string) {
        StringBuilder stringBuilder = new StringBuilder(string);
        int i = 0;
        while (i < string) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++;
        }
        return stringBuilder.toString();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(VoiceBookingActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(VoiceBookingActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(VoiceBookingActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    public void voice_book_ride_onClick(View view) {
        try {

            Booking booking = new Booking(AppConstants.getCurrentUser()
                    , BookingTypes.VOICE, AudioSavePathInDevice);

            bookRide(booking);

        } catch (Exception ex) {
            Snackbar.make(view, ex.getMessage(), Snackbar.LENGTH_LONG);
        }
    }

    private void bookRide(final Booking bookingArg) {

        new ImageUploadTask().execute(bookingArg);
    }


    private void onFailure(HttpResponse response) {
        if (response != null) {
            Toast.makeText(VoiceBookingActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
        }
    }

    private void onSuccess(HttpResponse response) {
        if (!response.getMesssage().contains(AppConstants.SUCCESS_MESSAGE)) {
            showSnackbar(R.string.something_went_wrong);
        }
        if (response.getMesssage().compareToIgnoreCase(AppConstants.SUCCESS_MESSAGE) == 0) {
            if (AudioSavePathInDevice != null) {
                File file = new File(AudioSavePathInDevice);
                file.delete();
            }
            Intent intent = new Intent();
            intent.putExtra("status", AppConstants.SUCCESS_MESSAGE);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void showSnackbar(int message) {
        final Snackbar snackbar = Snackbar.make(scrollViewRoot, message,
                Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.ok_button_text, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    class ImageUploadTask extends AsyncTask<Booking, Void, HttpResponse> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(VoiceBookingActivity.this);
            dialog.setTitle("Uploading Audio...");
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected HttpResponse doInBackground(Booking... bookings) {
            Booking booking = bookings[0];
            HttpResponse httpResponse = new HttpResponse();
            OkHttpClient client = new OkHttpClient();
            File file = new File(booking.getAudioFileName());
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);

            MediaType mediaType = MediaType.parse("audio/mpeg");
            builder.addFormDataPart("record_file", file.getName(), RequestBody.create(mediaType, file));
            builder.addFormDataPart("customerid", booking.getCustomer().getCustomerId() + "");
            MultipartBody multipartBody = builder.build();
            Request request = new Request.Builder()
                    .url(AppConstants.getBookingApi())
                    .post(multipartBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                httpResponse.setSuccessMessage(response.body().string());

            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                httpResponse.setErrorMessage(e.getMessage());
            }
            return httpResponse;
        }

        @Override
        protected void onPostExecute(HttpResponse httpResponse) {
            if (dialog.isShowing())
                dialog.dismiss();

            if (httpResponse.getStatus() == HttpResponse.Success) {
                Log.i(TAG, "----------" + httpResponse.getMesssage());
                onSuccess(httpResponse);
            } else {

                Log.e(TAG, "----------" + httpResponse.getMesssage());
                onFailure(httpResponse);
            }
        }


    }


}
