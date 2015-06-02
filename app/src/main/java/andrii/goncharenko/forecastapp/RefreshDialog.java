package andrii.goncharenko.forecastapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by Andrey on 29.05.2015.
 */
public class RefreshDialog extends DialogFragment implements DialogInterface.OnClickListener {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle(getActivity().getString(R.string.refresh_dialog_title))
                .setPositiveButton(getActivity().getString(R.string.yes), this)
                .setNegativeButton(getActivity().getString(R.string.no), this)
                .setMessage(R.string.refresh_dialog_text);
        return adb.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                ForecastDayFragment fragment = (ForecastDayFragment) getActivity().getSupportFragmentManager().findFragmentByTag("forecastFragment");
                if (fragment != null)
                    fragment.updateWeather();
                break;
            case Dialog.BUTTON_NEGATIVE:
                break;
        }
    }

}
