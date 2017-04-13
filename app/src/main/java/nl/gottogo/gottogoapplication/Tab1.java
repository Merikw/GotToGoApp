package nl.gottogo.gottogoapplication;

/**
 * Created by Merik on 23/03/2017.
 */

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Tab1 extends Fragment {

    private DatabaseReference mUserDatabase;
    private GoogleApiClient mGoogleApiClient;
    private HashMap<String, Integer> citiesCount;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1, container, false);

        citiesCount = new HashMap<>();

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
                Intent detailIntent = new Intent(getContext(), DetailCityView.class);
                startActivity(detailIntent);
            }
        });

        mUserDatabase = FirebaseDatabase.getInstance().getReference("users");

        mUserDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                citiesCount.clear();
                ArrayList<String > ownList = new ArrayList<String>();
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    if (!dataSnapshot.getKey().toLowerCase().equals(Logic.getInstance().getUserMail().toLowerCase())) {
                        Integer n = citiesCount.get(children.getKey());
                        if (n == null) {
                            n = 1;
                        } else {
                            n++;
                        }
                        citiesCount.put(children.getKey(), n);
                    } else{
                        System.out.println("Ik bne niet merik");
                    }
                }

                Comparer comparer = new Comparer();
                comparer.sortMapByValue(citiesCount);

                ArrayList<String> places_id = new ArrayList<String>();
                for(Map.Entry<String, Integer> entry : citiesCount.entrySet()){
                    places_id.add(entry.getKey());
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