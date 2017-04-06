package nl.gottogo.gottogoapplication;

/**
 * Created by dande on 6-4-2017.
 */

public class CityRating {
    private int foodRating;
    private int enviromentRating;
    private int sightsRating;
    private int transportRating;

    public CityRating(int foodRating, int enviromentRating, int sightsRating, int transportRating) {
        this.foodRating = foodRating;
        this.enviromentRating = enviromentRating;
        this.sightsRating = sightsRating;
        this.transportRating = transportRating;
    }

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
