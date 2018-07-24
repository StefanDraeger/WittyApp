package wittyapp.draegerit.de.wittyapp.examples;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ThreadLocalRandom;

import wittyapp.draegerit.de.wittyapp.R;
import wittyapp.draegerit.de.wittyapp.examples.timertasks.PhotoresistorTimerTask;
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

    @Override
    public void updateTimer() {
        Timer timer = getTimer();
        timer.cancel();
        timer.purge();
        initTimer();
    }

    @Override
    public void initTimer() {
        Timer timer = new Timer();
        timer.schedule(new PhotoresistorTimerTask(), 0, getUpdateInterval());
        Log.d("WittyApp", "----------->" + String.valueOf(getUpdateInterval()));
        setTimer(timer);
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

        Button photoresistorBtnAdd = this.getView().findViewById(R.id.photoresistorBtnAdd);
        photoresistorBtnAdd.setOnClickListener(new View.OnClickListener() {

            int counter = 0;

            @Override
            public void onClick(View view) {
                int i = ThreadLocalRandom.current().nextInt(0, 10 + 1);

                Entry entry = new Entry(i, ++counter);
                dataset.addEntry(entry);
                data.clearValues();
                data.addDataSet(dataset);
                updateLineChart(getLabels(counter));

                maxValue = Math.max(i, maxValue);
                minValue = Math.min(i, minValue);
                averageValue = calcAverageValue();

                TextView minValueTextView = getView().findViewById(R.id.minValueTextView);
                minValueTextView.setText(String.valueOf(minValue));

                TextView maxValueTextView = getView().findViewById(R.id.maxValueTextView);
                maxValueTextView.setText(String.valueOf(maxValue));

                TextView averageValueTextView = getView().findViewById(R.id.averageValueTextView);
                averageValueTextView.setText(String.valueOf(averageValue));
            }

            private List<String> getLabels(int counter) {
                List<String> labels = new ArrayList<>();
                for (int i = 0; i < counter; i++) {
                    labels.add(String.valueOf(i));
                }
                return labels;
            }
        });
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
    }
}
