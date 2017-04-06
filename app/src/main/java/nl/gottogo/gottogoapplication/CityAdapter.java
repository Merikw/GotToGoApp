package nl.gottogo.gottogoapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dande on 30-3-2017.
 */

public class CityAdapter extends ArrayAdapter<City> {

    public CityAdapter(Context context,ArrayList<City> cities) {
        super(context,0,cities);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        City city = getItem(position);



        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_city,parent,false);
        }

        LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.llcity);
        TextView tvCityName = (TextView) convertView.findViewById(R.id.tvCityName);
        TextView tvCityDescription = (TextView) convertView.findViewById(R.id.tvCityDescription);



        linearLayout.setBackgroundResource(R.drawable.amsterdam);
        tvCityName.setText(city.getName());
        tvCityDescription.setText(city.getDescription());

        return convertView;
    }

}
