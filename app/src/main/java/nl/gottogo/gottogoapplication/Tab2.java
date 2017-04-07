package nl.gottogo.gottogoapplication;

/**
 * Created by Merik on 23/03/2017.
 */

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class Tab2 extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private City selected;
    private ArrayList<City> cities;
    private DatabaseReference mUserDatabase;

    private GoogleApiClient mGoogleApiClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab2, container, false);
        Button btnAdd = (Button) rootView.findViewById(R.id.btnAdd);

        mGoogleApiClient = new GoogleApiClient
                .Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this.getActivity(), this)
                .build();

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                selected = new City(place.getId(), place.getId(), place.getName().toString());
            }

            @Override
            public void onError(Status status) {
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logic.getInstance().addCity(selected);
            }
        });

        mUserDatabase = FirebaseDatabase.getInstance().getReference(Logic.getInstance().getUserMail());
        final ArrayList<City> citiesListed = new ArrayList<City>();

        final CityAdapter ca = new CityAdapter(getContext(), citiesListed);

        mUserDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Places.GeoDataApi.getPlaceById(mGoogleApiClient, dataSnapshot.getKey())
                        .setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(PlaceBuffer places) {
                                if (places.getStatus().isSuccess() && places.getCount() > 0) {
                                    final Place place = places.get(0);
                                    System.out.println(place.getName());
                                    City c = new City(place.getId(), place.getId(), place.getName().toString());
                                    citiesListed.add(c);
                                    ca.notifyDataSetChanged();
                                } else {
                                }
                                places.release();
                            }
                        });

                //Find the listview
                ListView lv = (ListView) rootView.findViewById(R.id.lvRec);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Logic.getInstance().setCity(citiesListed.get(i));
                        Intent detailIntent = new Intent(getContext(), DetailCityView.class);
                        startActivity(detailIntent);
                    }
                });

                lv.setAdapter(ca);
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}