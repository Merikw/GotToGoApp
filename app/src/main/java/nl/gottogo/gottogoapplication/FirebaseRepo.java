package nl.gottogo.gottogoapplication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Merik on 24/03/2017.
 */

public class FirebaseRepo extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private String android_id;
    private AccountManager am;

    public FirebaseRepo(AccountManager am){

        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.am = am;

    }

    public void registerDevice() {

        Account[] accounts = am.getAccountsByType("com.google");

        System.out.println("HEEEE");

        //String email = accounts[0].name;
        //Logic.getInstance().setUserMail(email);

        Logic.getInstance().setUserMail("Merik");

        mDatabase.child("Merik").child("Budapest").setValue("8");
        mDatabase.child("Merik").child("Budapest").setValue("9");
    }
}
