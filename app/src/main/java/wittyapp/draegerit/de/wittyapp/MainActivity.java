package wittyapp.draegerit.de.wittyapp;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

import wittyapp.draegerit.de.wittyapp.examples.BuzzerActivity;
import wittyapp.draegerit.de.wittyapp.examples.MatrixActivity;
import wittyapp.draegerit.de.wittyapp.examples.PhotoresistorActivity;
import wittyapp.draegerit.de.wittyapp.examples.RelayActivity;
import wittyapp.draegerit.de.wittyapp.examples.RgbLedActivity;
import wittyapp.draegerit.de.wittyapp.examples.TemperatureSensorActivity;
import wittyapp.draegerit.de.wittyapp.frames.OnFragmentInteractionListener;
import wittyapp.draegerit.de.wittyapp.frames.SettingsActivity;
import wittyapp.draegerit.de.wittyapp.util.BuzzerValue;
import wittyapp.draegerit.de.wittyapp.util.Configuration;
import wittyapp.draegerit.de.wittyapp.util.EAction;
import wittyapp.draegerit.de.wittyapp.util.EActiveView;
import wittyapp.draegerit.de.wittyapp.util.IActionView;
import wittyapp.draegerit.de.wittyapp.util.IPAddressValidator;
import wittyapp.draegerit.de.wittyapp.util.PreferencesUtil;
import wittyapp.draegerit.de.wittyapp.util.RgbColor;
import wittyapp.draegerit.de.wittyapp.util.console.ConsoleEntry;
import wittyapp.draegerit.de.wittyapp.util.console.ConsoleUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener {

    private static final String EMPTY = "";
    private static final int ZERO = 0;

    private String lastIpAddress;

    private ViewSwitcher viewSwitcher;
    private NavigationView navigationView;

    private EActiveView activeViewType;
    private IActionView actionView;

    private Timer timer;
    private TimerTask timerTask;

    private boolean timerIsRunning = false;

    private AsyncTask activeAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MobileAds.initialize(this, "ca-app-pub-8772239501259424~9390363923");
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

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

        actionView = new HomeActivity();
        activeViewType = EActiveView.HOME;
        setLastIpAddress(PreferencesUtil.getIpAddress(getApplicationContext()));

        timerTask = createDefaultTimerTask();

        toggleESPShieldFunctions(false);
        Configuration configuration = PreferencesUtil.getConfiguration(getApplicationContext());
        if (configuration.isAutoConnect()) {
            String ipAdress = PreferencesUtil.getIpAddress(getApplicationContext());
            tryToConnect(ipAdress);
        }

    }


    private TimerTask createDefaultTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                timerIsRunning = true;
                EAction action = null;
                switch (activeViewType) {
                    case HOME:
                    case CONSOLE:
                    case SETTINGS:
                    case IMPRINT:
                        break;
                    case PHOTORESISTOR:
                        action = EAction.PHOTORESISTOR;
                        break;
                    case RGB_LED:
                        break;
                    case TEMPERATUR_SENSOR:
                        action = EAction.TEMP;
                        break;
                    case BUZZER:
                        break;
                }
                ConnectionAsyncTask connectionAsyncTask = new ConnectionAsyncTask(getLastIpAddress(), action);
                connectionAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem clearMItem = menu.findItem(R.id.action_clear);
        MenuItem downloadMItem = menu.findItem(R.id.action_download);
        MenuItem timerMItem = menu.findItem(R.id.action_timer);
        MenuItem timeroffMItem = menu.findItem(R.id.action_timeroff);
        MenuItem refreshMItem = menu.findItem(R.id.action_refresh);
        MenuItem checkMItem = menu.findItem(R.id.action_check);
        MenuItem saveMItem = menu.findItem(R.id.action_save);

        clearMItem.setVisible(false);
        downloadMItem.setVisible(false);
        timerMItem.setVisible(false);
        timeroffMItem.setVisible(false);
        refreshMItem.setVisible(false);
        checkMItem.setVisible(false);
        saveMItem.setVisible(false);

        if (activeViewType.equals(EActiveView.PHOTORESISTOR) || activeViewType.equals(EActiveView.TEMPERATUR_SENSOR)) {
            clearMItem.setVisible(true);
            //downloadMItem.setVisible(true);
            timerMItem.setVisible(true);
            timeroffMItem.setVisible(true);
        } else if (activeViewType.equals(EActiveView.CONSOLE)) {
            clearMItem.setVisible(true);
            refreshMItem.setVisible(true);
        } else if (activeViewType.equals(EActiveView.RGB_LED) ||
                activeViewType.equals(EActiveView.RGB_LED2) ||
                activeViewType.equals(EActiveView.BUZZER)) {
            checkMItem.setVisible(true);
        } else if (activeViewType.equals(EActiveView.MATRIX)) {
            clearMItem.setVisible(true);
        } else if (activeViewType.equals(EActiveView.SETTINGS)) {
            saveMItem.setVisible(true);
        }

        timerMItem.setEnabled(!timerIsRunning);
        timeroffMItem.setEnabled(timerIsRunning);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_clear:
                actionView.clearData();
                break;
            case R.id.action_timer:
                showUpdateIntervalDialog();
                break;
            case R.id.action_refresh:
                actionView.update();
                break;
            case R.id.action_timeroff:
                cancelTimer();
                break;
            case R.id.action_check:
                sendToDevice();
                break;
            case R.id.action_save:
                doSave();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void doSave() {
        actionView.save();
    }


    private void sendToDevice() {
        if (activeViewType.equals(EActiveView.RGB_LED) || activeViewType.equals(EActiveView.RGB_LED2)) {
            RgbLedActivity rgbLedActivity = (RgbLedActivity) actionView;
            int red = rgbLedActivity.getRedValue();
            int green = rgbLedActivity.getGreenValue();
            int blue = rgbLedActivity.getBlueValue();
            PreferencesUtil.storeRgbColor(getApplicationContext(), new RgbColor(red, green, blue));
            ConnectionAsyncTask connectionAsyncTask = new ConnectionAsyncTask(getLastIpAddress(), EAction.RGB_LED);
            connectionAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else if (actionView instanceof BuzzerActivity) {
            BuzzerActivity buzzerActivity = (BuzzerActivity) actionView;
            PreferencesUtil.storeBuzzerValue(getApplicationContext(), new BuzzerValue(buzzerActivity.getFrequenz(), buzzerActivity.getDuration()));
            ConnectionAsyncTask connectionAsyncTask = new ConnectionAsyncTask(getLastIpAddress(), EAction.BUZZER);
            connectionAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else if (actionView instanceof MatrixActivity) {
            ConnectionAsyncTask connectionAsyncTask = new ConnectionAsyncTask(getLastIpAddress(), EAction.MATRIX);
            connectionAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private void showUpdateIntervalDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_interval_dialog, null);
        dialogBuilder.setTitle(getString(R.string.set_updateInterval));
        dialogBuilder.setView(dialogView);
        dialogBuilder.setIcon(R.drawable.ic_timer_blue_24dp);
        final RadioGroup updateIntervalRadioGroup = dialogView.findViewById(R.id.updateIntervalRadioGroup);

        RadioButton rBtn;
        switch (actionView.getUpdateInterval()) {
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

                actionView.setUpdateInterval(updateInterval * 1000);
                cancelTimer();
                startTimer();
                toggleStartTimerAction();
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

    private void toggleStartTimerAction() {
        invalidateOptionsMenu();
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
        cancelTimer();
        if (activeAsyncTask != null) {
            activeAsyncTask.cancel(true);
        }


        int id = item.getItemId();
        switch (id) {
            case R.id.nav_connect:
                connectToDevice(null);
                break;
            default:
                loadView(EActiveView.findByMenuItemId(id));
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void connectToDevice(String connectionErrorMsg) {
        getIpAdressFromDialog(connectionErrorMsg);
    }

    private void getIpAdressFromDialog(String connectionErrorMsg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(getString(R.string.connectToDevice));
        dialogBuilder.setIcon(R.drawable.ic_phonelink_blue_24dp);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.ipaddress_dialog, null);
        dialogBuilder.setView(dialogView);

        EditText ipAdressEditText = dialogView.findViewById(R.id.ipAddressEditText);
        ipAdressEditText.setText(getLastIpAddress() != null ? getLastIpAddress() : "");

        if (connectionErrorMsg != null) {
            TextView connectionRefusedTextView = dialogView.findViewById(R.id.connectionRefusedTextView);
            connectionRefusedTextView.setVisibility(View.VISIBLE);
            connectionRefusedTextView.setText(connectionErrorMsg);
        }

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        Button okBtn = dialogView.findViewById(R.id.okBtn);
        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText ipAdressEditText = dialogView.findViewById(R.id.ipAddressEditText);
                TextView ipAddressValidateTextView = dialogView.findViewById(R.id.ipAddressValidateTextView);
                ipAddressValidateTextView.setVisibility(View.GONE);
                String ipAdress = ipAdressEditText.getText().toString();
                setLastIpAddress(ipAdress);
                if (new IPAddressValidator().validate(ipAdress)) {
                    PreferencesUtil.storeIpAddress(getApplicationContext(), ipAdress);
                    alertDialog.dismiss();
                    tryToConnect(ipAdress);
                } else {
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

    private void tryToConnect(String ipAdress) {
        connect(ipAdress);
    }

    private void loadView(EActiveView activeView) {
        Fragment fragment = null;
        switch (activeView) {
            case HOME:
                break;
            case PHOTORESISTOR:
                PhotoresistorActivity photoresistorActivity = new PhotoresistorActivity();
                actionView = photoresistorActivity;
                fragment = photoresistorActivity;
                activeViewType = EActiveView.PHOTORESISTOR;
                break;
            case RGB_LED2:
                RgbLedActivity rgbLedActivity = new RgbLedActivity();
                actionView = rgbLedActivity;
                fragment = rgbLedActivity;
                activeViewType = EActiveView.RGB_LED2;
                break;
            case RGB_LED:
                RgbLedActivity rgbLed2Activity = new RgbLedActivity();
                actionView = rgbLed2Activity;
                fragment = rgbLed2Activity;
                activeViewType = EActiveView.RGB_LED;
                break;
            case BUZZER:
                BuzzerActivity buzzerActivity = new BuzzerActivity();
                actionView = buzzerActivity;
                fragment = buzzerActivity;
                activeViewType = EActiveView.BUZZER;
                break;
            case TEMPERATUR_SENSOR:
                TemperatureSensorActivity temperatureSensorActivity = new TemperatureSensorActivity();
                actionView = temperatureSensorActivity;
                fragment = temperatureSensorActivity;
                activeViewType = EActiveView.TEMPERATUR_SENSOR;
                break;
            case RELAY:
                RelayActivity relayActivity = new RelayActivity();
                actionView = relayActivity;
                fragment = relayActivity;
                activeViewType = EActiveView.RELAY;
                break;
            case MATRIX:
                MatrixActivity matrixActivity = new MatrixActivity();
                actionView = matrixActivity;
                fragment = matrixActivity;
                activeViewType = EActiveView.MATRIX;
                break;
            case SETTINGS:
                SettingsActivity settingsActivity = new SettingsActivity();
                actionView = settingsActivity;
                fragment = settingsActivity;
                activeViewType = EActiveView.SETTINGS;
                break;
            case CONSOLE:
                ConsoleActivity consoleActivity = new ConsoleActivity();
                actionView = consoleActivity;
                fragment = consoleActivity;
                activeViewType = EActiveView.CONSOLE;
                break;
            case IMPRINT:
                ImprintActivity imprintActivity = new ImprintActivity();
                actionView = imprintActivity;
                fragment = imprintActivity;
                activeViewType = EActiveView.IMPRINT;
                break;
            case HELP:
                HelpActivity helpActivity = new HelpActivity();
                actionView = helpActivity;
                fragment = helpActivity;
                activeViewType = EActiveView.HELP;
                break;
        }

        this.setTitle(getString(activeViewType.getTitleResId()));

        if (fragment != null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frameLayout, fragment);
            ft.commit();
        }

        invalidateOptionsMenu();
    }

    public void connect(String ipAddress) {
        ConnectionAsyncTask connectionAsyncTask = new ConnectionAsyncTask(ipAddress, EAction.CONNECT);
        connectionAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public String getLastIpAddress() {
        return lastIpAddress;
    }

    public void setLastIpAddress(String lastIpAddress) {
        this.lastIpAddress = lastIpAddress;
    }

    public void fireAction(EAction action) {
        switch (action) {
            case RELAY:
                fireRelayState();
                break;
            case MATRIX:
                fireMatrixState();
                break;
        }
    }

    private void fireMatrixState() {
        ConnectionAsyncTask connectionAsyncTask = new ConnectionAsyncTask(getLastIpAddress(), EAction.MATRIX);
        connectionAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void fireRelayState() {
        ConnectionAsyncTask connectionAsyncTask = new ConnectionAsyncTask(getLastIpAddress(), EAction.RELAY);
        connectionAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    class ConnectionAsyncTask extends AsyncTask<Void, Void, String> {

        private String ipAdress;
        private EAction action;
        private ProgressDialog progressDialog;

        public ConnectionAsyncTask(String ipAdress, EAction action) {
            this.ipAdress = ipAdress;
            this.action = action;
            activeAsyncTask = this;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String connectionErrorMsg;
            try {
                StringBuffer parameterBuffer = new StringBuffer();
                String urlStr = "http://" + ipAdress;
                if (actionView instanceof RgbLedActivity) {
                    RgbLedActivity rgbLedActivity = (RgbLedActivity) actionView;
                    urlStr += String.format(action.getActionSite(), rgbLedActivity.getRedValue(), rgbLedActivity.getGreenValue(), rgbLedActivity.getBlueValue());
                } else if (actionView instanceof BuzzerActivity) {
                    BuzzerActivity buzzerActivity = (BuzzerActivity) actionView;
                    urlStr += String.format(action.getActionSite(), buzzerActivity.getFrequenz(), buzzerActivity.getDuration());
                } else if (actionView instanceof RelayActivity) {
                    RelayActivity relayActivity = (RelayActivity) actionView;
                    urlStr += String.format(action.getActionSite(), relayActivity.getState());
                } else if (actionView instanceof TemperatureSensorActivity) {
                    urlStr += action.getActionSite();
                } else if (actionView instanceof MatrixActivity) {
                    MatrixActivity matrixActivity = (MatrixActivity) actionView;
                    urlStr += String.format(action.getActionSite(),
                            matrixActivity.getBrightness(),
                            matrixActivity.getCurrentRow(),
                            matrixActivity.getCurrentCol(),
                            matrixActivity.getState(),
                            matrixActivity.getClear()
                    );
                } else {
                    urlStr += action.getActionSite();
                }
                ConsoleUtil.addEntry(new ConsoleEntry(System.currentTimeMillis(), urlStr, true));
                URL url = new URL(urlStr);
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(parameterBuffer.toString());
                wr.flush();
                return readResult(conn, action);
            } catch (Exception e) {
                connectionErrorMsg = e.getMessage();
            }
            return connectionErrorMsg;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = getWaitDialog();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if (result.equals("Host unreachable")) {
                handleHostUnreachable();
                cancelTimer();
                return;
            } else {
                toggleESPShieldFunctions(true);
            }

            switch (action) {
                case CONNECT:
                    handleConnectionResult(result);
                    break;
                case PHOTORESISTOR:
                case TEMP:
                case MATRIX:
                    handleSensorResult(result);
                    break;
                case RGB_LED:
                case BUZZER:
                    break;
            }
        }

        private String readResult(URLConnection conn, EAction action) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String result = sb.toString();
            ConsoleUtil.addEntry(new ConsoleEntry(System.currentTimeMillis(), result, false));
            if (action.equals(EAction.CONNECT)) {
                showInfoMessage(result);
            }
            return result;
        }
    }

    private void handleSensorResult(String result) {
        actionView.addChartEntry(result);
    }

    private void handleConnectionResult(String result) {
        if (result != null) {
            if (!result.equals("Host unreachable") && !result.equals("Hello from ESP8266!")) {
                ConnectionResult connectionResult = new Gson().fromJson(result, ConnectionResult.class);
                if (!connectionResult.getMsg().equals("Hello from ESP8266!")) {
                    connectToDevice(result);
                }
            } else if (result.equals("Host unreachable")) {
                handleHostUnreachable();
            } else if (result.equals("Hello from ESP8266!")) {
                showInfoMessage(result);
            }
        }
    }

    public void handleHostUnreachable() {
        showErrorMessage("Host unreachable");
        if (navigationView != null) {
            toggleESPShieldFunctions(false);
        }

        cancelTimer();
        if (timerTask != null) {
            timerTask.cancel();
        }
    }

    private void toggleESPShieldFunctions(boolean active) {
        Menu menu = navigationView.getMenu();
        toggleMenuItem(menu, R.id.nav_esp01sdht11, active);
        toggleMenuItem(menu, R.id.nav_esp01srelais, active);
        toggleMenuItem(menu, R.id.nav_ldr_photoresitor, active);
        toggleMenuItem(menu, R.id.nav_rgb_led, active);
        toggleMenuItem(menu, R.id.nav_rgb_led2, active);
        toggleMenuItem(menu, R.id.nav_temp, active);
        toggleMenuItem(menu, R.id.nav_buzzer, active);
        toggleMenuItem(menu, R.id.nav_relay, active);
        toggleMenuItem(menu, R.id.nav_8matrix, active);
        navigationView.invalidate();
    }

    private void toggleMenuItem(Menu menu, int menuItemId, boolean active) {
        MenuItem item;
        item = menu.findItem(menuItemId);
        item.setEnabled(active);
    }

    private ProgressDialog getWaitDialog() {
        String titel = getResources().getString(R.string.msg_loaddialog_titel);
        String message = getResources().getString(R.string.msg_loaddialog_message);
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle(titel);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setButton(ProgressDialog.BUTTON_POSITIVE, getResources().getString(R.string.abort), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (activeAsyncTask != null) {
                    activeAsyncTask.cancel(true);
                }
            }
        });
        return progressDialog;
    }

    public void startTimer() {
        timer = new Timer();
        timer.schedule(getDefaultTimerTask(), 0, actionView.getUpdateInterval());
    }

    private TimerTask getDefaultTimerTask() {
        if (timerTask == null) {
            timerTask = createDefaultTimerTask();
        }
        return timerTask;
    }

    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }

        if (timerTask != null) {
            timerTask = null;
        }

        timerIsRunning = false;
        invalidateOptionsMenu();
    }

    public void showErrorMessage(String message) {
        showAlertDialog(R.string.errorMsg, R.drawable.ic_error_outline_rot_24dp, message);
    }

    private void showInfoMessage(String msg) {
        showAlertDialog(R.string.infoMsg, R.drawable.ic_info_outline_blue_24dp, msg);
    }

    private void showAlertDialog(int titleResId, int iconResId, String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setTitle(getString(titleResId));
        dialogBuilder.setIcon(iconResId);
        dialogBuilder.setMessage(msg);
        dialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}
