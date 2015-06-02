package andrii.goncharenko.forecastapp;

import java.text.SimpleDateFormat;

/**
 * Created by Andrey on 29.05.2015.
 */
public interface IForecastItem{

    public String getDay();
    public String getDescription();
    public int getMaxTemp();
    public int getMinTemp();
    public int getWeatherID();

}
