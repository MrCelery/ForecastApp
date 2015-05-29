package andrii.goncharenko.forecastapp;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Andrey on 29.05.2015.
 */
public class ForecastDayItem implements IForecastItem{


    final String OWM_WEATHER = "weather";
    final String OWM_TEMPERATURE = "temp";
    final String OWM_MAX = "max";
    final String OWM_MIN = "min";
    final String OWM_DESCRIPTION = "main";
    final String OWM_DATE = "dt";

    Date date;
    String description;
    String highAndLow;

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
            double high = temperatureObject.getDouble(OWM_MAX);
            double low = temperatureObject.getDouble(OWM_MIN);
            highAndLow = formatHighLows(high, low);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formatHighLows(double high, double low) {
        // For presentation, assume the user doesn't care about tenths of a degree.
        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String highLowStr = roundedHigh + "/" + roundedLow;
        return highLowStr;
    }

    @Override
    public String getItemStr() {
        return DayItemFormat.format(date) +" Temp: "+ highAndLow + " " + description;
    }
}
