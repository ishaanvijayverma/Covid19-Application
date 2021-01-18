package com.example.covid_19infoandnews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

public class CountryDataAdapter extends ArrayAdapter<CountryDataModel> {

    private Context context;
    private List<CountryDataModel> countryDataModelList;

    public CountryDataAdapter(Context context, List<CountryDataModel> countryDataModelList) {
        super(context, R.layout.custom_seacrh_item,countryDataModelList);

        this.context=context;
        this.countryDataModelList=countryDataModelList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_seacrh_item,null,true);

        TextView resultCountry=view.findViewById(R.id.resultCountryName);
        ImageView resultFlag=view.findViewById(R.id.resultFlag);

        resultCountry.setText(countryDataModelList.get(position).getCountry());
        Glide.with(context).load(countryDataModelList.get(position).getFlag()).into(resultFlag);

        return super.getView(position, convertView, parent);
    }
}
