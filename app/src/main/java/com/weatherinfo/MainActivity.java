package com.weatherinfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.weatherinfo.api.APIClient;
import com.weatherinfo.api.APIInterface;
import com.weatherinfo.models.WeatherInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "103d81484acba06de623a689412ea453"; // API key, which we get when register on OpenWeatherMap platform
    private static final String METRIC = "metric"; // We need to use this as a parameter for the request in order to get temperature in Celsius
    private static final String TAG = "WEATHER_INFO";

    private TextView mTextViewCity;
    private TextView mTextViewTemperature;
    private TextView mTextViewPressure;
    private TextView mTextViewWindSpeed;
    private TextView mTextViewHumidity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] cityValues = getResources().getStringArray(R.array.city_values);

        mTextViewCity = findViewById(R.id.tv_city);
        mTextViewTemperature = findViewById(R.id.tv_temperature);
        mTextViewPressure = findViewById(R.id.tv_pressure);
        mTextViewWindSpeed = findViewById(R.id.tv_wind_speed);
        mTextViewHumidity = findViewById(R.id.tv_humidity);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.city_display_names));

        final Spinner spinnerCity = findViewById(R.id.sp_country);
        spinnerCity.setAdapter(adapter);
        spinnerCity.setSelection(0);
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedCity = cityValues[i];

                final APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
                apiInterface.getCurrentWeather(selectedCity, METRIC, API_KEY).enqueue(new Callback<WeatherInfo>() {
                    @Override
                    public void onResponse(Call<WeatherInfo> call, Response<WeatherInfo> response) {
                        final WeatherInfo weatherInfo = response.body();

                        mTextViewCity.setText(weatherInfo.getName());
                        mTextViewTemperature.setText(String.format("%s °C", weatherInfo.getMain().getTemp().intValue()));
                        mTextViewPressure.setText(String.format("Pressure: %s hPa", weatherInfo.getMain().getPressure().intValue()));
                        mTextViewWindSpeed.setText(String.format("Wind speed: %s km/h", weatherInfo.getWind().getSpeed().intValue()));
                        mTextViewHumidity.setText(String.format("Humidity: %s %%", weatherInfo.getMain().getHumidity().intValue()));
                    }

                    @Override
                    public void onFailure(Call<WeatherInfo> call, Throwable t) {
                        Log.e(TAG, "Exception: %s", t.getCause());
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

}

