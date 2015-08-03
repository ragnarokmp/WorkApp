package it.mobileprogramming.ragnarok.workapp;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;
import it.mobileprogramming.ragnarok.workapp.GymModel.User;
import it.mobileprogramming.ragnarok.workapp.GymModel.WeightItem;
import it.mobileprogramming.ragnarok.workapp.util.App;
import it.mobileprogramming.ragnarok.workapp.util.BaseActivityWithToolbar;

public class AccountActivity extends BaseActivityWithToolbar {
    TextView tvTop;
    User currentUser;
    LineChart chart;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_account;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account_activty, menu);
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
    protected void onStart() {
        super.onStart();
        SQLiteSerializer dbSerializer = ((App) this.getApplication()).getDBSerializer();
        dbSerializer.open();

        //TODO TO BE REMOVED
        User anUser = dbSerializer.loadUser(1);
        ((App) this.getApplication()).setCurrentUser(anUser);
        //END TO BE REMOVED

        currentUser = ((App) this.getApplication()).getCurrentUser();
        tvTop   =  (TextView)findViewById(R.id.tv_top);
        tvTop.setText(currentUser.getStrName()+" "+currentUser.getStrSurname());
        chart = (LineChart) findViewById(R.id.lc_chart);
        chart.setDescription(getString(R.string.account_trend));
        ArrayList<WeightItem> history = currentUser.getWeightHistory();
        LineData line = new LineData();
        ArrayList<Entry> dataset = new ArrayList<Entry>();
        ArrayList<String>labels =   new ArrayList<String>();    //entry labels
        for (int i = 0; i < history.size(); i++) {
            WeightItem anItem   =   history.get(i);
            dataset.add(new Entry(anItem.value, i));
            labels.add(anItem.date.getDay()+"/"+anItem.date.getMonth()+"/"+anItem.date.getYear());
        }
        LineDataSet aDataset = new LineDataSet(dataset, currentUser.getStrName());

        TypedValue a = new TypedValue();
        int color;
        getTheme().resolveAttribute(android.R.attr.colorPrimary, a, true);//TODO works only from API 21!!!
        if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            // windowBackground is a color
            color = a.data;
        } else {
            // windowBackground is not a color, probably a drawable
            color   = Color.BLACK;
        }



        aDataset.setColor(color);
        aDataset.setCircleColor(color);
        line.addDataSet(aDataset);
        ArrayList<LineDataSet> datasets =   new ArrayList<LineDataSet>();
        datasets.add(aDataset);
        LineData data=  new LineData(labels,datasets);
        chart.setData(data);
    }
}
