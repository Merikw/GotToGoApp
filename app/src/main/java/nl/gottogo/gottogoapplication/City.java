package nl.gottogo.gottogoapplication;

import android.graphics.Bitmap;

/**
 * Created by dande on 30-3-2017.
 */

/**
 * Class holding all the information about the city.
 */
public class City {

    //fields of the city.

    private String name;
    private String id;
    private String place_id;
    private Bitmap image;
    private CityRating cityRating;

    /**
     * Constructor for the search of the cities with rating.
     * @param name the name of the city.
     * @param cityRating the rating of the city.
     */
    public City(String name, CityRating cityRating) {
        this.name = name;
        this.cityRating = cityRating;
    }

    /**
     * Construction for the search of cities without rating.
     * @param id the ID of the city.
     * @param place_id the Place_Id of the city.
     * @param name the name of the city.
     */
    public City(String id, String place_id, String name){
        this.id = id;
        this.place_id = place_id;
        this.name = name;
        this.cityRating = new CityRating(0, 0, 0, 0);
    }

    /**
     * Empty constructor of the city.
     */
    public City(){
        this.cityRating = new CityRating(0, 0, 0, 0);
    }

    // getters and setters.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(Bitmap image){
        this.image = image;
    }

    public Bitmap getImage(){
        return this.image;
    }

    public String getId() {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public CityRating getCityRating() {
        return cityRating;
    }

    public void setCityRating(CityRating cityRating) {
        this.cityRating = cityRating;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
