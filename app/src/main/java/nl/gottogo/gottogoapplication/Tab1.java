package nl.gottogo.gottogoapplication;

/**
 * Created by Merik on 23/03/2017.
 */

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.Circle;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.Semaphore;

public class Tab1 extends Fragment {

    private DatabaseReference mUserDatabase;
    private GoogleApiClient mGoogleApiClient;
    private HashMap<String, ArrayList<String>> citiesUsers;

    private final Semaphore semaphore = new Semaphore(0);

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1, container, false);

        citiesUsers = new HashMap<>();

        mGoogleApiClient = new GoogleApiClient
                .Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .addApi(ActivityRecognition.API)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
        //Find the listview
        ListView lv = (ListView) rootView.findViewById(R.id.LvRec);

        //test data
        final ArrayList<City> data = new ArrayList<City>();
        final CityAdapter ca = new CityAdapter(this.getContext(), data);
        lv.setAdapter(ca);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Logic.getInstance().setCity(data.get(i));
                ca.setFocus(i);
                ca.notifyDataSetChanged();

            }
        });


        mUserDatabase = FirebaseDatabase.getInstance().getReference("users");

        mUserDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final ArrayList<String > ownList = new ArrayList<String>();
                for (final DataSnapshot children : dataSnapshot.getChildren()) {
                    System.out.println(dataSnapshot.getKey() + " : " + children.getKey());
                    ArrayList<String> cities = citiesUsers.get(dataSnapshot.getKey());
                    if(cities == null){
                        cities = new ArrayList<String>();
                    }
                    cities.add(children.getKey());
                    citiesUsers.put(dataSnapshot.getKey(), cities);
                }

                ArrayList<String> places_id = new ArrayList<String>();
                ArrayList<String> ownPlaces = new ArrayList<String>();
                for(Map.Entry<String, ArrayList<String>> entry : citiesUsers.entrySet()){
                    if(entry.getKey().equals(Logic.getInstance().getUserMail())){
                        ownPlaces.addAll(entry.getValue());
                    }
                }

                for(Map.Entry<String, ArrayList<String>> entry : citiesUsers.entrySet()){
                    boolean match = false;
                    String matchedPlace = "";
                    if(!entry.getKey().equals(Logic.getInstance().getUserMail())){
                        for(String place : ownPlaces){
                            for(String otherPlace : entry.getValue()){
                                if(place.equals(otherPlace)){
                                    match = true;
                                    matchedPlace = place;
                                }
                            }
                        }
                    }

                    if(match){
                        for(String place : entry.getValue()){
                            if(!place.equals(matchedPlace)){
                                places_id.add(place);
                            }
                        }
                    }
                }

                for(String id : places_id) {
                    Places.GeoDataApi.getPlaceById(mGoogleApiClient, id)
                            .setResultCallback(new ResultCallback<PlaceBuffer>() {
                                @Override
                                public void onResult(PlaceBuffer places) {
                                    if (places.getStatus().isSuccess() && places.getCount() > 0) {
                                        final Place place = places.get(0);
                                        System.out.println(place.getName());
                                        City c = new City(place.getId(), place.getId(), place.getName().toString());
                                        boolean exists = false;
                                        for (City city : data) {
                                            if (city.getPlace_id().equals(c.getPlace_id())) {
                                                exists = true;
                                            }
                                        }
                                        if (!exists) {
                                            data.add(c);
                                        }
                                        ca.notifyDataSetChanged();
                                    } else {
                                    }
                                    places.release();
                                }
                            });
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return rootView;
    }
}