package andrii.goncharenko.forecastapp;

import java.text.SimpleDateFormat;

/**
 * Created by Andrey on 29.05.2015.
 */
public interface IForecastItem {

    public String getItemStr();
    final SimpleDateFormat JSONDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    final SimpleDateFormat DayItemFormat = new SimpleDateFormat("EEE, MMM d");
    final SimpleDateFormat ThreeHourItemFormat = new SimpleDateFormat("EEE, MMM d, HH:mm");
}
