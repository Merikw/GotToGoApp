package nl.gottogo.gottogoapplication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.widget.ArrayAdapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

/**
 * Created by Merik on 24/03/2017.
 */

public class FirebaseRepo extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private String android_id;
    private AccountManager am;

    public FirebaseRepo(AccountManager am) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.am = am;

    }

    public FirebaseRepo() {
        mDatabase = FirebaseDatabase.getInstance().getReference();;
    }

    public void registerDevice() {

        Account[] accounts = am.getAccountsByType("com.google");

        System.out.println("HEEEE");

        //String email = accounts[0].name;
        //Logic.getInstance().setUserMail(email);

        Logic.getInstance().setUserMail("Merik");
    }

    public void addCity(City city) {
        mDatabase.child(Logic.getInstance().getUserMail()).child(city.getPlace_id()).setValue(city.getName());
        mDatabase.child(city.getPlace_id()).setValue(city.getName());
    }
}
