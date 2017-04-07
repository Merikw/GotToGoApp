package nl.gottogo.gottogoapplication;

/**
 * Created by Merik on 23/03/2017.
 */

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static nl.gottogo.gottogoapplication.R.id.place_autocomplete_fragment;

public class Tab2 extends Fragment {

    private City selected;
    private ArrayList<City> cities;
    private DatabaseReference mUserDatabase;

    private GoogleApiClient mGoogleApiClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab2, container, false);
        Button btnAdd = (Button) rootView.findViewById(R.id.btnAdd);


        PlaceAutocompleteFragment autocompleteFragment  = (PlaceAutocompleteFragment)getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                City c = new City();
                c.setId(place.getId());
                c.setPlace_id(place.getId());
                c.setName(place.getName().toString());
                selected = c;
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

        mUserDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    JSONclass json = new JSONclass();
                    String jsonText = null;
                    jsonText = json.execute("https://maps.googleapis.com/maps/api/place/details/json?placeid="+dataSnapshot.getKey()+"&key=AIzaSyD5zGTckWc2J6MlhjNtnit2uyUZWJibRqA").get();
                    JSONObject jsonObject = new JSONObject(jsonText);
                    JSONObject city = new JSONObject(jsonObject.getString("result"));
                    City c = new City();
                    c.setId(city.getString("id"));
                    c.setName(city.getString("formatted_address"));
                    c.setPlace_id(dataSnapshot.getKey());
                    citiesListed.add(c);

                    //Find the listview
                    ListView lv = (ListView)rootView.findViewById(R.id.lvRec);

                    CityAdapter ca = new CityAdapter(getContext(), citiesListed);

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Logic.getInstance().setCity(citiesListed.get(i));
                            Intent detailIntent = new Intent(getContext(), DetailCityView.class);
                            startActivity(detailIntent);
                        }
                    });

                    lv.setAdapter(ca);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
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