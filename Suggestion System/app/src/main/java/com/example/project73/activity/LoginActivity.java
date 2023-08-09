package com.example.project73.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project73.R;
import com.example.project73.api.UserAPIModel;
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

    public String namauser;

    @JavascriptInterface
    public void printNama(String nama) {
        Log.d("Nama", nama);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        idNumberText = (EditText) findViewById(R.id.id_number_text);
        passwordText = (EditText) findViewById(R.id.password_text);
        loginButton = (ImageButton) findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameUser = idNumberText.getText().toString();
                String pass = passwordText.getText().toString();

                saveLoginStatus();
                UserAPIModel usr = new UserAPIModel();
                String pgn_username = usr.getNpk();

                // Jika Username Kosong
                if (TextUtils.isEmpty(usernameUser)){
                    Toast.makeText(LoginActivity.this,"Masukkan Username!", Toast.LENGTH_LONG).show();
                    return;
                }else if(TextUtils.isEmpty(pass)){
                    Toast.makeText(LoginActivity.this,"Masukkan Password!", Toast.LENGTH_LONG).show();
                }else {
                    new LoginTask().execute(usernameUser, pass);
//                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(i);
//                    finish();
                }


            }
        });
    }

    private class LoginTask extends AsyncTask<String, Void, String> {
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... params) {
            String usernameUser = params[0];
            String pass = params[1];
            String response = "";

            try {
                // Mendapatkan access token
                String accessTokenUrl = "https://api.polytechnic.astra.ac.id:2906/api_dev/AccessToken/Get";
                FormBody requestBody = new FormBody.Builder()
                        .add("username", usernameUser)
                        .add("password", pass)
                        .add("grant_type", "password")
                        .build();

                Request accessTokenRequest = new Request.Builder()
                        .url(accessTokenUrl)
                        .post(requestBody)
                        .build();

                Response accessTokenResponse = client.newCall(accessTokenRequest).execute();
                String accessTokenJson = accessTokenResponse.body().string();
                JSONObject accessTokenObj = new JSONObject(accessTokenJson);
                String accessToken = accessTokenObj.getString("access_token");
                accessTokenResponse.close();

                // Login dengan access token
                String loginUrl = "https://api.polytechnic.astra.ac.id:2906/api_dev/efcc359990d14328fda74beb65088ef9660ca17e/SIA/LoginSIA?username="
                        + usernameUser + "&password=" + pass;
                Request loginRequest = new Request.Builder()
                        .url(loginUrl)
                        .post(RequestBody.create(null, new byte[0])) // Tidak ada body request
                        .header("Authorization", "Bearer " + accessToken)
                        .build();

                Response loginResponse = client.newCall(loginRequest).execute();
                response = loginResponse.body().string();
                loginResponse.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            try {
                // Tindakan setelah berhasil login
                JSONObject resultObj = new JSONObject(response);

                // Mengambil data dari JSON response
                String result = resultObj.getString("result");
                String npk = resultObj.getString("npk");
                String username = resultObj.getString("username");
                String nama = resultObj.getString("nama");

                if (result.equalsIgnoreCase("TRUE")) {
                    // Membuat objek LoginResponse
                  //  LoginResponse loginResponse = new LoginResponse(result, npk, nama, username, role);

                // Membuat objek UserAPIModel
                UserAPIModel userAPIModel = new UserAPIModel(result, npk, username, nama);
                saveUserData(userAPIModel);

                // Menampilkan data objek di console
                Log.d("UserAPIModel", "Result: " + userAPIModel.getResult());
                Log.d("UserAPIModel", "NPK: " + userAPIModel.getNpk());
                Log.d("UserAPIModel", "Username: " + userAPIModel.getUsername());
                Log.d("UserAPIModel", "Nama: " + userAPIModel.getNama());


                // Tampilkan Toast berhasil di sini
                // Toast.makeText(getContext(), "Selamat Datang " + nama, Toast.LENGTH_SHORT).show();
                Toast.makeText( LoginActivity.this,
                        "Selamat Datang " +nama, Toast.LENGTH_SHORT).show();
                namauser=nama;
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();}else {
                    Toast.makeText( LoginActivity.this,
                            "Nomor Induk yang Anda Masukkan Tidak Terdaftar", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    private void navigateToMain() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void saveLoginStatus() {
        // Save the login status (logged in) using SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    private void saveUserData(UserAPIModel user) {
        // Save user data using SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("result", user.getResult());
        editor.putString("npk", user.getNpk());
        editor.putString("username", user.getUsername());
        editor.putString("nama", user.getNama());
        editor.apply();
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
