package it.mobileprogramming.ragnarok.workapp.util;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import it.mobileprogramming.ragnarok.workapp.R;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Get the old date from the text view
        CharSequence oldDate = ((TextView) getActivity().findViewById(R.id.details_text_button)).getText();

        int year;
        int month;
        int day;

        try {
            year = Integer.parseInt(oldDate.subSequence(6, 10).toString());
            month = Integer.parseInt(oldDate.subSequence(3, 5).toString()) - 1;
            day = Integer.parseInt(oldDate.subSequence(0, 2).toString());
        } catch (StringIndexOutOfBoundsException sting) {
            final Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }


        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Set the date chosen to text view
        ((TextView) getActivity().findViewById(R.id.details_text_button)).setText(String.format("%02d/%02d/%d", day, month + 1, year));
    }
}