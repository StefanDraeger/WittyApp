package wittyapp.draegerit.de.wittyapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import wittyapp.draegerit.de.wittyapp.examples.BuzzerView;
import wittyapp.draegerit.de.wittyapp.examples.PhotoresistorView;
import wittyapp.draegerit.de.wittyapp.examples.RGBLedView;
import wittyapp.draegerit.de.wittyapp.examples.timertasks.AbstractTimerTask;
import wittyapp.draegerit.de.wittyapp.examples.timertasks.PhotoresistorTimerTask;
import wittyapp.draegerit.de.wittyapp.util.AbstractView;
import wittyapp.draegerit.de.wittyapp.util.EAction;
import wittyapp.draegerit.de.wittyapp.util.EActiveView;
import wittyapp.draegerit.de.wittyapp.util.IPAddressValidator;
import wittyapp.draegerit.de.wittyapp.util.PreferencesUtil;
import wittyapp.draegerit.de.wittyapp.util.RgbColor;
import wittyapp.draegerit.de.wittyapp.util.console.ConsoleEntry;
import wittyapp.draegerit.de.wittyapp.util.console.ConsoleUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String EMPTY = "";
    private static final int ZERO = 0;

    private String lastIpAddress;

    private ViewSwitcher viewSwitcher;
    private NavigationView navigationView;

    private AbstractView activeView;
    private Timer timer;

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

        activeView = new HomeView(getApplicationContext(), viewSwitcher);
        setLastIpAddress(PreferencesUtil.getIpAddress(getApplicationContext()));
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

        clearMItem.setVisible(false);
        downloadMItem.setVisible(false);
        timerMItem.setVisible(false);
        timeroffMItem.setVisible(false);
        refreshMItem.setVisible(false);
        checkMItem.setVisible(false);

        if (activeView.getActiveView().equals(EActiveView.PHOTORESISTOR)) {
            clearMItem.setVisible(true);
            downloadMItem.setVisible(true);
            timerMItem.setVisible(true);
            timeroffMItem.setVisible(true);
        } else if (activeView.getActiveView().equals(EActiveView.CONSOLE)) {
            clearMItem.setVisible(true);
            refreshMItem.setVisible(true);
        } else if (activeView.getActiveView().equals(EActiveView.RGB_LED) ||
                activeView.getActiveView().equals(EActiveView.BUZZER)) {
            checkMItem.setVisible(true);
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
            case R.id.action_refresh:
                activeView.update();
                break;
            case R.id.action_timeroff:
                cancelTimer();
                break;
            case R.id.action_check:
                sendToDevice();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendToDevice() {
        if (activeView instanceof RGBLedView) {
            RGBLedView rgbLedView = (RGBLedView) activeView;
            int red = rgbLedView.getRedValue();
            int green = rgbLedView.getGreenValue();
            int blue = rgbLedView.getBlueValue();
            PreferencesUtil.storeRgbColor(getApplicationContext(), new RgbColor(red, green, blue));
            ConnectionAsyncTask connectionAsyncTask = new ConnectionAsyncTask(getLastIpAddress(), EAction.RGB_LED);
            connectionAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else if (activeView instanceof BuzzerView) {
            BuzzerView buzzerView = (BuzzerView) activeView;
            ConnectionAsyncTask connectionAsyncTask = new ConnectionAsyncTask(getLastIpAddress(), EAction.BUZZER);
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
                cancelTimer();
                startTimer();
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
            case R.id.nav_connect:
                connectToDevice(null);
                break;
            case R.id.nav_disconnect:
                disconnectFromDevice();
                break;
            default:
                loadView(EActiveView.findByMenuItemId(id));
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

    private void connectToDevice(String connectionErrorMsg) {
        getIpAdressFromDialog(connectionErrorMsg);
        boolean connectionSuccessfull = true;
        toggleConnectionState(connectionSuccessfull);
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

        this.setTitle(getString(activeView.getTitleResId()));

        switch (activeView) {
            case HOME:
                break;
            case PHOTORESISTOR:
                this.activeView = new PhotoresistorView(getApplicationContext(), viewSwitcher);
                break;
            case RGB_LED:
                this.activeView = new RGBLedView(getApplicationContext(), viewSwitcher);
                break;
            case BUZZER:
                this.activeView = new BuzzerView(getApplicationContext(), viewSwitcher);
                break;
            case SETTINGS:
                break;
            case CONSOLE:
                this.activeView = new ConsoleView(getApplicationContext(), viewSwitcher);
                break;
            case IMPRINT:
                this.activeView = new ImprintView(getApplicationContext(), viewSwitcher);
                break;
        }

        invalidateOptionsMenu();
    }

    private void switchLayout(int layoutResId) {
        View old = viewSwitcher.getChildAt(1);
        viewSwitcher.removeView(old);
        LayoutInflater mInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(layoutResId, null, true);

        viewSwitcher.addView(view);
        viewSwitcher.setDisplayedChild(1);
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
                if (activeView instanceof RGBLedView) {
                    RGBLedView rgbLedView = (RGBLedView) activeView;
                    urlStr += String.format(action.getActionSite(), rgbLedView.getRedValue(), rgbLedView.getGreenValue(), rgbLedView.getBlueValue());
                } else if (activeView instanceof BuzzerView) {
                    BuzzerView buzzerView = (BuzzerView) activeView;
                    urlStr += String.format(action.getActionSite(), buzzerView.getFrequenz(), buzzerView.getDuration());
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
                return readResult(conn);
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
            switch (action) {
                case CONNECT:
                    handleConnectionResult(result);
                    break;
                case PHOTORESISTOR:
                    handlePhotoResistorResult(result);
                    break;
                case RGB_LED:
                    break;
                case TEMP:
                    break;
                case BUZZER:
                    break;
            }
        }

        private String readResult(URLConnection conn) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String result = sb.toString();
            ConsoleUtil.addEntry(new ConsoleEntry(System.currentTimeMillis(), result, false));
            return result;
        }
    }

    private void handlePhotoResistorResult(String result) {
        PhotoresistorView photoresistorView = (PhotoresistorView) activeView;
        photoresistorView.addChartEntry(Integer.parseInt(result));
    }

    private void handleConnectionResult(String result) {
        if (result != null) {
            connectToDevice(result);
        }
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
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                EAction action = null;
                switch (activeView.getActiveView()) {
                    case HOME:
                    case CONSOLE:
                    case SETTINGS:
                    case IMPRINT:
                    case PRIVACY_POLICY:
                        break;
                    case PHOTORESISTOR:
                        action = EAction.PHOTORESISTOR;
                        break;
                    case RGB_LED:
                        break;
                    case TEMPERATUR_SENSOR:
                        break;
                    case BUZZER:
                        break;
                }
                ConnectionAsyncTask connectionAsyncTask = new ConnectionAsyncTask(getLastIpAddress(), action);
                connectionAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }, 0, activeView.getUpdateInterval());
    }

    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }
}
