package wittyapp.draegerit.de.wittyapp.frames;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import wittyapp.draegerit.de.wittyapp.R;
import wittyapp.draegerit.de.wittyapp.util.Configuration;
import wittyapp.draegerit.de.wittyapp.util.IActionView;
import wittyapp.draegerit.de.wittyapp.util.PreferencesUtil;

public class SettingsActivity extends Fragment implements OnFragmentInteractionListener, IActionView {

    private OnFragmentInteractionListener mListener;

    private CheckBox autoconnectChkBx;
    private EditText greetingEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_settings, container, false);
        Configuration configuration = PreferencesUtil.getConfiguration(getContext());
        autoconnectChkBx = view.findViewById(R.id.autoconnectChkBx);
        autoconnectChkBx.setChecked(configuration.isAutoConnect());
        greetingEditText = view.findViewById(R.id.greetingEditText);
        greetingEditText.setText(configuration.getGreeting());
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void save() {
        Configuration configuration = PreferencesUtil.getConfiguration(getContext());
        configuration.setAutoConnect(autoconnectChkBx.isChecked());
        configuration.setGreeting(greetingEditText.getText().toString());
        PreferencesUtil.storeConfiguration(getContext(), configuration);
    }

    @Override
    public void clearData() {

    }

    @Override
    public void update() {

    }

    @Override
    public int getUpdateInterval() {
        return 0;
    }

    @Override
    public void setUpdateInterval(int intervall) {

    }

    @Override
    public void addChartEntry(String entryValue) {

    }
}
