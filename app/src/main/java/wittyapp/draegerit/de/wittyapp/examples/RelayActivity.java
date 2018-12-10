package wittyapp.draegerit.de.wittyapp.examples;

import android.app.Fragment;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import wittyapp.draegerit.de.wittyapp.MainActivity;
import wittyapp.draegerit.de.wittyapp.R;
import wittyapp.draegerit.de.wittyapp.frames.OnFragmentInteractionListener;
import wittyapp.draegerit.de.wittyapp.util.EAction;
import wittyapp.draegerit.de.wittyapp.util.IActionView;

public class RelayActivity extends Fragment implements OnFragmentInteractionListener, IActionView {

    private int state = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_relay, container, false);
        final ImageView onoffImageView = getView().findViewById(R.id.onoffImageView);

        if (state == 0) {
            onoffImageView.setImageDrawable(getContext().getDrawable(R.drawable.ic_lock_green));
        } else {
            onoffImageView.setImageDrawable(getContext().getDrawable(R.drawable.ic_lock_red));
        }

        onoffImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == 0) {
                    onoffImageView.setImageDrawable(getContext().getDrawable(R.drawable.ic_lock_red));
                    state = 1;
                } else {
                    onoffImageView.setImageDrawable(getContext().getDrawable(R.drawable.ic_lock_green));
                    state = 0;
                }

                MainActivity mainActivity = (MainActivity) getView().getContext();
                mainActivity.fireAction(EAction.RELAY);
            }
        });
        return view;
    }

    public int getState() {
        return state;
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
