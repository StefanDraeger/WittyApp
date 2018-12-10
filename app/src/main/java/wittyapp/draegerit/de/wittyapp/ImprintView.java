package wittyapp.draegerit.de.wittyapp;

import android.content.Context;
import android.os.Build;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import wittyapp.draegerit.de.wittyapp.util.AbstractView;
import wittyapp.draegerit.de.wittyapp.util.EActiveView;

class ImprintView extends AbstractView {
    public ImprintView(Context ctx, ViewSwitcher view) {
        super(ctx, view, EActiveView.IMPRINT);
    }

    @Override
    public void clearData() {

    }

    @Override
    public void initialize() {
       TextView versionTextView = getView().findViewById(R.id.versionTextView);
       versionTextView.setText(getCtx().getString(R.string.version_text, BuildConfig.VERSION_NAME,BuildConfig.VERSION_CODE));
    }

    @Override
    public void update() {

    }

    @Override
    public void save() {

    }
}
