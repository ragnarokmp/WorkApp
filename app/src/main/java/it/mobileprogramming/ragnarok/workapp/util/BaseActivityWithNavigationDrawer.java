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
 * @version 1.3
 */
public abstract class BaseActivityWithNavigationDrawer extends BaseActivityWithToolbar {
    /**
     * The Drawer Layout.
     */
    protected DrawerLayout drawerLayout;

    /**
     * The Navigation View.
     */
    protected NavigationView navigationView;

    /**
     * Abstract method used in order to return the menu of the Navigation Drawer.
     * @return the resource ID of the menu to set.
     */
    protected abstract int getNavigationMenu();

    /**
     * Abstract method used in order to return the header of the Navigation Drawer.
     * @return the resource ID of the header to set.
     */
    protected abstract int getHeaderView();

    /**
     * Abstract method used in order to return the listener to set to the Navigation Drawer.
     * @return the OnNavigationItemSelectedLister of the Navigation Drawer to set.
     */
    protected abstract NavigationView.OnNavigationItemSelectedListener getNavigationListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setup the Navigation Drawer
        setupDrawerLayout();

        // Set the icon for the Navigation Drawer
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_menu);
    }

    /**
     * This method setup all the stuffs in order to has got a Navigation Drawer.
     */
    private void setupDrawerLayout() {
        // Get DrawerLayout and the NavigationView
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        // Set header and menu
        navigationView.inflateHeaderView(getHeaderView());
        navigationView.inflateMenu(getNavigationMenu());
        // Set listener
        navigationView.setNavigationItemSelectedListener(getNavigationListener());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open Navigation Drawer when icon is tapped
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}