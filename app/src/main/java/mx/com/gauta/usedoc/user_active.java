package mx.com.gauta.usedoc;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import mx.com.gauta.usedoc.ui.login.LoginActivity;

public class user_active {

    private static String user_id;
    private static String user_name;
    private static  String user_mail;
    private static int user_type;
    private static String user_key;
    private static String user_reg;
    private static String user_image;
    private static String user_data;

    public static String getUser_id() {
        return user_id;
    }

    public static void setUser_id(String user_id) {
        user_active.user_id = user_id;
    }

    public static String getUser_name() {
        return user_name;
    }

    public static void setUser_name(String user_name) {
        user_active.user_name = user_name;
    }

    public static String getUser_mail() {
        return user_mail;
    }

    public static void setUser_mail(String user_mail) {
        user_active.user_mail = user_mail;
    }

    public static int getUser_type() {
        return user_type;
    }

    public static void setUser_type(int user_type) {
        user_active.user_type = user_type;
    }

    public static String getUser_key() {
        return user_key;
    }

    public static void setUser_key(String user_key) {
        user_active.user_key = user_key;
    }

    public static String getUser_reg() {
        return user_reg;
    }

    public static void setUser_reg(String user_reg) {
        user_active.user_reg = user_reg;
    }

    public static String getUser_image() {
        return user_image;
    }

    public static void setUser_image(String user_image) {
        user_active.user_image = user_image;
    }

    public static String getUser_data() {
        return user_data;
    }

    public static void setUser_data(String user_data) {
        user_active.user_data = user_data;
    }

    public static void saveUser(Activity activity) {
        SQLiteDatabase db;
        String name = "usedoc.db";
        db = activity.openOrCreateDatabase(name,activity.MODE_PRIVATE,null);
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_ID, user_active.getUser_id());
        values.put(FeedEntry.COLUMN_NAME, user_active.getUser_name());
        values.put(FeedEntry.COLUMN_MAIL, user_active.getUser_mail());
        values.put(FeedEntry.COLUMN_TYPE, user_active.getUser_type());
        values.put(FeedEntry.COLUMN_KEY, user_active.getUser_key());
        values.put(FeedEntry.COLUMN_REG, user_active.getUser_reg());
        values.put(FeedEntry.COLUMN_IMAGE, user_active.getUser_image());
        values.put(FeedEntry.COLUMN_DATA, (user_active.getUser_data()!=null)?user_active.getUser_data():"No hay");
        long newRowId = db.insert(FeedEntry.TABLE_NAME, null, values);
        System.out.println(newRowId);
        db.close();

    }

    public static void getUserServer(final Activity activity, final String token){
        JSONObject json = new JSONObject();
        try {
            json.put("token",token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(activity);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, HttpManager.APIURL+"vuser", json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject user = response.getJSONObject("user");
                            System.out.println(response.toString());
                            if (user.getString("name")!= null){
                                user_active.setUser_name(user.getString("name")+" "+user.getString("lastName"));
                                user_active.setUser_id(user.getString("_id"));
                                user_active.setUser_image(user.getString("imageRoute"));
                                user_active.setUser_mail(user.getString("email"));
                                user_active.setUser_key(token);
                                user_active.setUser_type((user.getString("userType").equalsIgnoreCase("paciente"))?2:1);
                                user_active.setUser_reg(user.getString("registerDate"));
                                user_active.saveUser(activity);
                                System.out.println(user_active.getUser_key());
                            }else{
                                Toast.makeText(activity,user.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(activity,activity.getString(R.string.login_failed).toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(activity,activity.getString(R.string.login_failed).toString(),Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }

    public static void logOff(Activity activity) {
        SQLiteDatabase db;
        String name = "usedoc.db";
        db = activity.openOrCreateDatabase(name,activity.MODE_PRIVATE,null);
        String selection = FeedEntry.COLUMN_KEY+ " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { user_active.getUser_key() };
        // Issue SQL statement.
        int deletedRows = db.delete(FeedEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
        Toast.makeText(activity, activity.getString(R.string.log_Off).toString(),Toast.LENGTH_LONG).show();
        Intent intent  = new Intent(activity,MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "user_app";
        public static final String COLUMN_ID = "user_id";
        public static final String COLUMN_NAME= "user_name";
        public static final String COLUMN_MAIL ="user_mail";
        public static final String COLUMN_TYPE = "user_type";
        public static final String COLUMN_KEY = "user_key";
        public static final String COLUMN_REG = "user_reg";
        public static final String COLUMN_IMAGE = "user_img";
        public static final String COLUMN_DATA = "user_dat";
    }
}
