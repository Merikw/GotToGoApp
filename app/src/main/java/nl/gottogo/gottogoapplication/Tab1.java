package nl.gottogo.gottogoapplication;

/**
 * Created by Merik on 23/03/2017.
 */

import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Tab1 extends Fragment {

    //fields

    private DatabaseReference mUserDatabase;
    private GoogleApiClient mGoogleApiClient;
    private HashMap<String, ArrayList<String>> citiesUsers;
    private ArrayList<String> removedCities;
    private ArrayList<City> data;
    private CityAdapter ca;
    private ListView lv;
    private TextView tvTutorialTab1;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab1, container, false);

        Logic.getInstance().setContext(getContext());

        citiesUsers = new HashMap<>();
        removedCities = Logic.getInstance().readFile();

        initializingGoogleApiClient();
        addingRemoveButton(rootView);

        lv = (ListView) rootView.findViewById(R.id.LvRec);
        data = new ArrayList<>();
        ca = new CityAdapter(this.getContext(), data);
        lv.setAdapter(ca);
        this.tvTutorialTab1 = (TextView) rootView.findViewById(R.id.tvTutorialTab1);

        Logic.getInstance().setListviewItems(lv, data, ca);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Logic.getInstance().setCity(data.get(i));
                ca.setFocus(i);
                ca.notifyDataSetChanged();

            }
        });

        mUserDatabase = FirebaseDatabase.getInstance().getReference("users");

        final ArrayList<String> ownList = new ArrayList<>();

        mUserDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                findingCities(dataSnapshot, ownList);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                findingCities(dataSnapshot, ownList);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                findingCities(dataSnapshot, ownList);
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

    /**
     * Method which initializes the google api client.
     */
    public void initializingGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient
                .Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .addApi(ActivityRecognition.API)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    /**
     * Method to remove the city.
     * @param dataSnapshot the data snapshot.
     */
    public void removeCity(DataSnapshot dataSnapshot) {
        for (final DataSnapshot children : dataSnapshot.getChildren()) {
            Iterator<City> iter = data.iterator();

            while (iter.hasNext()) {
                City c = iter.next();

                if (c.getId().equals(children.getKey()))
                    iter.remove();
            }
            ca.notifyDataSetChanged();
        }
    }

    /**
     * Finding of cities and implementing the listview.
     * @param dataSnapshot the datasnapshot used from firebase.
     * @param ownList the list of the logged in user.
     */
    public void findingCities(DataSnapshot dataSnapshot, final ArrayList<String> ownList) {
        removedCities = Logic.getInstance().readFile();
        ArrayList<String> cities;
        for (final DataSnapshot children : dataSnapshot.getChildren()) {
            System.out.println(dataSnapshot.getKey() + " : " + children.getKey());
            cities = citiesUsers.get(dataSnapshot.getKey());
            if (cities == null) {
                cities = new ArrayList<>();
            }
            cities.add(children.getKey());
            citiesUsers.put(dataSnapshot.getKey(), cities);
        }

        ArrayList<String> places_id = new ArrayList<>();
        final ArrayList<String> ownPlaces = new ArrayList<>();
        for (Map.Entry<String, ArrayList<String>> entry : citiesUsers.entrySet()) {
            if (entry.getKey().equals(Logic.getInstance().getUserMail())) {
                ownPlaces.addAll(entry.getValue());
                System.out.println("Own: " + entry.getValue());
            }
        }

        for (Map.Entry<String, ArrayList<String>> entry : citiesUsers.entrySet()) {
            boolean match = false;
            String matchedPlace = "";
            if (!entry.getKey().equals(Logic.getInstance().getUserMail())) {
                for (String place : ownPlaces) {
                    for (String otherPlace : entry.getValue()) {
                        if (place.equals(otherPlace)) {
                            match = true;
                            matchedPlace = place;
                        }
                    }
                }
            }

            if (match) {
                for (String place : entry.getValue()) {
                    if (!place.equals(matchedPlace)) {
                        places_id.add(place);
                    }
                }
            }
        }

        for (final String id : places_id) {
            Places.GeoDataApi.getPlaceById(mGoogleApiClient, id)
                    .setResultCallback(new ResultCallback<PlaceBuffer>() {
                        @Override
                        public void onResult(PlaceBuffer places) {

                            boolean removed = false;
                            Iterator<String> iterRemoved = removedCities.iterator();

                            while (iterRemoved.hasNext()) {
                                String cityID = iterRemoved.next();

                                if (cityID.equals(id))
                                    removed = true;
                            }

                            Iterator<String> iter = ownList.iterator();

                            while (iter.hasNext()) {
                                String idNext = iter.next();

                                if (idNext.equals(id))
                                    iter.remove();
                            }

                            if (places.getStatus().isSuccess() && places.getCount() > 0 && !removed) {
                                final Place place = places.get(0);
                                System.out.println(place.getName());
                                City c = new City(place.getId(), place.getId(), place.getName().toString());
                                boolean exists = false;
                                for (City city : data) {
                                    if (city.getPlace_id().equals(c.getPlace_id())) {
                                        exists = true;
                                    }
                                }

                                for(City cityData : data){
                                    for(String str : ownPlaces){
                                        if(str.equals(cityData.getId())){
                                            data.remove(cityData);
                                            exists = true;
                                        }
                                    }
                                }

                                if (!exists) {
                                    data.add(c);
                                }
                                ca.notifyDataSetChanged();
                            }

                            if(data.size() > 0){
                                tvTutorialTab1.setVisibility(View.INVISIBLE);
                            } else{
                                tvTutorialTab1.setVisibility(View.VISIBLE);
                            }

                            ca.notifyDataSetChanged();

                            ca.notifyDataSetChanged();
                            places.release();
                        }
                    });
        }
    }

    /**
     * Method to add the remove button.
     * @param rootView the view.
     */
    public void addingRemoveButton(View rootView) {
        rootView.findViewById(R.id.btnUndoRemove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logic.getInstance().removeFileRemovedCities();
            }
        });
    }
}