package wittyapp.draegerit.de.wittyapp;

import android.content.Context;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ViewSwitcher;

import wittyapp.draegerit.de.wittyapp.util.AbstractView;
import wittyapp.draegerit.de.wittyapp.util.Configuration;
import wittyapp.draegerit.de.wittyapp.util.EActiveView;
import wittyapp.draegerit.de.wittyapp.util.PreferencesUtil;

class SettingsView extends AbstractView {

    private CheckBox autoconnectChkBx;
    private EditText greetingEditText;

    public SettingsView(Context context, ViewSwitcher viewSwitcher) {
        super(context, viewSwitcher, EActiveView.SETTINGS);
    }

    @Override
    public void clearData() {
    }

    @Override
    public void initialize() {
        Configuration configuration = PreferencesUtil.getConfiguration(getCtx());
        autoconnectChkBx = this.getView().findViewById(R.id.autoconnectChkBx);
        autoconnectChkBx.setChecked(configuration.isAutoConnect());
        greetingEditText = this.getView().findViewById(R.id.greetingEditText);
        greetingEditText.setText(configuration.getGreeting());
    }

    @Override
    public void save() {
        Configuration configuration = PreferencesUtil.getConfiguration(getCtx());
        configuration.setAutoConnect(autoconnectChkBx.isChecked());
        configuration.setGreeting(greetingEditText.getText().toString());
        PreferencesUtil.storeConfiguration(getCtx(), configuration);
    }

    @Override
    public void update() {

    }

}
