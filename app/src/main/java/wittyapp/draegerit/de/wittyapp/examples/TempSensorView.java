package wittyapp.draegerit.de.wittyapp.examples;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import wittyapp.draegerit.de.wittyapp.MainActivity;
import wittyapp.draegerit.de.wittyapp.R;
import wittyapp.draegerit.de.wittyapp.examples.adapter.ETempSensor;
import wittyapp.draegerit.de.wittyapp.examples.adapter.EnumTempSpinnerArrayAdapter;
import wittyapp.draegerit.de.wittyapp.util.AbstractView;
import wittyapp.draegerit.de.wittyapp.util.EAction;
import wittyapp.draegerit.de.wittyapp.util.EActiveView;

public class TempSensorView extends AbstractView {

    private ArrayList<Entry> tempCEntries = new ArrayList<>();
    private ArrayList<Entry> tempFEntries = new ArrayList<>();
    private ArrayList<Entry> humiEntries = new ArrayList<>();

    private LineChart lineChart;
    private List<LineDataSet> lineDataSets;
    private LineDataSet tempCDataset;
    private LineDataSet tempFDataset;
    private LineDataSet humiDataset;
    private LineData data;

    private EAction action = EAction.SHT30;

    private int counter = -1;

    private float tempCMin = 1000f;
    private float tempCMax = 0f;
    private float tempCAverage = 0f;

    private float tempFMin = 1000f;
    private float tempFMax = 0f;
    private float tempFAverage = 0f;

    private float humiMin = 1000f;
    private float humiMax = 0f;
    private float humiAverage = 0f;

    private TextView tempCMinValueTextView;
    private TextView tempCMaxValueTextView;
    private TextView tempCAvageValueTextView;
    private TextView tempFMinValueTextView;
    private TextView tempFMaxValueTextView;
    private TextView tempFAvageValueTextView;
    private TextView humMinValueTextView;
    private TextView humMaxValueTextView;
    private TextView humAvageValueTextView;

    public TempSensorView(Context ctx, ViewSwitcher viewSwitcher) {
        super(ctx, viewSwitcher, EActiveView.TEMPERATUR_SENSOR);
    }

    @Override
    public void clearData() {
        counter = -1;

        tempCMin = 1000f;
        tempCMax = 0f;
        tempCAverage = 0f;

        tempFMin = 1000f;
        tempFMax = 0f;
        tempFAverage = 0f;

        humiMin = 1000f;
        humiMax = 0f;
        humiAverage = 0f;

        for (DataSet<Entry> ds : lineDataSets) {
            ds.clear();
        }

        updateLineChart(getLabels(counter));
    }

