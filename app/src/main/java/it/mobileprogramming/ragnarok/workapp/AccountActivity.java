package it.mobileprogramming.ragnarok.workapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;
import it.mobileprogramming.ragnarok.workapp.GymModel.Singletons;
import it.mobileprogramming.ragnarok.workapp.GymModel.User;
import it.mobileprogramming.ragnarok.workapp.GymModel.WeightItem;
import it.mobileprogramming.ragnarok.workapp.util.App;
import it.mobileprogramming.ragnarok.workapp.util.BaseActivityWithToolbar;
import it.mobileprogramming.ragnarok.workapp.util.BitmapHelper;

public class AccountActivity extends BaseActivityWithToolbar {

    private User currentUser;
    private FloatingActionButton floatingActionButton;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_account;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.add_fab);

        // setting user name and avatar and other userspecifications
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (pref.contains("signed_in")) {
            TextView account = (TextView)findViewById(R.id.account_text_view);

            String account_name = ((App) getApplication()).getCurrentUser().getStrName();
            String account_gen  = (((App) getApplication()).getCurrentUser().getIntGender() == 0) ?
                    getApplication().getResources().getString(R.string.account_male) :
                    getApplication().getResources().getString(R.string.account_female);

            String account_reg  = getResources().getString(R.string.account_registered) + " " +
                    Singletons.getStringFromDate(
                            ((App) getApplication()).getCurrentUser().getDateRegistration());

            account.setText(account_name + ", " + account_gen + "\n" + account_reg);

            if (pref.contains("personAvatarBitmap")) {
                ((ImageView) findViewById(R.id.avatar)).setImageBitmap(BitmapHelper
                        .decodeBase64(pref.getString("personAvatarBitmap", null)));
            }
        }

        SQLiteSerializer dbSerializer = ((App) this.getApplication()).getDBSerializer();
        dbSerializer.open();

        ((App) this.getApplication()).setCurrentUser(dbSerializer.loadUser(
                pref.getString("userEmail", "username@gmail.com")));
        currentUser = ((App) this.getApplication()).getCurrentUser();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(AccountActivity.this)
                        .title(R.string.account_add_title)
                        .content(R.string.account_add_content)
                        .inputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL)
                        .positiveText(R.string.submit)
                        .input(getString(R.string.account_add_hint), null, true, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {

                                Date today = new Date();
                                WeightItem weightToday = new WeightItem();
                                weightToday.value = Float.valueOf(input.toString());
                                weightToday.date = today;
                                currentUser.addToWeightHistory(weightToday, true);
                                loadGraphData();

                                // disable floating button
                                floatingActionButton.setEnabled(false);
                                floatingActionButton.hide();

                            }
                        }).show();
            }
        });


        loadGraphData();
        String todayString = DateFormat.format("dd/MM/yyyy", new Date()).toString();
        ArrayList<WeightItem> history = currentUser.getWeightHistory();

        for (int y = 0; y < history.size(); y++) {
            String itemString = DateFormat.format("dd/MM/yyyy", history.get(y).date).toString();
            if (itemString.equals(todayString)) {
                floatingActionButton.setEnabled(false);
                floatingActionButton.hide();
            }
        }
    }

    public void loadGraphData(){
        LineChart chart = (LineChart) findViewById(R.id.lc_chart);
        YAxis leftAxis = chart.getAxisLeft();
        YAxis rightAxis = chart.getAxisRight();
        leftAxis.setStartAtZero(false);
        rightAxis.setStartAtZero(false);
        chart.setDescription(getString(R.string.account_trend));
        ArrayList<WeightItem> history = currentUser.getWeightHistory();

        Collections.sort(history);

        LineData line = new LineData();
        final ArrayList<Entry> dataSet = new ArrayList<>();
        ArrayList<String>labels = new ArrayList<>();    //entry labels
        for (int i = 0; i < history.size(); i++) {
            WeightItem anItem   =   history.get(i);
            dataSet.add(new Entry(anItem.value, i));
            String dateString   = DateFormat.format("dd/MM/yyyy", anItem.date).toString();
            labels.add(dateString);
        }
        LineDataSet aDataSet = new LineDataSet(dataSet, currentUser.getStrName());

        int color = Color.BLACK;
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;

        if (currentApiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
            color = getResources().getColor(R.color.primary);
        }

        aDataSet.setColor(color);
        aDataSet.setCircleColor(color);
        line.addDataSet(aDataSet);
        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(aDataSet);
        LineData data = new LineData(labels,dataSets);
        chart.setData(data);
    }
}
