package mx.com.gauta.usedoc.data;

import org.json.JSONObject;

import mx.com.gauta.usedoc.HttpManager;
import mx.com.gauta.usedoc.data.model.LoggedInUser;
import mx.com.gauta.usedoc.ui.login.LoginActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<JSONObject> login(String username, String password) {

        try {

            return null;
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
