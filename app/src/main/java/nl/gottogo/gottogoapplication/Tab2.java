package nl.gottogo.gottogoapplication;

/**
 * Created by Merik on 23/03/2017.
 */

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Tab2 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2, container, false);

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
                    jsonText = json.execute("https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + charSequence.toString() + "&types=(cities)&key=AIzaSyD5zGTckWc2J6MlhjNtnit2uyUZWJibRqA").get();

                    JSONObject jsonObject = new JSONObject(jsonText);
                    JSONArray jsonArray = jsonObject.getJSONArray("predictions");
                    ArrayList<String> cities = new ArrayList<String>();

                    for (int x = 0; x < jsonArray.length(); x++) {
                        JSONObject city = jsonArray.getJSONObject(x);
                        City c = new City();
                        cities.add(c.toString());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, cities);
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

        return rootView;
    }
}