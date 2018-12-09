package wittyapp.draegerit.de.wittyapp;

import android.content.Context;
import android.widget.ViewSwitcher;

import wittyapp.draegerit.de.wittyapp.util.AbstractView;
import wittyapp.draegerit.de.wittyapp.util.EActiveView;

class HelpView extends AbstractView {

    public HelpView(Context applicationContext, ViewSwitcher viewSwitcher) {
        super(applicationContext, viewSwitcher, EActiveView.HELP);
    }

    @Override
    public void clearData() {

    }

    @Override
    public void initialize() {

    }

    @Override
    public void update() {

    }
}
