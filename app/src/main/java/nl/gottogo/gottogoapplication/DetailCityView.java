package nl.gottogo.gottogoapplication;

import android.media.Rating;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Detail view for the selected city.
 */
public class DetailCityView extends AppCompatActivity {

    private TextView tvCityName;
    private ImageView cityImage;
    private RatingBar foodRating;
    private RatingBar sightRating;
    private RatingBar environmentRating;
    private RatingBar transportRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Fields

        City city = Logic.getInstance().getCity();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_city_view);

        findViews();

        this.foodRating.setRating(city.getCityRating().getFoodRating());
        this.sightRating.setRating(city.getCityRating().getSightsRating());
        this.environmentRating.setRating(city.getCityRating().getEnviromentRating());
        this.transportRating.setRating(city.getCityRating().getTransportRating());

        this.tvCityName.setText(city.getName());
    }

    /**
     * Method to get all the views.
     */
    public void findViews(){
        this.tvCityName = (TextView) findViewById(R.id.tvCityName);
        this.cityImage = (ImageView) findViewById(R.id.ivCityPic);
        this.foodRating = (RatingBar) findViewById(R.id.foodRating);
        this.sightRating = (RatingBar) findViewById(R.id.sightRating);
        this.environmentRating = (RatingBar) findViewById(R.id.enviromentRating);
        this.transportRating = (RatingBar) findViewById(R.id.transportRating);
    }
}
