package pucp.telecom.moviles.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import pucp.telecom.moviles.lab3.Fragments.LocalDialogFragment;
import pucp.telecom.moviles.lab3.entidades.Data2;

public class MainActivity extends AppCompatActivity {


        public Data2 data2;

        public Data2 asignarData(double [] mediciones, double longitud, double latitud){
            int tiempo =mediciones.length;

            Data2 data = new Data2(longitud,latitud,mediciones ,tiempo);

            return data;
        }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }



    public void guardarEnLaNube(View view){
        String url = "http://ec2-34-234-229-191.compute-1.amazonaws.com:5000/saveData";
        String apikey= "KCDk3WQ4goIicvcus7xnGTXCZTbBxC";
        String content = "application/json";
        Data2 dataEnvio = asignarData(); //RECIBE 3P PARÁMETROS DE LA MEDICIÓN.
        Gson gson = new Gson();
        String data2AsJson = gson.toJson(data2);

        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("resul_nube", response);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("errorVol", error.getMessage());
                    }
                }) {




            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<>();
                return parametros;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> cabeceras = new HashMap<>();
                cabeceras.put("X-Api-Token",apikey);
                cabeceras.put("Content-Type",content);
                return cabeceras;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);


    }





    public void AgregarLocalDialogFragment(View view){
        LocalDialogFragment localDialogFragment = new LocalDialogFragment();
        localDialogFragment.show(getSupportFragmentManager(),"localDialogFragment");
    }


}