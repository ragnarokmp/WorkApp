package it.mobileprogramming.ragnarok.workapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.FillFormatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;
import it.mobileprogramming.ragnarok.workapp.GymModel.User;
import it.mobileprogramming.ragnarok.workapp.GymModel.WeightItem;
import it.mobileprogramming.ragnarok.workapp.util.App;
import it.mobileprogramming.ragnarok.workapp.util.BaseActivityWithToolbar;

public class AccountActivity extends BaseActivityWithToolbar {

    private User currentUser;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_account;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_account_activty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SQLiteSerializer dbSerializer = ((App) this.getApplication()).getDBSerializer();
        dbSerializer.open();

        //TODO TO BE REMOVED
        User anUser = dbSerializer.loadUser(1);
        ((App) this.getApplication()).setCurrentUser(anUser);
        //END TO BE REMOVED

        currentUser = ((App) this.getApplication()).getCurrentUser();
        TextView tvTop       =   (TextView)  findViewById(R.id.account_text_view);
        TextView tvDetails   =   (TextView)  findViewById(R.id.account_text_view_details);
        System.out.println(tvTop);
        System.out.println(tvDetails);
        tvTop.setText(currentUser.getStrName() + " " + currentUser.getStrSurname());
        String gender=getString(R.string.account_unknown);;
        if(currentUser.getIntGender()==0){
            gender  =   getString(R.string.account_male);
        }
        else if(currentUser.getIntGender()==1){
            gender  =   getString(R.string.account_female);
        }
        DateFormat format       =   new DateFormat();
        String birthdate   = format.format("dd/MM/yyyy",currentUser.getDateBirth()).toString();
        tvDetails.setText(gender + ", " + birthdate);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.add_fab);
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
                                Log.i(TAG, "New weight: " + input.toString() + " kg");
                                Date today = new Date();
                                WeightItem weightToday = new WeightItem();
                                weightToday.value = Float.valueOf(input.toString());
                                weightToday.date = today;
                                currentUser.addToWeightHistory(weightToday, true);
                                loadGraphData();

                            }
                        }).show();
            }
        });
        loadGraphData();
        String todayString = format.format("dd/MM/yyyy", new Date()).toString();
        ArrayList<WeightItem> history = currentUser.getWeightHistory();
        for (int y = 0; y < history.size(); y++) {
            String itemString = format.format("dd/MM/yyyy", history.get(y).date).toString();
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


        for(int t=0;t<history.size();t++){
            System.out.println(history.get(t).date + " " + history.get(t).value);
        }


        Collections.sort(history);

        for(int t=0;t<history.size();t++){
            System.out.println(history.get(t).date + " " + history.get(t).value);
        }


        LineData line = new LineData();
        final ArrayList<Entry> dataset = new ArrayList<>();
        ArrayList<String>labels = new ArrayList<>();    //entry labels
        for (int i = 0; i < history.size(); i++) {
            WeightItem anItem   =   history.get(i);
            dataset.add(new Entry(anItem.value, i));
            DateFormat format = new DateFormat();
            String dateString   = format.format("dd/MM/yyyy",anItem.date).toString();
            labels.add(dateString);
        }
        LineDataSet aDataset = new LineDataSet(dataset, currentUser.getStrName());

        int color   = Color.BLACK;
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
            color = getResources().getColor(R.color.primary);

        }
        aDataset.setColor(color);
        aDataset.setCircleColor(color);
        line.addDataSet(aDataset);
        ArrayList<LineDataSet> datasets =   new ArrayList<>();
        datasets.add(aDataset);
        LineData data=  new LineData(labels,datasets);
        chart.setData(data);
    }
}
