package nl.gottogo.gottogoapplication;

import android.media.Rating;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class DetailCityView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        City city = Logic.getInstance().getCity();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_city_view);
        TextView tvCityName = (TextView) findViewById(R.id.tvCityName);
        ImageView cityImage = (ImageView) findViewById(R.id.ivCityPic);
        RatingBar foodRating = (RatingBar) findViewById(R.id.foodRating);
        RatingBar sightRating = (RatingBar) findViewById(R.id.sightRating);
        RatingBar enviromentRating = (RatingBar) findViewById(R.id.enviromentRating);
        RatingBar transportRating = (RatingBar) findViewById(R.id.transportRating);

        foodRating.setRating(city.getCityRating().getFoodRating());
        sightRating.setRating(city.getCityRating().getSightsRating());
        enviromentRating.setRating(city.getCityRating().getEnviromentRating());
        transportRating.setRating(city.getCityRating().getTransportRating());



        tvCityName.setText(city.getName());
    }
}
