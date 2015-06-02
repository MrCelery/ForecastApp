package andrii.goncharenko.forecastapp;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Andrey on 29.05.2015.
 */
public class ForecastDayItem implements IForecastItem {


    final String OWM_WEATHER = "weather";
    final String OWM_TEMPERATURE = "temp";
    final String OWM_MAX = "max";
    final String OWM_MIN = "min";
    final String OWM_DESCRIPTION = "description";
    final String OWM_DATE = "dt";
    final String OWM_WEATHER_ID = "id";

    Date date;
    String description;
    int maxTemp;
    int minTemp;
    int weatherID;

    public ForecastDayItem() {

    }

    public ForecastDayItem(JSONObject jsonObject) {
        JSONObject dayForecast = jsonObject;

        JSONObject weatherObject = null;
        try {
            date = new Date(dayForecast.getLong(OWM_DATE) * 1000);
            weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            description = weatherObject.getString(OWM_DESCRIPTION);

            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
            maxTemp = (int)temperatureObject.getDouble(OWM_MAX);
            minTemp = (int)temperatureObject.getDouble(OWM_MIN);
            weatherID = weatherObject.getInt(OWM_WEATHER_ID);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDay() {
        return Utility.DayItemFormat.format(date);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getMaxTemp() {
        return maxTemp;
    }

    @Override
    public int getMinTemp() {
        return minTemp;
    }

    @Override
    public int getWeatherID() {
        return weatherID;
    }

}
