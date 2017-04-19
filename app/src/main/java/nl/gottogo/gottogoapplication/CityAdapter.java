package nl.gottogo.gottogoapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.*;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by dande on 30-3-2017.
 */

/**
 * City Adapter class for custom listview.
 */
public class CityAdapter extends ArrayAdapter<City> {

    //Fields for the city adapter class.

    private int Position;
    private Bitmap image = null;
    private DatabaseReference mUserDatabase;

    //Views for the city adapter class.

    private LinearLayout llBackground;
    private TextView tvCityName;
    private LinearLayout llButtons;
    private Button btnRemove;
    private Button btnReview;
    private Button btnDetail;

    /**
     * Constructor of the City Adapter.
     * @param context the context.
     * @param cities the cities that need to get places in the listview.
     */
    public CityAdapter(Context context,ArrayList<City> cities) {
        super(context,0,cities);
        Position = -1;
    }

    /**
     * Generating the view of the card.
     * @param position the position of the card.
     * @param convertView the view when clicked on.
     * @param parent the parent.
     * @return the view of the card.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final City city = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_city,parent,false);
        }

        setViews(convertView);
        firebasePictures(city, this.llBackground);

        this.tvCityName.setText(city.getName());
        this.llButtons.setVisibility(convertView.GONE);

        if(llBackground.getBackground() == null){
            llBackground.setBackgroundResource(R.drawable.amsterdam);
        }

        if(this.Position == position){
            this.llButtons.setVisibility(convertView.VISIBLE);
        }

        removeButtonWithDialog(city);

        this.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(new Intent(getContext(),DetailCityView.class));
            }
        });

        return convertView;
    }

    /**
     * Method to get the pictures from the firebase.
     */
    public void firebasePictures(final City finalCity, final LinearLayout llBg){
        this.mUserDatabase = FirebaseDatabase.getInstance().getReference("cities").child(finalCity.getPlace_id());
        this.mUserDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addImagesCity(dataSnapshot, finalCity, llBg);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                addImagesCity(dataSnapshot, finalCity, llBg);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                addImagesCity(dataSnapshot, finalCity, llBg);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Add an image to the city for the card.
     * @param dataSnapshot the datasnapshot of the firebase.
     */
    public void addImagesCity(DataSnapshot dataSnapshot, final City city, final LinearLayout llBackground){
        for(DataSnapshot children : dataSnapshot.getChildren()) {
            String url = city.getPlace_id() + "/" + children.getKey();
            System.out.println("image: " + url);
            StorageReference islandRef = FirebaseStorage.getInstance().getReference().child(url);

            final long ONE_MEGABYTE = 1024 * 1024;
            islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    float pxW = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 338, getContext().getResources().getDisplayMetrics());
                    float pxH = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 175, getContext().getResources().getDisplayMetrics());
                    Bitmap bm = Bitmap.createScaledBitmap(image, (int) pxW, (int) pxH, true);
                    city.setImage(bm);
                    llBackground.setBackground(new BitmapDrawable(getContext().getResources(), bm));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
            break;
        }
    }

    /**
     * Setting all the views.
     * @param convertView the layout view.
     */
    public void setViews(View convertView){
        llBackground = (LinearLayout) convertView.findViewById(R.id.llBackground);
        tvCityName = (TextView) convertView.findViewById(R.id.tvCityName);
        llButtons = (LinearLayout) convertView.findViewById(R.id.llButtons);
        btnRemove = (Button) convertView.findViewById(R.id.btnRemove);
        btnReview = (Button) convertView.findViewById(R.id.btnReview);
        btnDetail = (Button) convertView.findViewById(R.id.btnDetail);
    }

    /**
     * Setting the focus when clicked on.
     * @param position the position of the card.
     */
    public void setFocus(int position){
        this.Position = position;
    }

    /**
     * Method which creates the remove button with the dialog.
     */
    public void removeButtonWithDialog(final City city){
        this.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                mUserDatabase = FirebaseDatabase.getInstance().getReference("users").child(Logic.getInstance().getUserMail()).child(city.getPlace_id());
                                try {
                                    mUserDatabase.removeValue();
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                                Logic.getInstance().writeToFile(city.getId());
                                Intent i = getContext().getPackageManager().getLaunchIntentForPackage(getContext().getPackageName());
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                getContext().startActivity(i);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to remove this city?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
    }

}