package mx.com.gauta.usedoc;

import androidx.appcompat.app.AppCompatActivity;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mx.com.gauta.usedoc.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity  {

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent;

        String name = "usedoc.db";
        db = this.openOrCreateDatabase(name,MODE_PRIVATE,null);
        initDB();
        if (user_active.getUser_id()>0){
            intent = new Intent(this,Home.class);
            startActivity(intent);
            finish();
        }else{
            intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void initDB() {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS user_app(\n" +
                "\t_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\tuser_name TEXT,\n" +
                "\tuser_mail TEXT,\n" +
                "\tuser_type INTEGER,\n" +
                "\tuser_key TEXT,\n" +
                "\tuser_dat TEXT,\n" +
                "\tuser_reg TEXT,\n" +
                "\tuser_img TEXT\n" +
                ");";

        db.execSQL(sqlCreate);

        Cursor c = db.query("user_app",new String[]{"_id","user_name","user_mail","user_type","user_key","user_reg","user_img","user_dat"},"'1'='1'",null,null,null,null);

        if (c.moveToFirst()) {
            System.out.println("hay uno");
            user_active.setUser_id(c.getInt(0));
            user_active.setUser_name(c.getString(1));
            user_active.setUser_mail(c.getString(2));
            user_active.setUser_type(c.getInt(3));
            user_active.setUser_key(c.getString(4));
            user_active.setUser_reg(c.getString(5));
            user_active.setUser_image(c.getString(6));
            user_active.setUser_data(c.getString(7));
            System.out.println(user_active.getUser_key());
        }else{
            user_active.setUser_id(0);
            user_active.setUser_name("");
            user_active.setUser_mail("");
            user_active.setUser_type(0);
            user_active.setUser_key("");
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpManager.URL +"verify_session" ,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equalsIgnoreCase("verify_session_ok")){
                    user_active.setUser_id(0);
                }else{

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                user_active.setUser_id(0);
                System.out.println("Error " + error.getMessage());
            }
        }){
            protected Map<String, String> getParams() {
                Map<String, String> MyData =  new HashMap<String, String>();
                MyData.put("user_key",user_active.getUser_key());
                return MyData;
            }
        };
        queue.add(stringRequest);
    }


}
