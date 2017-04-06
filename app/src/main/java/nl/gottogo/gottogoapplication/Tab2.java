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

public class Tab2 extends Fragment {

    private City selected;
    private ArrayList<City> cities;
    private DatabaseReference mUserDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab2, container, false);
        Button btnAdd = (Button) rootView.findViewById(R.id.btnAdd);

        final AutoCompleteTextView textView = (AutoCompleteTextView) rootView.findViewById(R.id.txtCity);

        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                JSONclass json = new JSONclass();
                String jsonText = null;
                try {
                    jsonText = json.execute("https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+charSequence.toString()+"&types=(cities)&key=AIzaSyD5zGTckWc2J6MlhjNtnit2uyUZWJibRqA").get();

                    JSONObject jsonObject = new JSONObject(jsonText);
                    JSONArray jsonArray = jsonObject.getJSONArray("predictions");
                    cities = new ArrayList<City>();

                    for(int x = 0; x < jsonArray.length(); x++){
                        JSONObject city = jsonArray.getJSONObject(x);
                        City c = new City();
                        c.setPlace_id(city.getString("place_id"));
                        c.setId(city.getString("id"));
                        c.setName(city.getString("description"));
                        cities.add(c);
                    }

                    ArrayAdapter<City> adapter = new ArrayAdapter<City>(getContext(), android.R.layout.simple_list_item_1, cities);
                    textView.setAdapter(adapter);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = cities.get(0);
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