package wittyapp.draegerit.de.wittyapp.examples;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import wittyapp.draegerit.de.wittyapp.MainActivity;
import wittyapp.draegerit.de.wittyapp.R;
import wittyapp.draegerit.de.wittyapp.util.AbstractView;
import wittyapp.draegerit.de.wittyapp.util.EAction;
import wittyapp.draegerit.de.wittyapp.util.EActiveView;

public class RelayShieldView extends AbstractView {

    private int state = 0;

    public RelayShieldView(Context ctx, ViewSwitcher view) {
        super(ctx, view, EActiveView.RELAY);
    }

    @Override
    public void clearData() {

    }

    @Override
    public void initialize() {
        final ImageView onoffImageView = getView().findViewById(R.id.onoffImageView);

        if (state == 0) {
            onoffImageView.setImageDrawable(getCtx().getDrawable(R.drawable.ic_lock_green));
        } else {
            onoffImageView.setImageDrawable(getCtx().getDrawable(R.drawable.ic_lock_red));
        }

        onoffImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == 0) {
                    onoffImageView.setImageDrawable(getCtx().getDrawable(R.drawable.ic_lock_red));
                    state = 1;
                } else {
                    onoffImageView.setImageDrawable(getCtx().getDrawable(R.drawable.ic_lock_green));
                    state = 0;
                }

                MainActivity mainActivity = (MainActivity) getView().getContext();
                mainActivity.fireAction(EAction.RELAY);
            }
        });
    }

    public int getState() {
        return state;
    }

    @Override
    public void update() {

    }
}
