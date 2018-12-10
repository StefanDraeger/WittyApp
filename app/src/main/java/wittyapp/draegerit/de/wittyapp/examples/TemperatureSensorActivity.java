package wittyapp.draegerit.de.wittyapp.examples;

import android.app.Fragment;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import wittyapp.draegerit.de.wittyapp.MainActivity;
import wittyapp.draegerit.de.wittyapp.R;
import wittyapp.draegerit.de.wittyapp.examples.adapter.ETempSensor;
import wittyapp.draegerit.de.wittyapp.examples.adapter.EnumTempSpinnerArrayAdapter;
import wittyapp.draegerit.de.wittyapp.frames.OnFragmentInteractionListener;
import wittyapp.draegerit.de.wittyapp.util.EAction;
import wittyapp.draegerit.de.wittyapp.util.IActionView;

public class TemperatureSensorActivity extends Fragment implements OnFragmentInteractionListener, IActionView {

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

    private int updateIntervall;

    private static final String DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm:ss,SSS";
    private static DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_temperature_sensor, container, false);
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

        EnumTempSpinnerArrayAdapter spinnerAdapter = new EnumTempSpinnerArrayAdapter(getContext(), ETempSensor.values());
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
        lineChart.setDescription(getContext().getString(R.string.photoresistorchart_desc));

        tempCMinValueTextView = getView().findViewById(R.id.tempCMinValueTextView);
        tempCMaxValueTextView = getView().findViewById(R.id.tempCMaxValueTextView);
        tempCAvageValueTextView = getView().findViewById(R.id.tempCAvageValueTextView);
        tempFMinValueTextView = getView().findViewById(R.id.tempFMinValueTextView);
        tempFMaxValueTextView = getView().findViewById(R.id.tempFMaxValueTextView);
        tempFAvageValueTextView = getView().findViewById(R.id.tempFAvageValueTextView);
        humMinValueTextView = getView().findViewById(R.id.humMinValueTextView);
        humMaxValueTextView = getView().findViewById(R.id.humMaxValueTextView);
        humAvageValueTextView = getView().findViewById(R.id.humAvageValueTextView);
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

    protected List<String> getLabels(int counter) {
        List<String> labels = new ArrayList<>();
        for (int i = 0; i <= counter; i++) {
            labels.add(String.valueOf(i));
        }
        return labels;
    }

    @Override
    public void update() {

    }

    private void updateLineChart(List<String> lbls) {
        try {
            data = new LineData(lbls, lineDataSets);
            lineChart.setDescription(getContext().getString(R.string.photoresistorchart_desc));
            lineChart.setData(data);
            lineChart.animateY(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int getUpdateInterval() {
        return updateIntervall;
    }

    @Override
    public void setUpdateInterval(int intervall) {
        this.updateIntervall = intervall;
    }

    @Override
    public void addChartEntry(String entryValue) {
        if (this.action.equals(EAction.SHT30) ||
                this.action.equals(EAction.DHT11) ||
                this.action.equals(EAction.DHT22)) {
            TempCFHResult sht30Result = new Gson().fromJson(entryValue, TempCFHResult.class);
            if (sht30Result.getMsg().isEmpty()) {
                ++counter;
                this.tempCDataset.addEntry(new Entry(sht30Result.getTempC(), counter));
                this.tempFDataset.addEntry(new Entry(sht30Result.getTempF(), counter));
                this.humiDataset.addEntry(new Entry(sht30Result.getHumi(), counter));
                updateLineChart(getLabels(counter));
                calcTempHumData(sht30Result.getTempC(), sht30Result.getTempF(), sht30Result.getHumi());
                lineChart.setDescription(getContext().getString(R.string.desc_lastupdate, formatDateTime(System.currentTimeMillis())));
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

        tempCMinValueTextView.setText(getContext().getString(R.string.tempC_val, tempCMin));
        tempCMaxValueTextView.setText(getContext().getString(R.string.tempC_val, tempCMax));
        tempCAvageValueTextView.setText(getContext().getString(R.string.tempC_val, tempCAverage));

        tempFMinValueTextView.setText(getContext().getString(R.string.tempF_val, tempFMin));
        tempFMaxValueTextView.setText(getContext().getString(R.string.tempF_val, tempFMax));
        tempFAvageValueTextView.setText(getContext().getString(R.string.tempF_val, tempFAverage));

        humMinValueTextView.setText(getContext().getString(R.string.humi_val, humiMin));
        humMaxValueTextView.setText(getContext().getString(R.string.humi_val, humiMax));
        humAvageValueTextView.setText(getContext().getString(R.string.humi_val, humiAverage));
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

    protected String formatDateTime(long timestamp) {
        return dateFormat.format(new Date(timestamp));
    }
}
