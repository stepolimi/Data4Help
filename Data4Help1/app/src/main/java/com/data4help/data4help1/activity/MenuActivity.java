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

import com.data4help.data4help1.fragments.AsosFragment;
import com.data4help.data4help1.fragments.HomeFragment;
import com.data4help.data4help1.fragments.SettingsFragment;
import com.data4help.data4help1.fragments.ThirdPartiesFragment;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.data4help.data4help1.R.layout.activity_main);

        Toolbar toolbar = findViewById(com.data4help.data4help1.R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(com.data4help.data4help1.R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, com.data4help.data4help1.R.string.navigation_drawer_open, com.data4help.data4help1.R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(com.data4help.data4help1.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(com.data4help.data4help1.R.id.frame_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(com.data4help.data4help1.R.id.nav_heart);
        }
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
        getSupportFragmentManager().beginTransaction().replace(com.data4help.data4help1.R.id.frame_container, fragment).commit();
        DrawerLayout drawer =findViewById(com.data4help.data4help1.R.id.drawer_layout);
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
            case com.data4help.data4help1.R.id.nav_heart:
                return new HomeFragment();
            case com.data4help.data4help1.R.id.nav_hospital:
                return new AsosFragment();
            case com.data4help.data4help1.R.id.nav_pocket:
                return new ThirdPartiesFragment();
            case com.data4help.data4help1.R.id.nav_settings:
                return new SettingsFragment();
            default:
                return null;
        }

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(com.data4help.data4help1.R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}