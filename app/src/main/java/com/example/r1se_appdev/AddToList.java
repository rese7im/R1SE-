package com.example.r1se_appdev;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AddToList extends AppCompatActivity {

    EditText editTextItem, editTextPrice;
    Button buttonAdd;
    String item, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_list);

        // Initialize UI elements
        editTextItem = findViewById(R.id.item);
        editTextPrice = findViewById(R.id.price);
        buttonAdd = findViewById(R.id.add);

        // Set click listener for add button
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve input values
                item = editTextItem.getText().toString().trim();
                price = editTextPrice.getText().toString().trim();

                // Basic input validation
                if (TextUtils.isEmpty(item) || TextUtils.isEmpty(price)) {
                    Toast.makeText(AddToList.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Send add item request
                addItem();
            }

            // Method to send a request to add an item to the list
            private void addItem() {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.1.109/R1SE-ANDROID-STUDIO/additem.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Handle server response
                                if (response.equals("success")){
                                    // If item is added successfully, show toast message
                                    Toast.makeText(AddToList.this, "Item added", Toast.LENGTH_SHORT).show();
                                } else {
                                    // If adding item fails, show error message
                                    Toast.makeText(AddToList.this, "Failed to add the item " + response, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle network errors
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            String errorResponse = new String(error.networkResponse.data);
                            Toast.makeText(AddToList.this, "Network error: " + errorResponse, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddToList.this, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        // Set parameters for the POST request
                        Map<String, String> params = new HashMap<>();
                        params.put("item", item);
                        params.put("price", price);
                        return params;
                    }
                };
                // Add the request to the RequestQueue
                queue.add(stringRequest);
            }
        });
    }
}
