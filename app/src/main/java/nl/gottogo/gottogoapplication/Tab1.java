package nl.gottogo.gottogoapplication;

/**
 * Created by Merik on 23/03/2017.
 */

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Tab1 extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.tab1, container, false);
       //Find the listview
        ListView lv = (ListView)rootView.findViewById(R.id.LvRec);
        //test data
        ArrayList<City> data = new ArrayList<City>();
        data.add(new City("Berghem", 10,"Allemaal boeren","plaatjeid"));
        data.add(new City("Oss", 90000," coole gozers","plaatjeid"));
        //Defining an adapter
        CityAdapter ca = new CityAdapter(this.getContext(),data);

        lv.setAdapter(ca);
        return rootView;
    }
}