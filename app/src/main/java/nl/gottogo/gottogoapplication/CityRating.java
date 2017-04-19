package nl.gottogo.gottogoapplication;

/**
 * Created by dande on 6-4-2017.
 */

/**
 * Class for getting the rating of the city.
 */
public class CityRating {

    // Fields of the city rating.

    private int foodRating;
    private int enviromentRating;
    private int sightsRating;
    private int transportRating;

    /**
     * Constructor for the rating of the city.
     * @param foodRating the rating for the food.
     * @param enviromentRating the rating for the environment.
     * @param sightsRating the rating fo the sights.
     * @param transportRating the rating for the transportation.
     */
    public CityRating(int foodRating, int enviromentRating, int sightsRating, int transportRating) {
        this.foodRating = foodRating;
        this.enviromentRating = enviromentRating;
        this.sightsRating = sightsRating;
        this.transportRating = transportRating;
    }

    // getters and setters

    public int getFoodRating() {
        return foodRating;
    }

    public void setFoodRating(int foodRating) {
        this.foodRating = foodRating;
    }

    public int getEnviromentRating() {
        return enviromentRating;
    }

    public void setEnviromentRating(int enviromentRating) {
        this.enviromentRating = enviromentRating;
    }

    public int getSightsRating() {
        return sightsRating;
    }

    public void setSightsRating(int sightsRating) {
        this.sightsRating = sightsRating;
    }

    public int getTransportRating() {
        return transportRating;
    }

    public void setTransportRating(int transportRating) {
        this.transportRating = transportRating;
    }
}
