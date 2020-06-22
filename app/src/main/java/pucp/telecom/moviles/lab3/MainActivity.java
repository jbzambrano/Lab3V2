package pucp.telecom.moviles.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void guardarEnLaNube(View view){
        String url = "http://ec2-34-234-229-191.compute-1.amazonaws.com:5000/saveData";
        String apikey= KCDk3WQ4goIicvcus7xnGTXCZTbBxC;

        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("resul_nube", response);


                        /*Gson gson = new Gson();
                        ResultTrabajo estado_Trabajo = gson.fromJson(response,ResultTrabajo.class);*/


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

                parametros.put("jobId",jobId);
                parametros.put("jobTitle",nombreTrabajo);
                parametros.put("minSalary",salarioMin);
                parametros.put("maxSalary",salarioMax);
                parametros.put("createdBy","grupo1");
                return parametros;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> cabeceras = new HashMap<>();
                cabeceras.put("api-key",apikey);
                return cabeceras;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);


    }




}