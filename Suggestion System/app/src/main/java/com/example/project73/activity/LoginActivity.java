package com.example.project73.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project73.R;
import com.example.project73.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final String TAG_USER_API_MODEL = "UserAPIModel";

    private EditText idNumberText;
    private EditText passwordText;
    private ImageButton loginButton;

    @JavascriptInterface
    public void printName(String name){
        Log.d(TAG, "Got user name login : " + name);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        idNumberText = (EditText) findViewById(R.id.id_number_text);
        passwordText = (EditText) findViewById(R.id.password_text);
        loginButton = (ImageButton) findViewById(R.id.login_button);

        loginButton.setOnClickListener(v -> {
            String username = idNumberText.getText().toString();
            String password = passwordText.getText().toString();
            if (!isValidate(idNumberText, passwordText)) return;
            new LoginTask().execute(username, password);
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });
    }

    private class LoginTask extends AsyncTask<String, Void, String>{

        private OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            String response = "";

            try {
                String accessTokenUrl = "https://api.polytechnic.astra.ac.id:2906/api_dev/AccessToken/Get";
                FormBody requestBody = new FormBody.Builder()
                        .add("username", username)
                        .add("password", password)
                        .add("grant_type", "password")
                        .build();
                Request accessTokenRequest = new Request.Builder()
                        .url(accessTokenUrl)
                        .post(requestBody)
                        .build();

                Response accessTokenResponse = client.newCall(accessTokenRequest).execute();
                String accessTokenJson = accessTokenResponse.body().string();
                JSONObject jsonObject = new JSONObject(accessTokenJson);
                String accessToken = jsonObject.getString("access_tokem");
                accessTokenResponse.close();

                String loginUrl = "https://api.polytechnic.astra.ac.id:2906/api_dev/efcc359990d14328fda74beb65088ef9660ca17e/SIA/LoginSIA?username="
                        + username + "&password=" + password;
                Request loginRequest = new Request.Builder()
                        .url(loginUrl)
                        .post(RequestBody.create(null, new byte[0]))
                        .header("Authorization", "Bearer" + accessToken)
                        .build();
                Response loginResponse = client.newCall(loginRequest).execute();
                response = loginResponse.body().string();
                loginResponse.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            try {
                JSONObject resultObj = new JSONObject(response);

                String result = resultObj.getString("result");
                String npk = resultObj.getString("npk");
                String userName = resultObj.getString("username");
                String name = resultObj.getString("nama");

                User userAPIModel = new User(result, npk, userName, name);
                Log.d(TAG_USER_API_MODEL, "Result: " + userAPIModel.getResult());
                Log.d(TAG_USER_API_MODEL, "NPK: " + userAPIModel.getNpk());
                Log.d(TAG_USER_API_MODEL, "Username: " + userAPIModel.getUsername());
                Log.d(TAG_USER_API_MODEL, "Nama: " + userAPIModel.getNama());
            }catch (JSONException je){
                je.printStackTrace();
            }
        }
    }

    private boolean isValidate(EditText mTxtEmail,EditText mTxtPassword){

        boolean valid = true;


        if(mTxtEmail.getText().toString().length() == 0){
            mTxtEmail.setError("Required");
            valid = false;
        }
        if(mTxtPassword.getText().toString().length() == 0){
            mTxtPassword.setError("Required");
            valid = false;
        }

        return valid;
    }

}
