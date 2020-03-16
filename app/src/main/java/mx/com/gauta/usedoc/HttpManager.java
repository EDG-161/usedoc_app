package mx.com.gauta.usedoc;

import android.app.Activity;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HttpManager extends AppCompatActivity{

     public static final String URL = "https://verify.usedoc.ml/";
    static  ArrayList<String> respond= new ArrayList<>();
    public static final String APIURL = "https://api.usedoc.ml/";

    public static ArrayList<String> makeRequest(final Map params, final String route, Activity a){

        RequestQueue queue = Volley.newRequestQueue(a);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL + route,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                HttpManager.respond= new ArrayList<>();
                HttpManager.respond.add("1");
                HttpManager.respond.add(response);
                System.out.println("respuesta " + route+" "+ respond.get(1));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("Error " + error.getMessage());
                HttpManager.respond.add("2");
                HttpManager.respond.add(error.getMessage());
            }
        }){
            protected Map<String, String> getParams() {
                Map<String, String> MyData = params;
                return MyData;
            }
        };
        queue.add(stringRequest);
        return HttpManager.respond;
    }

    public static ArrayList makeJSONRequest(final JSONObject params, final String route, final Activity a){
        final ArrayList respond = new ArrayList();
        respond.add("");respond.add("");
        RequestQueue queue = Volley.newRequestQueue(a);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL+route, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        respond.set(0,response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(a, error.getMessage(), Toast.LENGTH_SHORT).show();
                respond.set(1,error.getMessage());
                error.printStackTrace();
            }
        });
        queue.add(request);
        return respond;
    }

}
