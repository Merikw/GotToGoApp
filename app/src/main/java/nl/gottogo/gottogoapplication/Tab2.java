package nl.gottogo.gottogoapplication;

/**
 * Created by Merik on 23/03/2017.
 */

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class Tab2 extends Fragment implements GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleApiClient.ConnectionCallbacks {

    private City selected;
    private ArrayList<City> cities;
    private DatabaseReference mUserDatabase;
    private TextView tvTutorialTab2;

    private LocationManager locationManager;
    private android.location.LocationListener locationListener;

    private GoogleApiClient mGoogleApiClient;

    private double lat = 0;
    private double lon = 0;

    private Location mLastLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.tab2, container, false);
        Button btnAdd = (Button) rootView.findViewById(R.id.btnAdd);
        final Button btnGPS = (Button) rootView.findViewById(R.id.btnGPS);
        tvTutorialTab2 = (TextView) rootView.findViewById(R.id.tvTutorialTab2);

        mGoogleApiClient = new GoogleApiClient
                .Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .addApi(ActivityRecognition.API)
                .enableAutoManage(this.getActivity(), this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();

        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
            }
        }

        locationManager = (LocationManager) getContext().getSystemService(getContext().LOCATION_SERVICE);
        locationListener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();
                System.out.println("lat: " + location.getLatitude() + " Lon: " + location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        };

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                selected = new City(place.getId(), place.getId(), place.getName().toString());
                System.out.println("City: " + selected.getName());
            }

            @Override
            public void onError(Status status) {
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected != null) {
                    Logic.getInstance().addCity(selected);
                    Intent i = getContext().getPackageManager().getLaunchIntentForPackage(getContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getContext().startActivity(i);
                }
                else {
                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getContext());
                    dlgAlert.setMessage("You should select a city! If you did this with gps, click on the text box and " +
                            "please select the corresponding city.");
                    dlgAlert.setTitle("GotToGo Error!");
                    dlgAlert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                }
            }
        });

        btnGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                if((lon == 0 || lat == 0) && loc != null){
                    lon = loc.getLongitude();
                    lat = loc.getLatitude();
                }

                Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
                List<android.location.Address> addresses = null;
                try {
                    addresses = gcd.getFromLocation(lat, lon, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses.size() > 0)
                {
                    System.out.println("City: " + addresses.get(0).toString());
                    autocompleteFragment.setText(addresses.get(0).getLocality());
                }
                else
                {

                }
            }
        });

        mUserDatabase = FirebaseDatabase.getInstance().getReference("users").child(Logic.getInstance().getUserMail());
        final ArrayList<City> citiesListed = new ArrayList<City>();

        final CityAdapter ca = new CityAdapter(getContext(), citiesListed);

        //Find the listview
        ListView lv = (ListView) rootView.findViewById(R.id.lvRec);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Logic.getInstance().setCity(citiesListed.get(i));
                ca.setFocus(i);
                ca.notifyDataSetChanged();
            }
        });

        lv.setAdapter(ca);

        mUserDatabase.addChildEventListener(new

            ChildEventListener() {
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
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    citiesListed.clear();
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

                                    if(citiesListed.size() > 0){
                                        tvTutorialTab2.setVisibility(View.INVISIBLE);
                                    } else{
                                        tvTutorialTab2.setVisibility(View.VISIBLE);
                                    }

                                    places.release();
                                }
                            });
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Places.GeoDataApi.getPlaceById(mGoogleApiClient, dataSnapshot.getKey())
                            .setResultCallback(new ResultCallback<PlaceBuffer>() {
                                @Override
                                public void onResult(PlaceBuffer places) {
                                    if (places.getStatus().isSuccess() && places.getCount() > 0) {
                                        final Place place = places.get(0);
                                        System.out.println(place.getName());
                                        City c = new City(place.getId(), place.getId(), place.getName().toString());

                                        Iterator<City> iter = citiesListed.iterator();

                                        while (iter.hasNext()) {
                                            City city = iter.next();

                                            if (city.getId().equals(place.getId()))
                                                iter.remove();
                                        }
                                        ca.notifyDataSetChanged();
                                    } else {
                                    }

                                    if(citiesListed.size() > 0){
                                        tvTutorialTab2.setVisibility(View.INVISIBLE);
                                    } else{
                                        tvTutorialTab2.setVisibility(View.VISIBLE);
                                    }

                                    places.release();
                                }
                            });
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
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment fragment = (fm.findFragmentById(R.id.place_autocomplete_fragment));
        FragmentTransaction ft = fm.beginTransaction();
        if(fragment != null) {
            ft.remove(fragment);
            ft.commit();
        }
    }
}