package pucp.telecom.moviles.lab3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ConnectException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import pucp.telecom.moviles.lab3.Fragments.LocalDialogFragment;
import pucp.telecom.moviles.lab3.entidades.Data1;

public class MainActivity extends AppCompatActivity {

    public Data1 data1Ejemplo = new Data1();
    public void asignacionEjemplo(){
        data1Ejemplo.setTiempo(3);
        double[] datosDb = {60.2, 20.5, 32.5};
        data1Ejemplo.setMediciones(datosDb);
    }

    public String dataAsJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ValidatePermissions();
    }

    public void generateJson(Data1 data){
        Gson gson = new Gson();
        asignacionEjemplo();

        dataAsJson = gson.toJson(data);
        Log.d("dataAsJson",dataAsJson);
    }

    public void saveToDownloads(){

        Calendar calendar = Calendar.getInstance();
        String year = Integer.toString(calendar.get(calendar.YEAR));
        //String month = Integer.toString(calendar.get(calendar.MONTH));
        String monthFormat =String.format("%02d",calendar.get(Calendar.MONTH));
        //String day = Integer.toString(calendar.get(calendar.DAY_OF_MONTH));
        String dayFormat =String.format("%02d",calendar.get(Calendar.DAY_OF_MONTH));

        //String hour = Integer.toString(calendar.get(Calendar.HOUR));
        //String minute = Integer.toString(calendar.get(Calendar.MINUTE));
        String hourFormat = String.format("%02d", calendar.get(Calendar.HOUR));
        String minuteFormat = String.format("%02d", calendar.get(Calendar.MINUTE));

        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(dir, "_medicion_" + dayFormat + monthFormat + year + "_" + hourFormat + minuteFormat + ".json");

        // Write to file (oculto, solo visible desde la PC)
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.append(dataAsJson);
            Log.d("saveAsTextTry","Guardado exitoso");
        } catch (IOException e) {
            Log.e("saveAsTextCatch","Error al guardar");
        }
    }

    public void AgregarLocalDialogFragment(View view){
        LocalDialogFragment localDialogFragment = new LocalDialogFragment();
        localDialogFragment.show(getSupportFragmentManager(),"localDialogFragment");
    }


    // código para identificar quién abrió la solicitud de permiso
    private int codeWriteReadPermission = 1;

    // validar si tenemos permisos de lectura/escritura en shared storage
    public void ValidatePermissions(){
        // nivel de información para ver si tenemos o no permisos
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permission == PackageManager.PERMISSION_GRANTED){
            // si tenemos permisos (permission = 0 = PERMISSION_GRANTED)
            Log.d("PermissionStatus","Tenemos permisos de escritura");

            // si tenemos los permisos, obtener los datos
            generateJson(data1Ejemplo);
            saveToDownloads();
        }else{
            // si no tenemos permisos (permission = -1 = PERMISSION_DENIED)
            Log.d("PermissionStatus","No tenemos permisos de escritura");
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    codeWriteReadPermission);

        }
    }

    // validar si el usuario ha concedido o denegado permisos de lectura/escritura
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions
            , @NonNull int[] grantResults) {

        // Verificar si el requestCode es el definido para permisos de lectura escritura
        if (requestCode == codeWriteReadPermission){
            if(grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("PermissionResult","El usuario sí aceptó los permisos");
            }else{
                Log.d("PermissionResult","El usuario no aceptó los permisos");
            }
        }
    }
}