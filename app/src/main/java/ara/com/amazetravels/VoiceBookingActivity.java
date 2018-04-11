package ara.com.amazetravels;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.CalendarContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import ara.com.amazetravels.ara.com.amazetravels.models.Booking;
import ara.com.amazetravels.ara.com.amazetravels.models.BookingTypes;
import ara.com.amazetravels.ara.com.amazetravles.http.HttpResponse;
import ara.com.amazetravels.ara.com.utils.AppConstants;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static ara.com.amazetravels.ara.com.utils.AppConstants.AUDIO_BOOKING_MIN;
import static ara.com.amazetravels.ara.com.utils.AppConstants.AUDIO_BOOKING_TIME;
import static ara.com.amazetravels.ara.com.utils.AppConstants.COUNT_DOWN_INTERVAL;

public class VoiceBookingActivity extends AppCompatActivity {
    private final String TAG = "Voice Booking Activity";
    private final String AUDIO_EXTENSION = ".mp3";
    private static final String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;

    @BindView(R.id.layout_scroll_view_voice_book)
    ScrollView scrollViewRoot;


    @BindView(R.id.btn_record)
    Button button_recording;

    @BindView(R.id.btn_play)
    Button button_play_stop;

    @BindView(R.id.pb_audio)
    ProgressBar progressBar;

    @BindView(R.id.voice_book_text_time)
    TextView textView_time;

    @BindView(R.id.btn_book_voice)
    Button button_voice_booking;

    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;

    Booking booking;


    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_booking);
        ButterKnife.bind(this);
        button_recording = (Button) findViewById(R.id.btn_record);
        booking = new Booking(AppConstants.getCurrentUser()
                , BookingTypes.VOICE);
        changeButtonState(button_play_stop,false);
        changeButtonState(button_voice_booking,false);
    }

    private void changeButtonState(Button btnControl, boolean state) {
        btnControl.setEnabled(state);
        if (state) {
            //Enable state
            btnControl.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.primary_dark,null));
        } else {
            btnControl.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.gray,null));
        }
    }

    private void initCountDownTimer(int secondsInFuture) {
        progressBar.setMax(secondsInFuture);

        textView_time.setText(secondsInFuture + "");

        countDownTimer = new CountDownTimer(secondsInFuture * COUNT_DOWN_INTERVAL
                , COUNT_DOWN_INTERVAL) {

            public void onTick(long millisUntilFinished) {
                int progress = (int) (millisUntilFinished / COUNT_DOWN_INTERVAL);
                progressBar.setProgress(progress);
                textView_time.setText(progress + "");
            }

            public void onFinish() {
                if (button_recording.getText().toString().compareToIgnoreCase("Stop") == 0) {
                    changeButtonState(button_play_stop,true);
                    changeButtonState(button_voice_booking,true);
                    button_recording.setText(R.string.record);
                    if (mediaRecorder != null) {
                        mediaRecorder.stop();
                        mediaRecorder.release();
                        mediaRecorder = null;
                    }
                }
                if (button_play_stop.getText().toString().compareToIgnoreCase("Stop") == 0) {
                    progressBar.setProgress(0);
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    changeButtonState(button_voice_booking,true);
                    changeButtonState(button_recording,true);


                    button_play_stop.setText(R.string.play);
                }

            }
        };
    }

    public void btnPlayStopOnClick(View view) {
        if (button_play_stop.getText().toString().compareToIgnoreCase("Stop") == 0) {
            countDownTimer.cancel();
            countDownTimer.onFinish();
        } else {

            changeButtonState(button_voice_booking,false);
            changeButtonState(button_recording,false);
            button_play_stop.setText(R.string.stop);

            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(booking.getAudioFileName());
                mediaPlayer.prepare();
                initCountDownTimer((int) (mediaPlayer.getDuration() / COUNT_DOWN_INTERVAL));
                countDownTimer.start();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }

            mediaPlayer.start();
            countDownTimer.start();
            Toast.makeText(VoiceBookingActivity.this, "Recorded Playing",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void btnRecordOnClick(View view) {
        if (checkPermission()) {

            if (button_recording.getText().toString().compareToIgnoreCase("Stop") == 0) {

                countDownTimer.cancel();
                countDownTimer.onFinish();

            } else {

                button_recording.setText(R.string.stop);
                changeButtonState(button_play_stop,false);
                changeButtonState(button_voice_booking,false);

                booking.setAudioFileName(
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                createRandomAudioFileName(5) + "AudioRecording.mp3"
                );

                mediaRecorderReady();

                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                    initCountDownTimer(3 * 60);
                    countDownTimer.start();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
            }
        } else {
            requestPermission();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (mediaRecorder != null) {

            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaRecorderReady();
        }

    }

    public void mediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(booking.getAudioFileName());
    }

    public String createRandomAudioFileName(int string) {
        Random random = new Random();
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
            showSnackBar(R.string.something_went_wrong_support);
        }
        if (response.getMesssage().compareToIgnoreCase(AppConstants.SUCCESS_MESSAGE) == 0) {
            if (booking.getAudioFileName() != null) {
                File file = new File(booking.getAudioFileName());
                file.delete();
            }
            Intent intent = new Intent();
            intent.putExtra("status", AppConstants.SUCCESS_MESSAGE);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void showSnackBar(int message) {
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

            MultipartBody multipartBody = booking.toMultipartBody();
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

            try {
                File file = new File(booking.getAudioFileName());
                file.deleteOnExit();
            } catch (Exception exception) {
                Log.e(TAG, "Fail cannot be deleted." + exception.getMessage());
            }

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
