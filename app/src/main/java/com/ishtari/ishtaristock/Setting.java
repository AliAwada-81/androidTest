package com.ishtari.ishtaristock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Setting extends AppCompatActivity {
    EditText txt_token ;
    Button btn_save;
    SharedPreferences sharedPreferences;
    ImageButton btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        btn_back = findViewById(R.id.btn_back_change_status);
        txt_token = findViewById(R.id.setting_edit_text_token);
        btn_save = findViewById(R.id.setting_btn_save);
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        txt_token.setText(sharedPreferences.getString("txt_token",""));
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strValue = txt_token.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("txt_token",strValue);
                editor.apply();
                Toast.makeText(Setting.this, "success:save done", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}