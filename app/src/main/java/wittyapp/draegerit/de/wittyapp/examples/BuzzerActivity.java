package wittyapp.draegerit.de.wittyapp.examples;

import android.app.Fragment;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;

import wittyapp.draegerit.de.wittyapp.R;
import wittyapp.draegerit.de.wittyapp.frames.OnFragmentInteractionListener;
import wittyapp.draegerit.de.wittyapp.util.BuzzerValue;
import wittyapp.draegerit.de.wittyapp.util.IActionView;
import wittyapp.draegerit.de.wittyapp.util.PreferencesUtil;

public class BuzzerActivity extends Fragment implements OnFragmentInteractionListener, IActionView, View.OnKeyListener {

    private int frequenz;
    private int duration;

    private EditText frequenzEditText;
    private SeekBar frequenzSeekBar;

    private EditText durationEditText;
    private SeekBar durationSeekBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_buzzer, container, false);
        BuzzerValue buzzerValue = PreferencesUtil.getBuzzerValue(getContext());
        frequenzEditText = getView().findViewById(R.id.frequenzEditText);
        frequenzEditText.setText(String.valueOf(buzzerValue.getFrequenz()));
        frequenzEditText.setOnKeyListener(this);

        frequenzSeekBar = getView().findViewById(R.id.frequenzSeekBar);
        frequenzSeekBar.setProgress(buzzerValue.getFrequenz());
        frequenzSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateValues(seekBar);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        durationEditText = getView().findViewById(R.id.durationEditText);
        durationEditText.setOnKeyListener(this);
        durationEditText.setText(String.valueOf(buzzerValue.getDuration()));

        durationSeekBar = getView().findViewById(R.id.durationSeekBar);
        durationSeekBar.setProgress(buzzerValue.getDuration());
        durationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateValues(seekBar);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return view;
    }

    private void updateValues(View v) {
        if (v.equals(this.frequenzEditText)) {
            String val = frequenzEditText.getText().toString();
            if (val.equalsIgnoreCase("")) {
                val = "0";
            }
            frequenz = Integer.parseInt(val);
            this.frequenzSeekBar.setProgress(frequenz);
        } else if (v.equals(this.durationEditText)) {
            String val = durationEditText.getText().toString();
            if (val.equalsIgnoreCase("")) {
                val = "0";
            }
            duration = Integer.parseInt(val);
            this.durationSeekBar.setProgress(duration);
        } else if (v.equals(this.frequenzSeekBar)) {
            frequenz = this.frequenzSeekBar.getProgress();
            frequenzEditText.setText(String.valueOf(frequenz));
        } else if (v.equals(this.durationSeekBar)) {
            duration = this.durationSeekBar.getProgress();
            durationEditText.setText(String.valueOf(duration));
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void save() {

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

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        updateValues(v);
        return false;
    }


    public int getFrequenz() {
        return frequenz;
    }

    public void setFrequenz(int frequenz) {
        this.frequenz = frequenz;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}
