package mx.com.gauta.usedoc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mx.com.gauta.usedoc.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity  {

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);
        String name = "usedoc.db";
        db = this.openOrCreateDatabase(name,MODE_PRIVATE,null);
        initDB(this);


    }

    private void initDB(final Activity activity) {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS user_app(\n" +
                "\t_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\tuser_id TEXT,\n" +
                "\tuser_name TEXT,\n" +
                "\tuser_mail TEXT,\n" +
                "\tuser_type INTEGER,\n" +
                "\tuser_key TEXT,\n" +
                "\tuser_dat TEXT,\n" +
                "\tuser_reg TEXT,\n" +
                "\tuser_img TEXT\n" +
                ");";

        db.execSQL(sqlCreate);

        Cursor c = db.query("user_app",new String[]{"user_id","user_name","user_mail","user_type","user_key","user_reg","user_img","user_dat"},"'1'='1'",null,null,null,null);

        if (c.moveToNext()) {
            user_active.setUser_id(c.getString(0));
            user_active.setUser_name(c.getString(1));
            user_active.setUser_mail(c.getString(2));
            user_active.setUser_type(c.getInt(3));
            user_active.setUser_key(c.getString(4));
            user_active.setUser_reg(c.getString(5));
            user_active.setUser_image(c.getString(6));
            user_active.setUser_data(c.getString(7));
            System.out.println(user_active.getUser_key());
        }else{
            user_active.setUser_id("error");
            user_active.setUser_name("");
            user_active.setUser_mail("");
            user_active.setUser_type(0);
            user_active.setUser_key("");
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        final JSONObject json = new JSONObject();
        try {
            json.put("token",user_active.getUser_key());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, HttpManager.APIURL+"vuser", json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject user = response.getJSONObject("user");
                            System.out.println(user.toString());
                            if (user.getString("name")==null){
                                System.out.println(user.getString("name"));
                                user_active.setUser_id(null);
                            }
                            Intent intent;
                            System.out.println(user_active.getUser_id());
                            if (user.getString("name")!=null){
                                intent = new Intent(activity,Home.class);
                                user_active.getUserServer(activity,json.getString("token"));
                                startActivity(intent);
                                finish();
                            }else{
                                intent = new Intent(activity,LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            user_active.setUser_id(null);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();Intent intent;

                intent = new Intent(activity,Home.class);
                startActivity(intent);
                finish();
                user_active.setUser_id(null);
            }
        });
        queue.add(jsonObjectRequest);

    }


}
