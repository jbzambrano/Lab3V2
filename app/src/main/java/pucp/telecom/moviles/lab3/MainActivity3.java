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
import android.location.Location;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity3 extends AppCompatActivity {
    private Button btnStart;
    private Button btnStop;
    TextView mStatusView;
    private SensorManager sensorManager;
    private MediaRecorder mediaRecorder;
    private final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1;
    private static double mEMA = 0.0;
    static final private double EMA_FILTER = 0.6;
     ArrayList<String> listaMediciones = new ArrayList<String>();
    double latitud;
    double longitud;
     String tiempo ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        //------------------------------------------------------------------------------------------------------
        //PERMISOS
        if (ContextCompat.checkSelfPermission(MainActivity3.this,
                Manifest.permission.RECORD_AUDIO) + ContextCompat.checkSelfPermission(MainActivity3.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity3.this,
                    Manifest.permission.RECORD_AUDIO) || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity3.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity3.this,
                        new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);


                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {


        }


    }

    public void Empezar(View view) {

        mStatusView = findViewById(R.id.status);
        listaMediciones.clear();
        Long x = Long.valueOf(1);
        Long y = Long.valueOf(2);
        startRecorder();

        Sonometro sonometro = new Sonometro();
        sonometro.execute(x, y);
        obtenerInfoUbicacion();
    }

    public void Stop(View view){

        stopRecorder();
    }


    class Sonometro extends AsyncTask<Long, Double, Double> {

        @Override
        protected void onPreExecute() {
            Toast.makeText(MainActivity3.this, "Iniciando Sonometro", Toast.LENGTH_SHORT).show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Double aLong) {

            double wacho = 0.0;
            int cant = listaMediciones.size();
            for(int i=0;i<cant;i++){
                wacho = wacho + Double.valueOf(listaMediciones.get(i));

            }
            double wacho1 = wacho/cant;
            int temp1 = (int)(wacho1*100.0);
            double wachin = ((double)temp1)/100.0;
            Toast.makeText(MainActivity3.this, "Resultado del ultimo es : " + wachin +"db y duracion es:"+ tiempo+" segundos", Toast.LENGTH_SHORT).show();
            //Toast.makeText(MainActivity3.this, "Duracion es : " + tiempo, Toast.LENGTH_SHORT).show();

            super.onPostExecute(aLong);
        }

        @Override
        protected void onProgressUpdate(Double... values) {

            mStatusView.setText(String.valueOf(values[0]) + " dB");
            String med = String.valueOf(values[0]);
            listaMediciones.add(med);
            Log.d("wa", String.valueOf(listaMediciones.size()));

        }


        @Override
        protected Double doInBackground(Long... longs) {
            double powerDb = 0;


            int time = 0;
            while(true) {

                if (mediaRecorder != null) {

                    powerDb = soundDb();
                    publishProgress(powerDb);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
                else{
                    break;
                }

                time++;
            }
            tiempo = String.valueOf(time);
            Log.d("wa2", tiempo);

            return powerDb;

        }

    }




    public double soundDb(){
        double valor = 20 * Math.log10(getAmplitudeEMA() / 2700.0);
        int temp = (int)(valor*100.0);
        double shortDouble = ((double)temp)/100.0;
        return shortDouble;
    }
    public double getAmplitude() {
        if (mediaRecorder != null) {

            return mediaRecorder.getMaxAmplitude();
        }
            else
            return 0;

    }
    public double getAmplitudeEMA() {
        double amp =  getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
    }

    public void startRecorder(){
        if (mediaRecorder == null)
        {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile("/dev/null");
            try
            {
                mediaRecorder.prepare();
            }catch (java.io.IOException ioe) {
                android.util.Log.e("[Monkey]", "IOException: " +
                        android.util.Log.getStackTraceString(ioe));

            }catch (java.lang.SecurityException e) {
                android.util.Log.e("[Monkey]", "SecurityException: " +
                        android.util.Log.getStackTraceString(e));
            }
            try
            {
                mediaRecorder.start();
            }catch (java.lang.SecurityException e) {
                android.util.Log.e("[Monkey]", "SecurityException: " +
                        android.util.Log.getStackTraceString(e));
            }

            //mEMA = 0.0;
        }

    }


    public void stopRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }

    }

    //---------------------------------------------------------------------------------------------------------






    //-----------------------------------------------------------------------------------------
    public void obtenerInfoUbicacion() {
        int permission =
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission == PackageManager.PERMISSION_GRANTED) {
            FusedLocationProviderClient fusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(this);



            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null) {
                        Log.d("infoApp", "lat: " + location.getLatitude());
                        latitud = location.getLatitude();
                        longitud = location.getLongitude();
                        Log.d("infoApp", "long: " + location.getLongitude());
                    }else{
                        Log.d("infoApp", "Estoy en un emulador :(");
                    }
                }
            });

            fusedLocationProviderClient.getLastLocation().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("infoApp", "Si me dieron los permisos :)");
                obtenerInfoUbicacion();
            } else {
                Log.d("infoApp", "No me dieron los permisos");
            }
        }


    }

}