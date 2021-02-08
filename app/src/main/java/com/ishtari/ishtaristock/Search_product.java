package com.ishtari.ishtaristock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class Search_product extends AppCompatActivity {
    ImageButton btn_back_search;
    EditText txt_search_product;
    TextView txt_SKU;
    TextView txt_code;
    TextView txt_Qt;
    TextView txt_UPC;
    TextView txt_barcode;
    TextView txt_fakeQuantity;
    TextView txt_inStand;
    ProgressBar progressBar;
    LinearLayout lnver;
    ImageView productImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_searchproduct);
        txt_SKU = findViewById(R.id.searchProduct_txt_SKU);
        txt_code = findViewById(R.id.searchProduct_txt_code);
        txt_Qt = findViewById(R.id.searchProduct_txt_QT);
        txt_UPC = findViewById(R.id.searchProduct_txt_UPC);
        txt_barcode = findViewById(R.id.searchProduct_txt_Barcode);
        txt_fakeQuantity = findViewById(R.id.searchProduct_txt_Fake);
        txt_inStand = findViewById(R.id.searchProduct_txt_InStand);
        progressBar = findViewById(R.id.searchProduct_progressBar);
        txt_search_product = findViewById(R.id.search_edit_text_search_product);
        btn_back_search = findViewById(R.id.btn_back_change_status);
        productImg = findViewById(R.id.product_image);
        btn_back_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txt_search_product.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    progressBar.setVisibility(View.VISIBLE);
                    search();


                    return true;
                }
                return false;
            }
        });
    }


    public void search() {
        String postUrl = "https://www.ishtari.com/stock_api_for_testing/product.php?item=" + txt_search_product.getText().toString();
        RequestQueue requestQueue = Volley.newRequestQueue(Search_product.this);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, postUrl,
                response -> {

                    try {
                        JSONObject result = new JSONObject(response);

                        if (result.getBoolean("success")) {
                            JSONObject productData = result.getJSONObject("product_data");
                            txt_SKU.setText(productData.getString("sku"));
                            txt_code.setText(productData.getString("product_id"));
                            txt_Qt.setText((productData.getString("quantity")));
                            txt_UPC.setText(productData.getString("upc"));
                            txt_barcode.setText(productData.getString("barcode"));
                            txt_fakeQuantity.setText(productData.getString("fake_quantity"));
                            txt_inStand.setText("In Stand:" + productData.getString("inStandQuantity"));
                            Picasso.with(Search_product.this).load(productData.getString("image")).into(productImg);
                        } else {

                            display(result.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(View.GONE);

                },
                error -> Toast.makeText(Search_product.this, error.toString(), Toast.LENGTH_LONG).show()) {


        };

        requestQueue.add(stringRequest);

    }

    public void display(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.show();

    }


}