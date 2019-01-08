package com.data4help.data4help1.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.data4help.data4help1.fragments.AsosFragment;
import com.data4help.data4help1.fragments.HomeFragment;
import com.data4help.data4help1.fragments.SettingsFragment;
import com.data4help.data4help1.fragments.ThirdPartiesFragment;

import static com.data4help.data4help1.R.id.*;
import static com.data4help.data4help1.R.layout.*;
import static com.data4help.data4help1.R.string.*;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);

        Toolbar toolbar = findViewById(com.data4help.data4help1.R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, navigation_drawer_open, navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(frame_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(nav_heart);
        }

        queue = Volley.newRequestQueue(MenuActivity.this);
        HealthParamActivity healthParamActivity = new HealthParamActivity();
        healthParamActivity.startThread();
    }


    /**
     * @param menuItem is the group of items in the manu
     * @return always true
     *
     * Opens the chosen fragment and close the drawer
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = fragmentChoice(menuItem.getItemId());
        assert fragment != null;
        getSupportFragmentManager().beginTransaction().replace(frame_container, fragment).commit();
        DrawerLayout drawer =findViewById(drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * @param itemId is the id of the chosen icon in the app's menu
     * @return the fragment related to the chosen icon
     */
    @Nullable
    private Fragment fragmentChoice(int itemId) {
        switch (itemId) {
            case nav_heart:
                return new HomeFragment();
            case nav_hospital:
                return new AsosFragment();
            case nav_pocket:
                return new ThirdPartiesFragment();
            case nav_settings:
                return new SettingsFragment();
            default:
                return null;
        }

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}