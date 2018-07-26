package wittyapp.draegerit.de.wittyapp.examples;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import wittyapp.draegerit.de.wittyapp.R;
import wittyapp.draegerit.de.wittyapp.util.AbstractView;
import wittyapp.draegerit.de.wittyapp.util.EActiveView;
import wittyapp.draegerit.de.wittyapp.util.PreferencesUtil;
import wittyapp.draegerit.de.wittyapp.util.RgbColor;

public class RGBLedView extends AbstractView implements SeekBar.OnSeekBarChangeListener {

    private SeekBar redSeekBar;
    private SeekBar greenSeekBar;
    private SeekBar blueSeekBar;

    private TextView previewTextView;

    public RGBLedView(Context context, View view) {
        super(context, view, EActiveView.RGB_LED);
    }

    @Override
    public void clearData() {

    }

    @Override
    public void initialize() {
        RgbColor rgbColor = PreferencesUtil.getRgbColor(getCtx());

        redSeekBar = getView().findViewById(R.id.redSeekBar);
        redSeekBar.setOnSeekBarChangeListener(this);
        greenSeekBar = getView().findViewById(R.id.greenSeekBar);
        greenSeekBar.setOnSeekBarChangeListener(this);
        blueSeekBar = getView().findViewById(R.id.blueSeekBar);
        blueSeekBar.setOnSeekBarChangeListener(this);

        previewTextView = getView().findViewById(R.id.previewTextView);

        redSeekBar.setProgress(rgbColor.getRed());
        greenSeekBar.setProgress(rgbColor.getGreen());
        blueSeekBar.setProgress(rgbColor.getBlue());

        updatePreview();
    }

    @Override
    public void update() {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        TextView valueTextView = null;
        if (seekBar.equals(redSeekBar)) {
            valueTextView = getView().findViewById(R.id.redValueTextView);
        } else if (seekBar.equals(greenSeekBar)) {
            valueTextView = getView().findViewById(R.id.greenValueTextView);
        } else if (seekBar.equals(blueSeekBar)) {
            valueTextView = getView().findViewById(R.id.blueValueTextView);
        }
        valueTextView.setText(String.valueOf(progress));
        updatePreview();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void updatePreview() {
        float red = redSeekBar.getProgress();
        float green = greenSeekBar.getProgress();
        float blue = blueSeekBar.getProgress();
        Color c = Color.valueOf(red, green, blue);
        String hex = String.format("#%02x%02x%02x", (int) red, (int) green, (int) blue);
        previewTextView.setBackgroundColor(Color.parseColor(hex));
        previewTextView.setText(hex);
    }

    public int getRedValue() {
        return redSeekBar.getProgress();
    }

    public int getGreenValue() {
        return greenSeekBar.getProgress();
    }

    public int getBlueValue() {
        return blueSeekBar.getProgress();
    }
}
