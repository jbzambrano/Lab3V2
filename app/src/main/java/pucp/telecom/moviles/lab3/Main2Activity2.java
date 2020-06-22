package pucp.telecom.moviles.lab3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.Date;

public class Main2Activity2 extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Button btnRecord;
    private Button btnStop;
    private Button btnPlay;
    private double mEMA = 0.0;
    private MediaRecorder mediaRecorder ;
    private int codigoPermisoWriteReadSD = 1;
    //private MediaPlayer mediaPlayer;
    String outputFile;

    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    private final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main22);

        //------------------------------------------------------------------------------------------------------
        //PERMISOS
        if (ContextCompat.checkSelfPermission(Main2Activity2.this,
                Manifest.permission.RECORD_AUDIO) + ContextCompat.checkSelfPermission(Main2Activity2.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Main2Activity2.this,
                    Manifest.permission.RECORD_AUDIO) || ActivityCompat.shouldShowRequestPermissionRationale(Main2Activity2.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(Main2Activity2.this,
                        new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);



                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {


            // Permission has already been granted
            btnRecord = (Button) findViewById(R.id.buttonRecord);
            btnStop = (Button) findViewById(R.id.buttonStop);
           // btnPlay = (Button) findViewById(R.id.buttonPlay);
           // btnStop.setEnabled(false);
            //btnPlay.setEnabled(false);








        }


        //------------------------------------------------------------------------------------------------------




//---------------------------------------------------------------------------------------------------------
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (sensor != null) {
                Log.d("infoApp", "el dispositivo tiene acelerómetro");
            } else {
                Log.d("infoApp", "el dispositivo no tiene acelerómetro");
            }
            sensorManager.registerListener(Main2Activity2.this,sensor,SensorManager.SENSOR_DELAY_NORMAL);

            Log.d("infoApp22", "el dispositivo");
        }

//---------------------------------------------------------------------------------------------------------
    }

    public void Start(View view){
        Long date=new Date().getTime();
        Date current_time = new Date(Long.valueOf(date));
        mediaRecorder = new MediaRecorder();
        outputFile = getFilesDir() + "/"+current_time+".3gp";
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(outputFile);
        try {
            //mediaRecorder=new MediaRecorder();
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IllegalStateException ise) {
            // make something ...
        } catch (IOException ioe) {
            // make something
        }

        btnRecord.setEnabled(false);
        btnStop.setEnabled(true);
        Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
        mEMA = 0.0;

    }

    public void Stop(View view){

        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        btnRecord.setEnabled(true);
        btnStop.setEnabled(false);
        // play.setEnabled(true);
        Toast.makeText(getApplicationContext(), "Audio Recorder stopped", Toast.LENGTH_LONG).show();

    }


//---------------------------------------------------------------------------------------------------------


    @Override
    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();

        if (type == Sensor.TYPE_ACCELEROMETER) {
         //   Log.d("infoAppX:", String.valueOf(event.values[0]));
          //  Log.d("infoAppY:", String.valueOf(event.values[1]));

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (sensorManager != null) {
            Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            // sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //sensorManager.unregisterListener(this);
    }

}