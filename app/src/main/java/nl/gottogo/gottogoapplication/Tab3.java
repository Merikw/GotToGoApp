package nl.gottogo.gottogoapplication;

/**
 * Created by Merik on 23/03/2017.
 */

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import static nl.gottogo.gottogoapplication.R.id.imageView;

public class Tab3 extends Fragment implements GoogleApiClient.OnConnectionFailedListener{

    private City selected;
    private GoogleApiClient mGoogleApiClient;
    private ImageView preview;
    private Bitmap image;
    private Intent data;
    private View rootView;
    private PlaceAutocompleteFragment autocompleteFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        if(rootView == null) {
            rootView = inflater.inflate(R.layout.tab3, container, false);
        }

        if(mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient
                    .Builder(getContext())
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(this.getActivity(), 1, this)
                    .build();
        }

        if(autocompleteFragment == null) {
            autocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment2);
        }

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                selected = new City(place.getId(), place.getId(), place.getName().toString());
            }

            @Override
            public void onError(Status status) {
            }
        });


        Button btnCamera = (Button) rootView.findViewById(R.id.btnCamera);
        preview = (ImageView) rootView.findViewById(imageView);
        Button btnAddImage = (Button) rootView.findViewById(R.id.btnAdd);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 1);
                }
            }
        });

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected != null && selected.getPlace_id() != null && selected.getPlace_id().length() > 0 && image != null) {
                    Logic.getInstance().addImage(image, selected);
                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getContext());
                    dlgAlert.setMessage("You posted an image at the city with succes!");
                    dlgAlert.setTitle("GotToGo");
                    dlgAlert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = getContext().getPackageManager().getLaunchIntentForPackage(getContext().getPackageName());
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    getContext().startActivity(i);
                                }
                            });
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                } else{
                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getContext());
                    dlgAlert.setMessage("You should select a city and add a picutre first!");
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

        return rootView;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            image = (Bitmap) data.getExtras().get("data");
            preview.setImageBitmap(image);
            this.data = data;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment fragment = (fm.findFragmentById(R.id.place_autocomplete_fragment2));
        FragmentTransaction ft = fm.beginTransaction();
        if(fragment != null) {
            ft.remove(fragment);
            ft.commit();
        }
    }
}