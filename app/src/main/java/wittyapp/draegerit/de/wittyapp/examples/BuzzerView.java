package wittyapp.draegerit.de.wittyapp.examples;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

import java.util.prefs.PreferencesFactory;

import wittyapp.draegerit.de.wittyapp.R;
import wittyapp.draegerit.de.wittyapp.util.AbstractView;
import wittyapp.draegerit.de.wittyapp.util.BuzzerValue;
import wittyapp.draegerit.de.wittyapp.util.EActiveView;
import wittyapp.draegerit.de.wittyapp.util.PreferencesUtil;

public class BuzzerView extends AbstractView implements View.OnKeyListener {

    private int frequenz;
    private int duration;

    private EditText frequenzEditText;
    private SeekBar frequenzSeekBar;

    private EditText durationEditText;
    private SeekBar durationSeekBar;

    public BuzzerView(Context context, View view) {
        super(context, view, EActiveView.BUZZER);
    }

    @Override
    public void clearData() {

    }

    @Override
    public void initialize() {
        BuzzerValue buzzerValue = PreferencesUtil.getBuzzerValue(getCtx());
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
    }

    @Override
    public void update() {

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
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        updateValues(v);
        return false;
    }
}
