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

import pucp.telecom.moviles.lab3.Fragments.LocalDialogFragment;
import pucp.telecom.moviles.lab3.entidades.Data1;

public class MainActivity extends AppCompatActivity {

    public Data1 data1Ejemplo = new Data1();
    public void asignacionEjemplo(){
        data1Ejemplo.setTiempo(3);
        double[] datosDb = {60.2, 20.5, 32.5};
        data1Ejemplo.setSonidoDb(datosDb);
    }

    String dataAsJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ValidatePermissions();
        generateJson(data1Ejemplo);
        saveToDownloads();
    }


    public void generateJson(Data1 data){
        Gson gson = new Gson();
        asignacionEjemplo();

        dataAsJson = gson.toJson(data);
        Log.d("dataAsJson",dataAsJson);
    }

    public void saveToDownloads(){
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Log.d("Path",dir.getPath());

        // Le damos un nombre al archivo
        String fileName = "fileJson ";

        File file = new File(dir, "archivo1.txt");
        Log.d("fileName",file.getName());
        Log.d("filePath",file.getAbsolutePath());
        Log.d("file",file.toString());


        try (FileOutputStream fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
             FileWriter fileWriter = new FileWriter(fileOutputStream.getFD());) {

            // Escribimos con el lápiz la cadena de texto
            fileWriter.write(dataAsJson);
            Log.d("saveAsTextTry","Guardado exitoso");
        } catch (IOException e) {
            Log.e("saveAsTextCatch","Error al guardar");
            e.printStackTrace();
        }

        DownloadManager downloadManager = (DownloadManager) this.getSystemService(DOWNLOAD_SERVICE);
        downloadManager.addCompletedDownload(file.getName(), file.getName(), true, "text/plain",file.getAbsolutePath(),file.length(),true);
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

        }else{
            // si no tenemos permisos (permission = -1 = PERMISSION_DENIED)
            Log.d("PermissionStatus","No tenemos permisos de escritura");
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    codeWriteReadPermission);

        }
    }

    // valir si el usuario ha concedido o denegado permisos de lectura/escritura
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