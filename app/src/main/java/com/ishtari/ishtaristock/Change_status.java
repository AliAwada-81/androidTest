package com.ishtari.ishtaristock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Change_status extends AppCompatActivity {
    ImageButton btn_back_change_status;
    Button btn_reset;
    TextView txt_counter_change_status;
    EditText txt_enter_order_change_status;
    SharedPreferences sharedPreferences;
    Vibrator vib;
    Bundle b;
    String type;
    Spinner sp;
    String strspinner;
    TextView change_status_txt_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_status);
       b = getIntent().getExtras();
       type = b.getString("action");
        vib = (Vibrator) getSystemService(order_product.VIBRATOR_SERVICE);
        btn_back_change_status = findViewById(R.id.btn_back_change_status);
        btn_reset = findViewById(R.id.btn_reset_change_status);
        txt_enter_order_change_status = findViewById(R.id.txt_enter_order_change_status);
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        txt_counter_change_status = findViewById(R.id.txt_counter_change_status);
        sp = findViewById(R.id.spinner_change_order_status);
        change_status_txt_message = findViewById(R.id.chane_status_txt_message);
        String [] items = {"1111","22222","33333","444444","555555","6666666"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Change_status.this, android.R.layout.simple_spinner_item,items);
        sp.setAdapter(adapter);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_counter_change_status.setText("0");
            }
        });
        btn_back_change_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void search() {
        String postUrl = "http://www.ishtari.com/stockapi/updateorderstatus.php?order_id="+txt_enter_order_change_status.getText().toString()+"&type="+type+"&user_id="+sharedPreferences.getString("user_id","")+"&logistic_id=";

        RequestQueue requestQueue = Volley.newRequestQueue(Change_status.this);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, postUrl,
                response -> {

                    try {
                        JSONObject result = new JSONObject(response);
                        int counter = Integer.parseInt(txt_counter_change_status.getText().toString());

                        if (result.getBoolean("success")) {
                            counter++;
                            txt_counter_change_status.setText(String.valueOf(counter));
                            change_status_txt_message.setText("success");
                            change_status_txt_message.setTextColor(Color.RED);
                        } else {

                            change_status_txt_message.setText(result.getString("message"));
                            vib.vibrate(200);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    txt_counter_change_status.setText("");
                    txt_counter_change_status.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Change_status.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(txt_counter_change_status, InputMethodManager.SHOW_IMPLICIT);
                },
                error -> Toast.makeText(Change_status.this, error.toString(), Toast.LENGTH_LONG).show()) {


        };

        requestQueue.add(stringRequest);

    }
    public void display(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.show();

    }
}