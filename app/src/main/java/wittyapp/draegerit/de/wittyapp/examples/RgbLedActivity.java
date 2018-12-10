package wittyapp.draegerit.de.wittyapp.examples;

import android.app.Fragment;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import wittyapp.draegerit.de.wittyapp.R;
import wittyapp.draegerit.de.wittyapp.frames.OnFragmentInteractionListener;
import wittyapp.draegerit.de.wittyapp.util.IActionView;
import wittyapp.draegerit.de.wittyapp.util.PreferencesUtil;
import wittyapp.draegerit.de.wittyapp.util.RgbColor;

public class RgbLedActivity extends Fragment implements OnFragmentInteractionListener, IActionView, SeekBar.OnSeekBarChangeListener {

    private SeekBar redSeekBar;
    private SeekBar greenSeekBar;
    private SeekBar blueSeekBar;

    private TextView previewTextView;

    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_rgb_led, container, false);

        RgbColor rgbColor = PreferencesUtil.getRgbColor(getContext());

        redSeekBar = view.findViewById(R.id.redSeekBar);
        redSeekBar.setOnSeekBarChangeListener(this);
        greenSeekBar = view.findViewById(R.id.greenSeekBar);
        greenSeekBar.setOnSeekBarChangeListener(this);
        blueSeekBar = view.findViewById(R.id.blueSeekBar);
        blueSeekBar.setOnSeekBarChangeListener(this);

        previewTextView = view.findViewById(R.id.previewTextView);

        redSeekBar.setProgress(rgbColor.getRed());
        greenSeekBar.setProgress(rgbColor.getGreen());
        blueSeekBar.setProgress(rgbColor.getBlue());

        updatePreview();
        return view;
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
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        TextView valueTextView = null;
        if (seekBar.equals(redSeekBar)) {
            valueTextView = view.findViewById(R.id.redValueTextView);
        } else if (seekBar.equals(greenSeekBar)) {
            valueTextView = view.findViewById(R.id.greenValueTextView);
        } else if (seekBar.equals(blueSeekBar)) {
            valueTextView = view.findViewById(R.id.blueValueTextView);
        }
        valueTextView.setText(String.valueOf(progress));
        updatePreview();
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

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

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
