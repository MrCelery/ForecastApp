package andrii.goncharenko.forecastapp;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Y50-70 on 30.05.2015.
 */
public class ForecastAdapter extends ArrayAdapter<IForecastItem> {

    private int layoutResourceId;
    private Context context;
    private List<IForecastItem> forecastItems;

    public ForecastAdapter(Context context, int layoutResourceId, List<IForecastItem> forecastItems) {
        super(context, layoutResourceId, forecastItems);

        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.forecastItems = forecastItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;
        IForecastItem item = forecastItems.get(position);
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(layoutResourceId, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) view.getTag();
        viewHolder.forecastIconView.setImageResource(Utility.getIconResourceForWeatherCondition(item.getWeatherID()));
        viewHolder.dateTextView.setText(item.getDay());
        viewHolder.descriptionTextView.setText(item.getDescription());
        viewHolder.maxTempTextView.setText(context.getString(R.string.format_temperature, item.getMaxTemp()));
        viewHolder.minTempTextView.setText(context.getString(R.string.format_temperature, item.getMinTemp()));
        return view;
    }



    public static class ViewHolder {
        public final ImageView forecastIconView;
        public final TextView dateTextView;
        public final TextView descriptionTextView;
        public final TextView maxTempTextView;
        public final TextView minTempTextView;

        public ViewHolder(View view) {
            forecastIconView = (ImageView) view.findViewById(R.id.list_item_forecast_icon);
            dateTextView = (TextView) view.findViewById(R.id.list_item_date_textView);
            descriptionTextView = (TextView) view.findViewById(R.id.list_item_description_textView);
            maxTempTextView = (TextView) view.findViewById(R.id.list_item_max_temp_textView);
            minTempTextView = (TextView) view.findViewById(R.id.list_item_min_temp_textView);
        }

    }

}
