package it.mobileprogramming.ragnarok.workapp.util;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;
import it.mobileprogramming.ragnarok.workapp.R;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {



    private DatePickerCallback mCallbacks = datePickerCallback;

    //define callback interface
    public interface DatePickerCallback {

        void pickerOnDateSet(Calendar result, Integer workout_id);
    }

    private static DatePickerCallback datePickerCallback = new DatePickerCallback() {
        @Override
        public void pickerOnDateSet(Calendar result, Integer workout_id) {

        }
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Get the old date from the text view
        CharSequence oldDate = ((TextView) getActivity().findViewById(R.id.date_text_button)).getText();

        int year;
        int month;
        int day;

        try {
            year = Integer.parseInt(oldDate.subSequence(6, 10).toString());
            month = Integer.parseInt(oldDate.subSequence(3, 5).toString()) - 1;
            day = Integer.parseInt(oldDate.subSequence(0, 3).toString());
        } catch (Exception string) {
            final Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }


        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof DatePickerCallback)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (DatePickerCallback) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = datePickerCallback;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {

        // Set the date chosen to text view
       // ((TextView) getActivity().findViewById(R.id.details_text_button)).setText(String.format("%02d/%02d/%d", day, month + 1, year));

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day, 0, 0, 0);
        Date date = cal.getTime(); // get back a Date object

        Bundle bundle = this.getArguments();
        int workout_id = bundle.getInt("WORKOUT_ID", Integer.MAX_VALUE);

        mCallbacks.pickerOnDateSet(cal, workout_id);
    }
}