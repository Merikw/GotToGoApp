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
        data.add(new City("Berghem", 10,"Berghem (Brabants: Bèrge) (vroeger ook als Berchem gespeld) is een Noord-Brabants dorp aan de rand van Oss, met circa 10.000 inwoners. Tot 1 januari 1994 vormde het een zelfstandige gemeente, sindsdien behoort het tot de gemeente Oss. Op het gebied van Berghem worden uitbreidingsplannen gerealiseerd, zodat in 2011 reeds ongeveer 11% van de inwoners van de Gemeente Oss uit Berghemnaren bestond.","plaatjeid"));
        data.add(new City("Oss", 90000," coole gozers","plaatjeid"));
        data.add(new City("Oss", 90000," coole gozers","plaatjeid"));
        data.add(new City("Oss", 90000," coole gozers","plaatjeid"));
        data.add(new City("Oss", 90000," coole gozers","plaatjeid"));
        data.add(new City("Oss", 90000," coole gozers","plaatjeid"));
        //Defining an adapter
        CityAdapter ca = new CityAdapter(this.getContext(),data);

        lv.setAdapter(ca);
        return rootView;
    }
}