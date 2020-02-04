package mx.com.gauta.usedoc.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.app.Activity;
import android.content.Intent;
import android.util.Patterns;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mx.com.gauta.usedoc.Home;
import mx.com.gauta.usedoc.HttpManager;
import mx.com.gauta.usedoc.data.LoginRepository;
import mx.com.gauta.usedoc.data.Result;
import mx.com.gauta.usedoc.data.model.LoggedInUser;
import mx.com.gauta.usedoc.R;
import mx.com.gauta.usedoc.user_active;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(final String username,final String password, final Activity activity) {
        RequestQueue queue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://verify.usedoc.ml/login",new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String user_json_str =response;
                System.out.println("Texto a JSON"+user_json_str);
                try {
                    JSONObject user = new JSONObject(""+user_json_str);
                    if (user.getInt("id_usr")>0){
                        Toast.makeText(activity,activity.getString(R.string.welcome) + user.getString("name_usr"),Toast.LENGTH_SHORT).show();
                        user_active.setUser_id(user.getInt("id_usr"));
                        user_active.setUser_name(user.getString("name_usr") + user.getString("appat_usr") + user.getString("apmat_usr"));
                        user_active.setUser_key(user.getString("key_usr"));
                        user_active.setUser_type(user.getInt("id_tid"));
                        user_active.setUser_mail(user.getString("email_usr"));
                        user_active.setUser_reg(user.getString("reg_usr"));
                        user_active.setUser_image(user.getString("img_usr"));
                        user_active.setUser_data(user.getString("dat_usr"));
                        user_active.saveUser(activity);
                        Intent intent = new Intent(activity, Home.class);
                        activity.startActivity(intent);
                        activity.finish();
                    }else{
                        Toast.makeText(activity,activity.getString(R.string.user_wrong).toString(),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity, LoginActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
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
        }){
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("email", username);

                params.put("pass", password);
                return params;
            }
        };queue.add(stringRequest);
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (!username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 8;
    }
}
