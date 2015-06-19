package it.mobileprogramming.ragnarok.workapp.util;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import it.mobileprogramming.ragnarok.workapp.R;

/**
 * Base App Compact Activity that add also Navigation Drawer.
 * @author pincopallino93
 * @version 1.2
 */
public abstract class BaseActivityWithNavigationDrawer extends BaseActivity {

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupDrawerLayout();

        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_menu);
    }

    private void setupDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.inflateHeaderView(getHeaderView());
        navigationView.inflateMenu(getNavigationMenu());
        navigationView.setNavigationItemSelectedListener(getNavigationListener());

    }

    protected abstract int getNavigationMenu();

    protected abstract int getHeaderView();

    protected abstract NavigationView.OnNavigationItemSelectedListener getNavigationListener();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}