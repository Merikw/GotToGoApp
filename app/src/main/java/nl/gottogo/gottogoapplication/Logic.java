package nl.gottogo.gottogoapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.util.Log;

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

/**
 * Created by Merik on 05/04/2017.
 */

public class Logic {

    private String email;
    private City city;
    private static Logic instance = null;
    private int positionTab = 0;
    private Context context;

    private FirebaseRepo repo = new FirebaseRepo();

    protected Logic() {

    }

    public static Logic getInstance() {
        if (instance == null) {
            instance = new Logic();
        }
        return instance;
    }

    public String getUserMail() {
        return this.email;
    }

    public void setUserMail(String mail) {
        this.email = mail;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public void addCity(City city) {
        repo.addCity(city);
    }

    public void addImage(Bitmap image, City city) {
        repo.addImage(image, city);
    }

    public int getPositionTab() {
        return this.positionTab;
    }

    public void setPositionTab(int position) {
        this.positionTab = position;
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    public void writeToFile(String str) {
        try {
            DataOutputStream out = new DataOutputStream(this.context.openFileOutput("cities.txt", Context.MODE_APPEND));
            out.writeUTF(str);
            out.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

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

    public void removeFileRemovedCities() {
        try {
            DataOutputStream out = new DataOutputStream(this.context.openFileOutput("cities.txt", Context.MODE_PRIVATE));
            out.writeUTF(" ");
            out.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}
