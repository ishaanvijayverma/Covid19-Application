package com.example.covid_19infoandnews;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    TextInputLayout searchBar;
    ListView listView;

    public static List<CountryDataModel> countryDataModelList=new ArrayList<>();
    CountryDataModel countryDataModel;
    CountryDataAdapter countryDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initializeVariable();

        fetchData();
    }

    private void fetchData() {
        String url="https://disease.sh/v3/covid-19/countries/";
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray=new JSONArray(response);

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);

                        String countryName=jsonObject.getString("country");
                        String cases=jsonObject.getString("cases");
                        String todayCases=jsonObject.getString("todayCases");
                        String deaths=jsonObject.getString("deaths");
                        String todayDeaths=jsonObject.getString("todayDeaths");
                        String recovered=jsonObject.getString("recovered");
                        String active=jsonObject.getString("active");
                        String critical=jsonObject.getString("critical");

                        JSONObject ob=jsonObject.getJSONObject("countryInfo");
                        String flagURL=ob.getString("flag");

                        countryDataModel=new CountryDataModel(flagURL,countryName,cases,todayCases,deaths,todayDeaths,recovered,active,critical);
                        countryDataModelList.add(countryDataModel);

                    }

                    countryDataAdapter=new CountryDataAdapter(SearchActivity.this,countryDataModelList);
                    listView.setAdapter(countryDataAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchActivity.this,"Error : "+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void initializeVariable() {
        searchBar=findViewById(R.id.searchBar);
        listView=findViewById(R.id.resultList);
    }
}