package nl.gottogo.gottogoapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by dande on 30-3-2017.
 */

public class CityAdapter extends ArrayAdapter<City> {
    private int Position;
    private Bitmap image = null;
    private DatabaseReference mUserDatabase;

    public CityAdapter(Context context,ArrayList<City> cities) {
        super(context,0,cities);
        Position = -1;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final City city = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_city,parent,false);
        }

        final LinearLayout llBackground = (LinearLayout) convertView.findViewById(R.id.llBackground);
        TextView tvCityName = (TextView) convertView.findViewById(R.id.tvCityName);
        LinearLayout llButtons = (LinearLayout) convertView.findViewById(R.id.llButtons);
        Button remove = (Button) convertView.findViewById(R.id.btnRemove);
        Button review = (Button) convertView.findViewById(R.id.btnReview);
        Button detail = (Button) convertView.findViewById(R.id.btnDetail);

        llButtons.setVisibility(convertView.GONE);

        mUserDatabase = FirebaseDatabase.getInstance().getReference("cities").child(city.getPlace_id());
        mUserDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot children : dataSnapshot.getChildren()) {
                    System.out.println("image: " + city.getPlace_id() + "/" + children.getKey());
                    StorageReference islandRef = FirebaseStorage.getInstance().getReference().child(city.getPlace_id() + "/" + children.getKey());

                    final long ONE_MEGABYTE = 1024 * 1024;
                    islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            Bitmap bm = Bitmap.createScaledBitmap(image, 1000, 450, true);
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

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot children : dataSnapshot.getChildren()) {
                    System.out.println("image: " + city.getPlace_id() + "/" + children.getKey());
                    StorageReference islandRef = FirebaseStorage.getInstance().getReference().child(city.getPlace_id() + "/" + children.getKey());

                    final long ONE_MEGABYTE = 1024 * 1024;
                    islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            Bitmap bm = Bitmap.createScaledBitmap(image, 1000, 450, true);
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

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                for(DataSnapshot children : dataSnapshot.getChildren()) {
                    System.out.println("image: " + city.getPlace_id() + "/" + children.getKey());
                    StorageReference islandRef = FirebaseStorage.getInstance().getReference().child(city.getPlace_id() + "/" + children.getKey());

                    final long ONE_MEGABYTE = 1024 * 1024;
                    islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            Bitmap bm = Bitmap.createScaledBitmap(image, 1000, 450, true);
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

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(llBackground.getBackground() == null){
            llBackground.setBackgroundResource(R.drawable.amsterdam);
        }

        tvCityName.setText(city.getName());

        if(this.Position == position){
            llButtons.setVisibility(convertView.VISIBLE);
        }

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserDatabase = FirebaseDatabase.getInstance().getReference("users").child(Logic.getInstance().getUserMail()).child(city.getPlace_id());
                try {
                    mUserDatabase.removeValue();
                } catch (Exception e){
                    FileOutputStream outputStream = null;
                    try {
                        outputStream = getContext().openFileOutput("cities", Context.MODE_APPEND);
                        outputStream.write((city.getPlace_id() + "\n").getBytes());
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getContext().startActivity(new Intent(getContext(),DetailCityView.class));
            }
        });


        return convertView;
    }
    public void setFocus(int position){
        this.Position = position;
    }

}