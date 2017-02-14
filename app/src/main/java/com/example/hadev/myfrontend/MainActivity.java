package com.example.hadev.myfrontend;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";

    MyPermissionManager permissionManager = new MyPermissionManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (contactsIsShown()){
            clearContent();
            drawer.openDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            clearContent();
        } else if (id == R.id.nav_gallery) {
            showImage();
        } else if (id == R.id.nav_contacts) {
            // Check that the activity is using the layout version with the fragment_container FrameLayout
            if (findViewById(R.id.fragment_container) != null &&
                    // Let the permission manager check whether the permission is already granted or not.
                    permissionManager.checkPermission(this, Manifest.permission.READ_CONTACTS)) {
                showContacts();
            }
        } else if (id == R.id.nav_manage) {
            clearContent();
        } else if (id == R.id.nav_share) {
            clearContent();
        } else if (id == R.id.nav_send) {
            clearContent();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Wait for permissions to be granted.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (permissionManager.onRequestPermissionsResult(this,requestCode,permissions,grantResults)) {
            showContacts();
        }
    }

    /**
     * Show the contacts in a fragment.
     */
    private void showContacts() {
        // Create a new Fragment to be placed in the activity layout
        ContactsFragment contactsFragment = new ContactsFragment();

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container, contactsFragment).commit();
    }

    /**
     * Show the contacts in a fragment.
     */
    private void clearContent() {
        for(Fragment fragment : getSupportFragmentManager().getFragments()){
            if (fragment != null) {
                Log.d(TAG, fragment.toString());
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
         }
    }

    private boolean contactsIsShown() {
        for(Fragment fragment : getSupportFragmentManager().getFragments()){
            if (fragment != null && fragment.getClass().equals(ContactsFragment.class)) {
                Log.d(TAG, "Contants fragment is shown");
                return true;
            }
        }
        return false;
    }

    /**
     * Show the selected image in a fragment.
     */
    private void showImage() {
        // Create new fragment
        Fragment imageFragment = new ImageFragment();

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container, imageFragment).commit();
    }

}