    private void updateLineChart(List<String> lbls) {
        try {
            data = new LineData(lbls, lineDataSets);
            lineChart.setDescription(getCtx().getString(R.string.photoresistorchart_desc));
            lineChart.setData(data);
            lineChart.animateY(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize() {
        Spinner tempSensorSpinner = getView().findViewById(R.id.tempSensorSpinner);
        tempSensorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int tempCRowVis = View.GONE;
                int tempFRowVis = View.GONE;
                int humiRowVis = View.GONE;

                switch (position) {
                    case 0:
                        action = EAction.SHT30;
                        break;
                    case 1:
                        action = EAction.DS18B20;
                        break;
                    case 2:
                        action = EAction.DHT11;
                        break;
                    case 3:
                        action = EAction.DHT22;
                        break;
                }

                if (action.equals(EAction.SHT30) || action.equals(EAction.DHT11) || action.equals(EAction.DHT22)) {
                    tempCRowVis = View.VISIBLE;
                    tempFRowVis = View.VISIBLE;
                    humiRowVis = View.VISIBLE;
                } else if (action.equals(EAction.DS18B20)) {
                    tempCRowVis = View.VISIBLE;
                }

                TableRow tempCRow = getView().findViewById(R.id.tempCRow);
                TableRow tempFRow = getView().findViewById(R.id.tempFRow);
                TableRow humiRow = getView().findViewById(R.id.humiRow);

                tempCRow.setVisibility(tempCRowVis);
                tempFRow.setVisibility(tempFRowVis);
                humiRow.setVisibility(humiRowVis);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        EnumTempSpinnerArrayAdapter spinnerAdapter = new EnumTempSpinnerArrayAdapter(getCtx(), ETempSensor.values());
        tempSensorSpinner.setAdapter(spinnerAdapter);

        lineChart = this.getView().findViewById(R.id.tempChart);

        tempCDataset = new LineDataSet(tempCEntries, "#Temp. °C");
        tempCDataset.setDrawCubic(true);
        tempCDataset.setColor(Color.BLUE);
        tempCDataset.setCircleColor(Color.BLUE);

        tempFDataset = new LineDataSet(tempFEntries, "#Temp. °F");
        tempFDataset.setDrawCubic(true);
        tempFDataset.setColor(Color.GREEN);
        tempFDataset.setCircleColor(Color.GREEN);


        humiDataset = new LineDataSet(humiEntries, "#Humidity");
        humiDataset.setDrawCubic(true);
        int orangeColor = Color.valueOf(255, 165, 0).toArgb();
        humiDataset.setColor(orangeColor);
        humiDataset.setCircleColor(orangeColor);

        lineDataSets = new ArrayList<>();
        lineDataSets.add(tempCDataset);
        lineDataSets.add(tempFDataset);
        lineDataSets.add(humiDataset);

        data = new LineData(new ArrayList<String>(), lineDataSets);
        lineChart.setData(data);
        lineChart.setDescription(this.getCtx().getString(R.string.photoresistorchart_desc));

        tempCMinValueTextView = getView().findViewById(R.id.tempCMinValueTextView);
        tempCMaxValueTextView = getView().findViewById(R.id.tempCMaxValueTextView);
        tempCAvageValueTextView = getView().findViewById(R.id.tempCAvageValueTextView);
        tempFMinValueTextView = getView().findViewById(R.id.tempFMinValueTextView);
        tempFMaxValueTextView = getView().findViewById(R.id.tempFMaxValueTextView);
        tempFAvageValueTextView = getView().findViewById(R.id.tempFAvageValueTextView);
        humMinValueTextView = getView().findViewById(R.id.humMinValueTextView);
        humMaxValueTextView = getView().findViewById(R.id.humMaxValueTextView);
        humAvageValueTextView = getView().findViewById(R.id.humAvageValueTextView);
    }

    @Override
    public void update() {

    }

    public void addChartEntry(String result) {
        if (this.action.equals(EAction.SHT30) ||
                this.action.equals(EAction.DHT11) ||
                this.action.equals(EAction.DHT22)) {
            TempCFHResult sht30Result = new Gson().fromJson(result, TempCFHResult.class);
            if (sht30Result.getMsg().isEmpty()) {
                ++counter;
                this.tempCDataset.addEntry(new Entry(sht30Result.getTempC(), counter));
                this.tempFDataset.addEntry(new Entry(sht30Result.getTempF(), counter));
                this.humiDataset.addEntry(new Entry(sht30Result.getHumi(), counter));
                updateLineChart(getLabels(counter));
                calcTempHumData(sht30Result.getTempC(), sht30Result.getTempF(), sht30Result.getHumi());
                lineChart.setDescription(getCtx().getString(R.string.desc_lastupdate, formatDateTime(System.currentTimeMillis())));
            } else {
                MainActivity mainActivity = (MainActivity) getView().getContext();
                mainActivity.cancelTimer();
                mainActivity.showErrorMessage(sht30Result.getMsg());
            }
        }
    }

    private void calcTempHumData(float tempC, float tempF, float humi) {
        tempCMax = Math.max(tempC, tempCMax);
        tempCMin = Math.min(tempC, tempCMin);
        tempCAverage = calcAverageValue(0);

        tempFMax = Math.max(tempF, tempFMax);
        tempFMin = Math.min(tempF, tempFMin);
        tempFAverage = calcAverageValue(1);

        humiMax = Math.max(humi, humiMax);
        humiMin = Math.min(humi, humiMin);
        humiAverage = calcAverageValue(2);

        tempCMinValueTextView.setText(getCtx().getString(R.string.tempC_val, tempCMin));
        tempCMaxValueTextView.setText(getCtx().getString(R.string.tempC_val, tempCMax));
        tempCAvageValueTextView.setText(getCtx().getString(R.string.tempC_val, tempCAverage));

        tempFMinValueTextView.setText(getCtx().getString(R.string.tempF_val, tempFMin));
        tempFMaxValueTextView.setText(getCtx().getString(R.string.tempF_val, tempFMax));
        tempFAvageValueTextView.setText(getCtx().getString(R.string.tempF_val, tempFAverage));

        humMinValueTextView.setText(getCtx().getString(R.string.humi_val, humiMin));
        humMaxValueTextView.setText(getCtx().getString(R.string.humi_val, humiMax));
        humAvageValueTextView.setText(getCtx().getString(R.string.humi_val, humiAverage));
    }

    private float calcAverageValue(int pos) {
        float averageV = 0f;
        DataSet<Entry> dataSet = lineDataSets.get(pos);
        for (Entry entry : dataSet.getYVals()) {
            averageV += entry.getVal();
        }
        if (averageV > 0) {
            averageV = averageV / dataSet.getYVals().size();
        }
        return averageV;
    }

    public EAction getAction() {
        return action;
    }

}
