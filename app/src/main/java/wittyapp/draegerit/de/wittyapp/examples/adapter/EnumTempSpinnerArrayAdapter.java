package wittyapp.draegerit.de.wittyapp.examples.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import wittyapp.draegerit.de.wittyapp.R;

public class EnumTempSpinnerArrayAdapter extends ArrayAdapter<ETempSensor> {

    public EnumTempSpinnerArrayAdapter(Context context, ETempSensor[] values) {
        super(context, -1, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.tempspinner_listitem, parent, false);
        setEnumValueText(position, view);
        return view;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = getCustomView(parent);
        setEnumValueText(position, view);
        return view;
    }

    private View getCustomView(ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.tempspinner_listitem, parent, false);
        return view;
    }

    private void setEnumValueText(int position, View view) {
        ETempSensor value = getItem(position);

        ImageView tempSensorImageView = view.findViewById(R.id.tempSensorImageView);
        tempSensorImageView.setImageDrawable(getContext().getDrawable(value.getDrawableResId()));

        TextView lblTextView = view.findViewById(R.id.tempLblTextView);
        lblTextView.setText(getContext().getString(value.getLblResId()));
    }
}
