package wittyapp.draegerit.de.wittyapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import wittyapp.draegerit.de.wittyapp.examples.PhotoresistorView;
import wittyapp.draegerit.de.wittyapp.util.AbstractView;
import wittyapp.draegerit.de.wittyapp.util.EActiveView;
import wittyapp.draegerit.de.wittyapp.util.IPAddressValidator;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String EMPTY = "";
    private static final int ZERO = 0;

    private ViewSwitcher viewSwitcher;
    private NavigationView navigationView;

    private AbstractView activeView;

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

        activeView = new HomeView(getApplicationContext(), viewSwitcher);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem clearMItem = menu.findItem(R.id.action_clear);
        MenuItem downloadMItem = menu.findItem(R.id.action_download);
        MenuItem timerMItem = menu.findItem(R.id.action_timer);

        if (activeView.getActiveView().equals(EActiveView.PHOTORESISTOR)) {
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

        switch (id) {
            case R.id.action_clear:
                activeView.clearData();
                break;
            case R.id.action_timer:
                showUpdateIntervalDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showUpdateIntervalDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_interval_dialog, null);
        dialogBuilder.setTitle(getString(R.string.set_updateInterval));
        dialogBuilder.setView(dialogView);
        dialogBuilder.setIcon(R.drawable.ic_timer_blue_24dp);
        final RadioGroup updateIntervalRadioGroup = dialogView.findViewById(R.id.updateIntervalRadioGroup);

        RadioButton rBtn = null;
        switch (activeView.getUpdateInterval()) {
            default:
            case 1000:
                rBtn = dialogView.findViewById(R.id.oneSecRBtn);
                break;
            case 5000:
                rBtn = dialogView.findViewById(R.id.fiveSecRBtn);
                break;
            case 10000:
                rBtn = dialogView.findViewById(R.id.tenSecRBtn);
                break;
            case 15000:
                rBtn = dialogView.findViewById(R.id.fiveteenSecRBtn);
                break;
        }
        if (rBtn != null) {
            rBtn.setChecked(true);
        }


        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        Button okBtn = dialogView.findViewById(R.id.okBtn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int updateInterval = 0;

                int checkedRadioButtonId = updateIntervalRadioGroup.getCheckedRadioButtonId();
                switch (checkedRadioButtonId) {
                    case R.id.oneSecRBtn:
                        updateInterval = 1;
                        break;
                    case R.id.fiveSecRBtn:
                        updateInterval = 5;
                        break;
                    case R.id.tenSecRBtn:
                        updateInterval = 10;
                        break;
                    case R.id.fiveteenSecRBtn:
                        updateInterval = 15;
                        break;
                    default:
                        Log.d("WittyApp", "ID not found!");
                        break;
                }

                activeView.setUpdateInterval(updateInterval * 1000);
                alertDialog.dismiss();
            }
        });

        Button abortBtn = dialogView.findViewById(R.id.abortBtn);
        abortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
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
        getIpAdressFromDialog();


        boolean connectionSuccessfull = true;
        toggleConnectionState(connectionSuccessfull);
    }

    private void getIpAdressFromDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(getString(R.string.connectToDevice));
        dialogBuilder.setIcon(R.drawable.ic_phonelink_blue_24dp);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.ipaddress_dialog, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        Button okBtn = dialogView.findViewById(R.id.okBtn);
        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText ipAdressEditText = dialogView.findViewById(R.id.ipAddressEditText);
                String ipAdress = ipAdressEditText.getText().toString();
                if (new IPAddressValidator().validate(ipAdress)) {
                    boolean successfull = tryToConnect(ipAdress);
                    if (successfull) {
                        alertDialog.dismiss();
                    }
                } else {
                    TextView ipAddressValidateTextView = dialogView.findViewById(R.id.ipAddressValidateTextView);
                    ipAddressValidateTextView.setVisibility(View.VISIBLE);
                }
            }
        });

        Button abortBtn = dialogView.findViewById(R.id.abortBtn);
        abortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    private boolean tryToConnect(String ipAdress) {
        return true;
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
        activeView = new PhotoresistorView(getApplicationContext(), viewSwitcher);
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
