package wittyapp.draegerit.de.wittyapp.examples;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import wittyapp.draegerit.de.wittyapp.R;
import wittyapp.draegerit.de.wittyapp.util.AbstractView;
import wittyapp.draegerit.de.wittyapp.util.EActiveView;

public class PhotoresistorView extends AbstractView {

    private static ArrayList<Entry> entries = new ArrayList<>();
    private static ArrayList<String> labels = new ArrayList<>();

    private int maxValue = 0;
    private int minValue = 1023;
    private float averageValue;

    private LineChart lineChart;
    private LineDataSet dataset;
    private LineData data;

    private static int counter = 0;

    public PhotoresistorView(Context ctx, View view) {
        super(ctx, view, EActiveView.PHOTORESISTOR);
    }

    @Override
    public void clearData() {
        entries.clear();
        labels.clear();
        data.clearValues();
        updateLineChart(labels);
    }

    public void addChartEntry(int value) {
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

    @Override
    public void initialize() {
        lineChart = this.getView().findViewById(R.id.chart);

        dataset = new LineDataSet(entries, "#Test");
        dataset.setDrawCubic(true);
        dataset.setDrawFilled(true);

        data = new LineData(labels, dataset);
        lineChart.setData(data);
        lineChart.setDescription(this.getCtx().getString(R.string.photoresistorchart_desc));
    }

    @Override
    public void update() {

    }

    private float calcAverageValue() {
        float averageV = 0f;
        for (Entry entry : dataset.getYVals()) {
            averageV += entry.getVal();
        }
        if (averageV > 0)
            averageV = averageV / dataset.getYVals().size();
        return averageV;
    }

    private void updateLineChart(List<String> lbls) {
        data = new LineData(lbls, dataset);
        lineChart.setDescription(getCtx().getString(R.string.photoresistorchart_desc));
        lineChart.setData(data);
        lineChart.animateY(500);
        lineChart.setDescription(getCtx().getString(R.string.desc_lastupdate, formatDateTime(System.currentTimeMillis())));
    }

}
