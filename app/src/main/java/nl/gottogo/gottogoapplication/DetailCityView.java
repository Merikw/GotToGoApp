package nl.gottogo.gottogoapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailCityView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        City city = Logic.getInstance().getCity();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_city_view);
        TextView tvCityName = (TextView) findViewById(R.id.tvCityName);
        ImageView cityImage = (ImageView) findViewById(R.id.ivCityPic);
        TextView cityDesc = (TextView) findViewById(R.id.tvCityDesc);

        cityDesc.setText(city.getDescription());
        tvCityName.setText(city.getName());
    }
}
