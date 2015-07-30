package it.mobileprogramming.ragnarok.workapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import it.mobileprogramming.ragnarok.workapp.util.BaseActivityWithToolbar;

public class AccountActivity extends BaseActivityWithToolbar {

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
}
