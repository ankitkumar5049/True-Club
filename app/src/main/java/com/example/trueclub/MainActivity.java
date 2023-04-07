package com.example.trueclub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.trueclub.model.Country;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText getName;
    Button submit;
    RecyclerView recyclerView;
    ArrayList<Country> list;
    RelativeLayout progressBar;
    RecyclerAdapter recyclerAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getName = findViewById(R.id.txtName);
        submit = findViewById(R.id.btnSubmit);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressLayout);

        layoutManager = new LinearLayoutManager(this);
        list = new ArrayList<>();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                funSubmit();
            }
        });
    }

    public void funSubmit(){
        progressBar.setVisibility(View.VISIBLE);
        String name = getName.getText().toString();
        getName.setText("");
        if(name==null || name.equals("")){
            Toast.makeText(this, "Enter a valid name", Toast.LENGTH_SHORT).show();
        }
        else{
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(submit.getWindowToken(), 0);
            api(name);
            recyclerAdapter = new RecyclerAdapter(list);
            recyclerView.setAdapter(recyclerAdapter);
            recyclerView.setLayoutManager(layoutManager);
        }
    }

    private void api(String name) {
        String url = "https://api.nationalize.io/?name="+name;

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        StringRequest
                stringRequest
                = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("TAG", "onResponse: "+response, null);
                        try {
                            JSONObject object = new JSONObject(response.toString());
                            JSONArray array = object.getJSONArray("country");

                            for(int i=0;i<array.length();i++){
                                JSONObject jo = array.getJSONObject(i);
                                String country_id = jo.getString("country_id");
                                String prob = String.valueOf(jo.getDouble("probability"));
                                list.add(new Country(country_id, prob));
                                Log.e("TAG", "onResponse: "+country_id+" "+prob, null);
                            }


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e("TAG", "onResponse: error", null);
                    }
                });
        requestQueue.add(stringRequest);

    }
}