package nl.gottogo.gottogoapplication;

/**
 * Created by Merik on 23/03/2017.
 */

import android.content.Intent;
import android.media.Rating;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Tab1 extends Fragment{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.tab1, container, false);
       //Find the listview
        ListView lv = (ListView)rootView.findViewById(R.id.LvRec);
        //test data
        final ArrayList<City> data = new ArrayList<City>();
        data.add(new City("Berghem",new CityRating(1,1,1,1)));
        data.add(new City("hi1",new CityRating(1,1,1,1)));
        data.add(new City("h2i",new CityRating(1,1,1,1)));
        data.add(new City("Berghem3",new CityRating(1,1,1,1)));
        data.add(new City("Ber4ghem",new CityRating(1,1,1,1)));
        //Defining an adapter
        CityAdapter ca = new CityAdapter(this.getContext(),data);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Logic.getInstance().setCity(data.get(i));
                Intent detailIntent = new Intent(getContext(),DetailCityView.class);
                startActivity(detailIntent);
            }
        });

        lv.setAdapter(ca);
        return rootView;
    }

}