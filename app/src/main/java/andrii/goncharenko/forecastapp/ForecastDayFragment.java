package andrii.goncharenko.forecastapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ForecastDayFragment extends Fragment {

    private static final int THREE_HOUR_VIEW = 0;
    private static final int DAY_VIEW = 1;

    private static final String DAY_INTERVAL_PARAM = "daily?";
    private static final String THREE_HOUR_INTERVAL_PARAM = "";

    ForecastAdapter adapter;
    MenuItem actionProgress;
    MenuItem actionRefresh;
    List<IForecastItem> forecastItems = new ArrayList<>();

    public ForecastDayFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        actionProgress = menu.findItem(R.id.action_progress);
        actionRefresh = menu.findItem(R.id.action_refresh);
        ProgressBar progressBar;
        if (actionProgress != null)
            progressBar =  (ProgressBar) MenuItemCompat.getActionView(actionProgress);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_refresh) {
            RefreshDialog refreshDialog = new RefreshDialog();
            refreshDialog.show(getFragmentManager(), "refreshDialog");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_forecast, container, false);
        adapter = new ForecastAdapter(this.getActivity(), R.layout.list_item_forecast, forecastItems);
        ListView listView = (ListView) rootView.findViewById(R.id.listView_forecast);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }

        });

        return rootView;
    }

    public void updateWeather() {
        actionProgress.setVisible(true);
        actionRefresh.setVisible(false);
        FetchWeatherTask weatherTask = new FetchWeatherTask();
        String intervalType =
                ((MainActivity)getActivity()).getCurrentTab() == THREE_HOUR_VIEW
                    ? THREE_HOUR_INTERVAL_PARAM
                    : DAY_INTERVAL_PARAM;
        weatherTask.execute(intervalType, getActivity().getString(R.string.pref_location_default),
                getActivity().getString(R.string.pref_units_default), Locale.getDefault().getLanguage());
    }

    public class FetchWeatherTask extends AsyncTask<String, Void, List<IForecastItem>> {

        private final String LOG_TAG = FetchWeatherTask.class.getName();

        @Override
        protected List<IForecastItem> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String forecastJsonStr = null;
            String format = "json";
            int days = 14;
            try {
                final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/";

                final String QUERY_PARAM = "q";
                final String FORMAT_PARAM = "mode";
                final String UNITS_PARAM = "units";
                final String DAYS_PARAM = "cnt";
                final String LANG_PARAM = "lang";
                Uri builtUri = Uri.parse(FORECAST_BASE_URL + params[0])
                        .buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[1])
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, params[2])
                        .appendQueryParameter(DAYS_PARAM, Integer.toString(days))
                        .appendQueryParameter(LANG_PARAM, "ua")
                        .build();

                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                     return null;
                }
                forecastJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {;
                return getWeatherDataFromJson(forecastJsonStr, params[0]);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        public FetchWeatherTask() {
            super();
        }

        private List<IForecastItem> getWeatherDataFromJson(String forecastJsonStr, String viewType)
                throws JSONException {

            final String OWM_LIST = "list";

            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

            List<IForecastItem> items = new ArrayList<>();

            for (int i = 0; i < weatherArray.length(); i++) {
                if (viewType.equals(DAY_INTERVAL_PARAM))
                    items.add(new ForecastDayItem(weatherArray.getJSONObject(i)));
                else
                    items.add(new ForecastThreeHourItem(weatherArray.getJSONObject(i)));
            }
            return items;
        }

        @Override
        protected void onPostExecute(List<IForecastItem> strings) {
            if (strings != null) {
                adapter.clear();
                for (IForecastItem forecastItem : strings)
                    adapter.add(forecastItem);
            }
            actionProgress.setVisible(false);
            actionRefresh.setVisible(true);
        }
    }

}
