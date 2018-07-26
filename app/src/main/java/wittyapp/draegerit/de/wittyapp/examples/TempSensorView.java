package wittyapp.draegerit.de.wittyapp.examples;

import android.content.Context;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.ViewSwitcher;

import wittyapp.draegerit.de.wittyapp.R;
import wittyapp.draegerit.de.wittyapp.examples.adapter.ETempSensor;
import wittyapp.draegerit.de.wittyapp.examples.adapter.EnumTempSpinnerArrayAdapter;
import wittyapp.draegerit.de.wittyapp.examples.adapter.TempSensorSpinnerAdapter;
import wittyapp.draegerit.de.wittyapp.util.AbstractView;
import wittyapp.draegerit.de.wittyapp.util.EActiveView;

public class TempSensorView extends AbstractView {
    public TempSensorView(Context ctx, ViewSwitcher viewSwitcher) {
        super(ctx, viewSwitcher, EActiveView.TEMPERATUR_SENSOR);
    }

    @Override
    public void clearData() {

    }

    @Override
    public void initialize() {
        Spinner tempSensorSpinner = getView().findViewById(R.id.tempSensorSpinner);

        EnumTempSpinnerArrayAdapter spinnerAdapter = new EnumTempSpinnerArrayAdapter(getCtx(), ETempSensor.values());
        tempSensorSpinner.setAdapter(spinnerAdapter);
    }

    @Override
    public void update() {

    }
}
