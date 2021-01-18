package com.example.covid_19infoandnews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView city,country,confirmedCases,totalDeath,totalRecovered,newCases,criticalCases,activeCases;
    ImageView search,news;

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeVariable();
        getLocation();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SearchActivity.class));
            }
        });
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,NewsActivity.class));
            }
        });
    }

    private void initializeVariable(){
        city=findViewById(R.id.tv_city);
        country=findViewById(R.id.tv_country);
        confirmedCases=findViewById(R.id.confirmedCases);
        totalDeath=findViewById(R.id.totalDeath);
        totalRecovered=findViewById(R.id.totalRecovered);
        newCases=findViewById(R.id.newCases);
        criticalCases=findViewById(R.id.criticalCases);
        activeCases=findViewById(R.id.activeCases);
        search=findViewById(R.id.img_search);
        news=findViewById(R.id.img_news);
    }

    private void getLocation(){
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);

        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location=task.getResult();
                    if(location!=null){
                        Geocoder geocoder=new Geocoder(MainActivity.this, Locale.getDefault());
                        try {
                            List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

                            country.setText(Html.fromHtml(""+addresses.get(0).getCountryName()));
                            city.setText(Html.fromHtml(""+addresses.get(0).getLocality()));


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });

            fetchData();
        }else{
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }
    }

    private void fetchData() {
        String countryN=country.getText().toString();
        String url="https://disease.sh/v3/covid-19/countries/India?yesterday=true&twoDaysAgo=true&strict=true&allowNull=false";
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response.toString());
                    confirmedCases.setText(jsonObject.getString("cases"));
                    totalDeath.setText(jsonObject.getString("deaths"));
                    newCases.setText(jsonObject.getString("todayCases"));
                    totalRecovered.setText(jsonObject.getString("recovered"));
                    criticalCases.setText(jsonObject.getString("critical"));
                    activeCases.setText(jsonObject.getString("active"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Error : "+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}