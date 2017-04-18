package nl.gottogo.gottogoapplication;

import android.content.Intent;
import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Merik on 05/04/2017.
 */

public class Logic {

    private String email;
    private City city;
    private static Logic instance = null;
    private int positionTab = 0;

    private FirebaseRepo repo = new FirebaseRepo();

    protected Logic(){

    }

    public static Logic getInstance(){
        if(instance == null){
            instance = new Logic();
        }
        return instance;
    }

    public String getUserMail(){
        return this.email;
    }

    public void setUserMail(String mail){
        this.email = mail;
    }

    public City getCity() {return city;}

    public void setCity(City city) {this.city = city;}

    public void addCity(City city){
        repo.addCity(city);
    }

    public void addImage(Bitmap image, City city){
        repo.addImage(image, city);
    }

    public int getPositionTab(){
        return this.positionTab;
    }

    public void setPositionTab(int position){
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
}
