package nl.gottogo.gottogoapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.R.attr.id;

/**
 * Created by Merik on 05/04/2017.
 */

/**
 * Singleton class for the logic.
 */
public class Logic {

    //fields

    private String email;
    private City city;
    private static Logic instance = null;
    private int positionTab = 0;
    private Context context;
    private FirebaseRepo repo = new FirebaseRepo();

    private ListView lv;
    private ArrayList<City> data;
    private CityAdapter ca;

    protected Logic() {

    }

    /**
     * Getting the instance.
     * @return the logic instance.
     */
    public static Logic getInstance() {
        if (instance == null) {
            instance = new Logic();
        }
        return instance;
    }

    /**
     * Getting the user email.
     * @return email of the user.
     */
    public String getUserMail() {
        return this.email;
    }

    /**
     * Setting the users email.
     * @param mail the users email.
     */
    public void setUserMail(String mail) {
        this.email = mail;
    }

    /**
     * Setting the context of the app.
     * @param context the context of the app.
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Getting the city.
     * @return the city.
     */
    public City getCity() {
        return city;
    }

    /**
     * Setting the city.
     * @param city the city.
     */
    public void setCity(City city) {
        this.city = city;
    }

    /**
     * Setting all the listviews items so it can be updated.
     * @param lv the listview of the cities.
     * @param data the list of cities.
     * @param ca the cityadapter of the listview.
     */
    public void setListviewItems(ListView lv, ArrayList<City> data, CityAdapter ca){
        this.lv = lv;
        this.data = data;
        this.ca = ca;
    }

    /**
     * Adding the city to the firebase.
     * @param city the city that needs to be added.
     */
    public void addCity(City city) {
        repo.addCity(city);
    }

    /**
     * Adding image to the city.
     * @param image the image that needs to get added.
     * @param city the city where the image needs to get added.
     */
    public void addImage(Bitmap image, City city) {
        repo.addImage(image, city);
    }

    /**
     * Writing to the file for the removed cities.
     * @param str the id of the city.
     */
    public void writeToFile(String str) {
        try {
            DataOutputStream out = new DataOutputStream(this.context.openFileOutput("cities.txt", Context.MODE_APPEND));
            out.writeUTF(str);
            out.close();
            Iterator<City> iter = data.iterator();

            while (iter.hasNext()) {
                City c = iter.next();

                if (c.getId().equals(str))
                    iter.remove();
            }
            ca.notifyDataSetChanged();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Reading the file of removed cities.
     * @return the list of removed cities.
     */
    public ArrayList<String> readFile() {
        ArrayList<String> removedCities = new ArrayList<>();
        try {
            DataInputStream in = new DataInputStream(this.context.openFileInput("cities.txt"));
            String str = "";
            while ((str = in.readUTF()) != "") {
                removedCities.add(str);
                System.out.println("text: " + str);
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return removedCities;
    }

    /**
     * Emptying the file of removed cities.
     */
    public void removeFileRemovedCities() {
        try {
            DataOutputStream out = new DataOutputStream(this.context.openFileOutput("cities.txt", Context.MODE_PRIVATE));
            out.writeUTF(" ");
            out.close();
            Intent i = this.context.getPackageManager().getLaunchIntentForPackage(this.context.getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.context.startActivity(i);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}
