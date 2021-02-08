package com.ishtari.ishtaristock;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import android.os.Vibrator;

public class order_product extends AppCompatActivity {
    ImageButton order_product_btn_back;
    TextView txt_counter;
    EditText txt_Enter_order;
    EditText txt_Enter_SKU;
    ArrayList<ListItems> items;
    MyorderAdapter myorderAdapter;
    ListView ls;
    Vibrator vib;
    Button complete;
    LinearLayout linearLayout;
    LayoutInflater lninflat;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_product);
        order_product_btn_back = findViewById(R.id.order_product_btn_back);
        txt_Enter_order = findViewById(R.id.txt_enter_order_change_status);
        txt_Enter_order.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(order_product.INPUT_METHOD_SERVICE);
        imm.showSoftInput(txt_Enter_order, InputMethodManager.SHOW_IMPLICIT);
        txt_Enter_SKU = findViewById(R.id.order_enter_SKU);
        txt_counter = findViewById(R.id.txt_counter_order_product);
        items = new ArrayList<ListItems>();
        myorderAdapter = new MyorderAdapter(items);
        ls = findViewById(R.id.list_view);
        ls.setAdapter(myorderAdapter);
        txt_Enter_order.setVisibility(View.VISIBLE);
        txt_Enter_SKU.setVisibility(View.GONE);
        lninflat = getLayoutInflater();
        view = lninflat.inflate(R.layout.row_view, null);
        linearLayout = view.findViewById(R.id.layout_order);
        complete = findViewById(R.id.btn_complete_order);
        vib = (Vibrator) getSystemService(order_product.VIBRATOR_SERVICE);
        order_product_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txt_Enter_order.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    order();
                    txt_Enter_order.setVisibility(View.GONE);
                    txt_Enter_SKU.setVisibility(View.VISIBLE);
                    txt_Enter_SKU.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(order_product.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(txt_Enter_SKU, InputMethodManager.SHOW_IMPLICIT);


                    return true;
                }
                return false;
            }
        });
        txt_Enter_SKU.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    Boolean isFound = true;
                    for (int i = 0; i < items.size(); i++) {
                        if ((items.get(i).Sku.equals(txt_Enter_SKU.getText().toString()) || items.get(i).barcode.equals(txt_Enter_SKU.getText().toString()) || items.get(i).id.equals(txt_Enter_SKU.getText().toString())) && (items.get(i).counter != items.get(i).quantity)) {

                            items.get(i).counter++;

                            ls.setAdapter(myorderAdapter);
                            complite();
                            return true;
                        } else {
                            isFound = false;

                        }
                    }

                    if (!isFound) {
                        display("product not found");
                        vib.vibrate(200);

                    }


                    return true;
                }
                return false;
            }
        });


    }

    class MyorderAdapter extends BaseAdapter {
        ArrayList<ListItems> items = new ArrayList<ListItems>();

        MyorderAdapter(ArrayList<ListItems> Items) {
            this.items = Items;

        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position).id;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater lninflat = getLayoutInflater();
            @SuppressLint("ViewHolder") View view = lninflat.inflate(R.layout.row_view, null);
            TextView txt_id = view.findViewById(R.id.txt_product_id);
            TextView txt_Sku = view.findViewById(R.id.txt_SKU_order);
            TextView txt_barcode = view.findViewById(R.id.txt_barcod_order);
            TextView txt_quantity = view.findViewById(R.id.txt_quantity);
            TextView txt_counter = view.findViewById(R.id.txt_counter_order_product);
            ImageView orderImage = view.findViewById(R.id.order_image);
            txt_id.setText(items.get(position).id + "");
            txt_Sku.setText(items.get(position).Sku);
            txt_barcode.setText(items.get(position).barcode);
            txt_quantity.setText(items.get(position).quantity + "");
            txt_counter.setText(items.get(position).counter + "");
            Picasso.with(order_product.this).load(items.get(position).imgUrl).into(orderImage);
            if (items.get(position).counter == items.get(position).quantity) {
                view.setBackgroundColor(Color.GREEN);


            }
            if (items.get(position).counter != items.get(position).quantity && items.get(position).counter != 0) {
                view.setBackgroundColor(Color.RED);


            }


            return view;
        }
    }

    public void order() {
        String postUrl = "https://www.ishtari.com/stock_api_for_testing/products_fullversion.php?order_id=" + txt_Enter_order.getText().toString();
        RequestQueue requestQueue = Volley.newRequestQueue(order_product.this);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, postUrl,
                response -> {

                    try {

                        JSONObject result = new JSONObject(response);

                        if (result.getBoolean("success")) {
                            JSONArray orderData = result.getJSONArray("products");
                            items = new ArrayList<>();
                            for (int i = 0; i < orderData.length(); i++) {
                                JSONObject product = orderData.getJSONObject(i);
                                items.add(new ListItems(
                                        product.getString("product_id"),
                                        product.getString("sku"),
                                        product.getString("barcode"),
                                        product.getInt("quantity"),
                                        product.getString("image"),
                                        0
                                ));

                            }

                            myorderAdapter = new MyorderAdapter(items);
                            ls.setAdapter(myorderAdapter);


                        } else {
                            display(result.getString("message"));

                        }
                    } catch (JSONException e) {
                        display(e.getMessage());
                    }

                },
                error -> Toast.makeText(order_product.this, error.toString(), Toast.LENGTH_LONG).show()) {


        };

        requestQueue.add(stringRequest);

    }

    public void display(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.show();

    }

    public void complite() {
        txt_Enter_SKU.setText("");


        InputMethodManager imm = (InputMethodManager) getSystemService(order_product.INPUT_METHOD_SERVICE);
        imm.showSoftInput(txt_Enter_SKU, InputMethodManager.SHOW_FORCED);
        txt_Enter_SKU.requestFocus();
        txt_Enter_SKU.beginBatchEdit();
        boolean complet = true;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).counter != items.get(i).quantity) {
                complet = false;
                break;
            }
        }

        if (complet) {
            complete.setVisibility(View.VISIBLE);
        }


    }
}
