package wittyapp.draegerit.de.wittyapp.util;

import wittyapp.draegerit.de.wittyapp.R;

public enum EActiveView {
    PHOTORESISTOR(0, R.layout.activity_photoresistor),RGB_LED(1, R.layout.activity_rgb_led);

    int position;
    int layoutResId;

    EActiveView(int position, int layoutResId) {
        this.position = position;
        this.layoutResId = layoutResId;
    }

    public int getPosition() {
        return position;
    }

    public int getLayoutResId() {
        return layoutResId;
    }
}
