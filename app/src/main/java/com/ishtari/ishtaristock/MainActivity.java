package com.ishtari.ishtaristock;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    ProgressBar progressBar;
    Button btn_login;
    EditText txt_email;
    EditText txt_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt_email = findViewById(R.id.activity_main_txt_email);
        txt_pass = findViewById(R.id.activity_main_txt_passwae);
        btn_login = findViewById(R.id.activity_main_btn_login);
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        progressBar = findViewById(R.id.activity_main_progress);

        txt_pass.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    clickLogin();
                    return true;
                }
                return false;
            }
        });
        if (sharedPreferences.getBoolean("logged", false)) {
            Intent i = new Intent(MainActivity.this, Home.class);
            startActivity(i);
            finish();
        } else {
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickLogin();
                }
            });
        }

    }

    public void login(String username, String password) {
        String postUrl = "https://www.ishtari.com/stock_api_for_testing/admin_login.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl,
                response -> {

                    try {
                        JSONObject result = new JSONObject(response);
                        if (result.getBoolean("success")) {
                            JSONObject userData = result.getJSONObject("user");
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("logged", true);
                            editor.putString("user_id", userData.getString("user_id"));
                            editor.putString("user_name", userData.getString("user_name"));
                            editor.apply();

                            Intent i = new Intent(MainActivity.this, Home.class);
                            startActivity(i);
                            finish();

                        } else {
                            btn_login.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            display(result.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                },
                error -> Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                return params;

            }

        };

        requestQueue.add(stringRequest);

    }

    public void display(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.show();

    }

    public void clickLogin() {
        if (txt_email.getText().toString().length() < 3) {
            display("Warning: user name most be grater then 3 characters!");
        } else if (txt_pass.getText().toString().length() < 5) {
            display("Warning: password most be grater then 5 characters!");
        } else {

            progressBar.setVisibility(View.VISIBLE);
            btn_login.setVisibility(View.GONE);
            login(txt_email.getText().toString(), txt_pass.getText().toString());
        }
    }

}