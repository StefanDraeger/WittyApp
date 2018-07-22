package wittyapp.draegerit.de.wittyapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import wittyapp.draegerit.de.wittyapp.util.EActiveView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String EMPTY = "";
    private static final int ZERO = 0;
    private ViewSwitcher viewSwitcher;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.viewSwitcher = findViewById(R.id.viewSwitcher);

        TextView emailTextView = navigationView.getHeaderView(ZERO).findViewById(R.id.emailTextView);
        emailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL  , new String[] { getString(R.string.email) });
                intent.putExtra(Intent.EXTRA_SUBJECT, EMPTY);

                startActivity(Intent.createChooser(intent, getString(R.string.app_name)));
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_connect:
                connectToDevice();
                break;
            case R.id.nav_disconnect:
                disconnectFromDevice();
                break;
            case R.id.nav_ldr_photoresitor:
                loadView(EActiveView.PHOTORESISTOR);
                break;
            case R.id.nav_rgb_led:
                loadView(EActiveView.RGB_LED);
                break;
            case R.id.nav_impressum:
                break;
            case R.id.nav_privacy_policy:
                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void disconnectFromDevice() {
        boolean disconnectionSuccessfull = true;
        toggleConnectionState(!disconnectionSuccessfull);
    }

    private void connectToDevice() {
        boolean connectionSuccessfull = true;
        toggleConnectionState(connectionSuccessfull);
    }

    private void toggleConnectionState(boolean connectionSuccessfull) {
        Menu menuNav = navigationView.getMenu();
        MenuItem disconnectMItem = menuNav.findItem(R.id.nav_disconnect);
        disconnectMItem.setEnabled(connectionSuccessfull);

        MenuItem connectMItem = menuNav.findItem(R.id.nav_connect);
        connectMItem.setEnabled(!connectionSuccessfull);
    }

    private void loadView(EActiveView activeView) {
        if (!activeView.equals(EActiveView.PHOTORESISTOR)) {
            switchLayout(activeView.getLayoutResId());
        } else {
            viewSwitcher.setDisplayedChild(activeView.getPosition());
        }


    }

    private void switchLayout(int layoutResId) {
        View old = viewSwitcher.getChildAt(1);
        viewSwitcher.removeView(old);
        LayoutInflater mInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(layoutResId, null, true);

        viewSwitcher.addView(view);
        viewSwitcher.setDisplayedChild(1);
    }
}
