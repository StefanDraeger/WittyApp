package wittyapp.draegerit.de.wittyapp;

import android.app.Fragment;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import wittyapp.draegerit.de.wittyapp.frames.OnFragmentInteractionListener;
import wittyapp.draegerit.de.wittyapp.util.IActionView;

public class ImprintActivity extends Fragment implements OnFragmentInteractionListener, IActionView {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_imprint, container, false);
        TextView versionTextView = view.findViewById(R.id.versionTextView);
        versionTextView.setText(getContext().getString(R.string.version_text, BuildConfig.VERSION_NAME,BuildConfig.VERSION_CODE));
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
}
