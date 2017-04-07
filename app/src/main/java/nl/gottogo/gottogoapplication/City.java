package nl.gottogo.gottogoapplication;

import android.graphics.Bitmap;
import android.media.Rating;

/**
 * Created by dande on 30-3-2017.
 */

public class City {
    private String name;
    private String id;
    private String place_id;
    private Bitmap image;
    private String bannerRef;
    private CityRating cityRating;


    public City(String name,CityRating cityRating) {
        this.name = name;
        this.cityRating = cityRating;
    }

    public City(String id, String place_id, String name){
        this.id = id;
        this.place_id = place_id;
        this.name = name;
    }

    public City(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBannerRef() {
        return bannerRef;
    }

    public void setBannerRef(String bannerRef) {
        this.bannerRef = bannerRef;
    }

    public void setImage(Bitmap image){
        this.image = image;
    }

    public Bitmap getImage(){
          return this.image;
    }

    public String getId() {return id;}

    public CityRating getCityRating() {
        return cityRating;
    }

    public void setCityRating(CityRating cityRating) {
        this.cityRating = cityRating;
    }

    public void setId(String id) {this.id = id;}

    public String getPlace_id() {return place_id;}

    public void setPlace_id(String place_id) {this.place_id = place_id;}

    @Override
    public String toString() {
        return this.name;
    }
}
