package wittyapp.draegerit.de.wittyapp.examples;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import wittyapp.draegerit.de.wittyapp.R;
import wittyapp.draegerit.de.wittyapp.frames.OnFragmentInteractionListener;
import wittyapp.draegerit.de.wittyapp.util.Configuration;
import wittyapp.draegerit.de.wittyapp.util.IActionView;
import wittyapp.draegerit.de.wittyapp.util.PreferencesUtil;

public class PhotoresistorActivity extends Fragment implements OnFragmentInteractionListener, IActionView {
    private static ArrayList<Entry> entries = new ArrayList<>();
    private static ArrayList<String> labels = new ArrayList<>();

    private int maxValue = 0;
    private int minValue = 1023;
    private String averageValue;

    private LineChart lineChart;
    private LineDataSet dataset;
    private LineData data;

    private static int counter = 0;

    private int updateIntervall;

    private NumberFormat numberFormat = new DecimalFormat("00.00");
    private static final String DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm:ss,SSS";
    private static DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_photoresistor, container, false);
        lineChart = view.findViewById(R.id.chart);

        dataset = new LineDataSet(entries, "#Value");
        dataset.setDrawCubic(true);
        dataset.setDrawFilled(true);

        data = new LineData(labels, dataset);
        lineChart.setData(data);
        lineChart.setDescription(getContext().getString(R.string.photoresistorchart_desc));
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
        entries.clear();
        labels.clear();
        data.clearValues();
        updateLineChart(labels);
    }

    public void addChartEntry(String entryValue) {
        int value = Integer.parseInt(entryValue);
        Entry entry = new Entry(value, ++counter);
        dataset.addEntry(entry);
        data.clearValues();
        data.addDataSet(dataset);
        updateLineChart(getLabels(counter));

        maxValue = Math.max(value, maxValue);
        minValue = Math.min(value, minValue);
        averageValue = calcAverageValue();

        TextView minValueTextView = getView().findViewById(R.id.minValueTextView);
        minValueTextView.setText(String.valueOf(minValue));

        TextView maxValueTextView = getView().findViewById(R.id.maxValueTextView);
        maxValueTextView.setText(String.valueOf(maxValue));

        TextView averageValueTextView = getView().findViewById(R.id.averageValueTextView);
        averageValueTextView.setText(String.valueOf(averageValue));
    }

    protected List<String> getLabels(int counter) {
        List<String> labels = new ArrayList<>();
        for (int i = 0; i <= counter; i++) {
            labels.add(String.valueOf(i));
        }
        return labels;
    }

    private String calcAverageValue() {
        float averageV = 0f;
        for (Entry entry : dataset.getYVals()) {
            averageV += entry.getVal();
        }
        if (averageV > 0)
            averageV = averageV / dataset.getYVals().size();
        return numberFormat.format(averageV);
    }

    private void updateLineChart(List<String> lbls) {
        data = new LineData(lbls, dataset);
        lineChart.setDescription(getContext().getString(R.string.photoresistorchart_desc));
        lineChart.setData(data);
        lineChart.animateY(500);
        lineChart.setDescription(getContext().getString(R.string.desc_lastupdate, formatDateTime(System.currentTimeMillis())));
    }

    @Override
    public void update() {

    }

    @Override
    public int getUpdateInterval() {
        return updateIntervall;
    }

    @Override
    public void setUpdateInterval(int intervall) {
this.updateIntervall = intervall;
    }

    protected String formatDateTime(long timestamp) {
        return dateFormat.format(new Date(timestamp));
    }
}
