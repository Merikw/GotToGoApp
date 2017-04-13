package nl.gottogo.gottogoapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dande on 30-3-2017.
 */

public class CityAdapter extends ArrayAdapter<City> {
    private int Position;

    public CityAdapter(Context context,ArrayList<City> cities) {
        super(context,0,cities);
        Position = -1;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final City city = getItem(position);



        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_city,parent,false);
        }

        LinearLayout llBackground = (LinearLayout) convertView.findViewById(R.id.llBackground);
        TextView tvCityName = (TextView) convertView.findViewById(R.id.tvCityName);
        LinearLayout llButtons = (LinearLayout) convertView.findViewById(R.id.llButtons);
        Button remove = (Button) convertView.findViewById(R.id.btnRemove);
        Button review = (Button) convertView.findViewById(R.id.btnReview);
        Button detail = (Button) convertView.findViewById(R.id.btnDetail);


        llButtons.setVisibility(convertView.GONE);
        llBackground.setBackgroundResource(R.drawable.amsterdam);
        tvCityName.setText(city.getName());

        if(this.Position == position){
            llButtons.setVisibility(convertView.VISIBLE);
        }

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo maak verwijder fucntie
            }
        });
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getContext().startActivity(new Intent(getContext(),DetailCityView.class));
            }
        });


        return convertView;
    }
    public void setFocus(int position){
        this.Position = position;
    }

}
