package nl.gottogo.gottogoapplication;

import android.graphics.Bitmap;

/**
 * Created by dande on 30-3-2017.
 */

public class City {
    private String name;
    private int population;
    private String description;
    private Bitmap image;
    private String bannerRef;

    public City(String name, int population, String description, String bannerRef) {
        this.name = name;
        this.population = population;
        this.description = description;
        this.bannerRef = bannerRef;
    }

    public City(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public String toString() {
        return this.description;
    }
}
