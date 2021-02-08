package com.ishtari.ishtaristock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences sharedPreferences;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    Button btn_Search_product;
    Button btn_order_product;
    Button btn_change_UPC;
    Button btn_change_status;
    Button btn_given;
    Button btn_receive_instore;
    Button btn_complete_store;
    Button btn_faild;
    Button btn_back_to_stock;
    Button btn_received_in_stock;
    RelativeLayout activity_header;
    NavigationView nav;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        nav = findViewById(R.id.activity_hom_nav_view);
        activity_header = findViewById(R.id.activity_header_linearlayout);
        LayoutInflater inflater = getLayoutInflater();
        View v = nav.getHeaderView(0);
        btn_order_product = v.findViewById(R.id.hom_btn_order_product);
        btn_Search_product = v.findViewById(R.id.hom_btn_search_product);
        btn_change_UPC = v.findViewById(R.id.hom_btn_change_UPC);
        btn_change_status = v.findViewById(R.id.hom_btn_change_status);
        btn_given = v.findViewById(R.id.hom_btn_given);
        btn_receive_instore = v.findViewById(R.id.hom_btn_receive_instore);
        btn_complete_store = v.findViewById(R.id.hom_btn_complete_store);
        btn_faild = v.findViewById(R.id.hom_btn_faild);
        btn_back_to_stock = v.findViewById(R.id.hom_btn_back_to_stock);
        btn_received_in_stock = v.findViewById(R.id.hom_btn_received_in_stock);

        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_menu_24));
        toolbar.setTitle("Welcome " + sharedPreferences.getString("user_name", ""));

        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.activity_home_drawer_layout);

        toggle = new androidx.appcompat.app.ActionBarDrawerToggle(Home.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        btn_Search_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent d = new Intent(Home.this, Search_product.class);
                startActivity(d);
            }
        });
        btn_order_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent c = new Intent(Home.this, order_product.class);
                startActivity(c);
            }
        });
        btn_given.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this,Change_status.class);
                Bundle b = new Bundle();
                b.putString("action","given");
                i.putExtras(b);
                startActivity(i);
            }
        });

    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.hom_main_logout) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("logged", false);
            editor.putString("user_id", "0");
            editor.putString("user_name", "");
            editor.apply();
            Intent e = new Intent(this, MainActivity.class);
            startActivity(e);
            finish();

        } else if (id == R.id.hom_main_setting) {
            Intent v = new Intent(this, Setting.class);
            startActivity(v);
        }
        return super.onOptionsItemSelected(item);
    }
}