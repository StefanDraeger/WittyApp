package wittyapp.draegerit.de.wittyapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import wittyapp.draegerit.de.wittyapp.util.EActiveView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String EMPTY = "";
    private static final int ZERO = 0;
    private ViewSwitcher viewSwitcher;
    private NavigationView navigationView;

    private EActiveView activeView;

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
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.email)});
                intent.putExtra(Intent.EXTRA_SUBJECT, EMPTY);

                startActivity(Intent.createChooser(intent, getString(R.string.app_name)));
            }
        });

        TextView versionTextView = navigationView.getHeaderView(ZERO).findViewById(R.id.versionTextView);
        versionTextView.setText(getString(R.string.version_text, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));

        activeView = EActiveView.HOME;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem clearMItem = menu.findItem(R.id.action_clear);
        MenuItem downloadMItem = menu.findItem(R.id.action_download);
        MenuItem timerMItem = menu.findItem(R.id.action_timer);

        if (activeView.equals(EActiveView.PHOTORESISTOR)) {
            clearMItem.setVisible(true);
            downloadMItem.setVisible(true);
            timerMItem.setVisible(true);
        } else {
            clearMItem.setVisible(false);
            downloadMItem.setVisible(false);
            timerMItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_clear) {
            //return true;
        }

        return super.onOptionsItemSelected(item);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                loadView(EActiveView.HOME);
                break;
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        viewSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
        viewSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right));

        this.activeView = activeView;

        if (!activeView.equals(EActiveView.HOME)) {
            switchLayout(activeView.getLayoutResId());
        } else {
            viewSwitcher.setDisplayedChild(activeView.getPosition());
        }

        switch (activeView) {
            case HOME:
                break;
            case PHOTORESISTOR:
                loadPhotoresistorView();
                break;
            case RGB_LED:
                break;
        }

        invalidateOptionsMenu();
    }

    private void loadPhotoresistorView() {
        final LineChart lineChart = findViewById(R.id.chart);
        // creating list of entry<br />
        final ArrayList<Entry> entries = new ArrayList<>();

        final LineDataSet dataset = new LineDataSet(entries, "#Test");
        dataset.setDrawCubic(true);
        dataset.setDrawFilled(true);

        ArrayList<String> labels = new ArrayList<>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");

        final LineData data = new LineData(labels, dataset);
        lineChart.setData(data);
        lineChart.setDescription("Description");

        Button photoresistorBtnAdd = findViewById(R.id.photoresistorBtnAdd);
        photoresistorBtnAdd.setOnClickListener(new View.OnClickListener() {

            int counter = 0;

            @Override
            public void onClick(View view) {
                int i = ThreadLocalRandom.current().nextInt(0, 10 + 1);
                Entry entry = new Entry(i, ++counter);
                dataset.addEntry(entry);
                data.clearValues();
                data.addDataSet(dataset);
                LineData data = new LineData(getLabels(counter), dataset);
                lineChart.setDescription("Description");
                lineChart.setData(data);
                lineChart.animateY(500);
            }

            private List<String> getLabels(int counter) {
                List<String> labels = new ArrayList<>();
                for (int i = 0; i < counter; i++) {
                    labels.add(String.valueOf(i));
                }
                return labels;
            }
        });


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
