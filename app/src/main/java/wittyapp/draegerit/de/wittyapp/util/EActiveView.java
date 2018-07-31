package wittyapp.draegerit.de.wittyapp.util;

import wittyapp.draegerit.de.wittyapp.R;

public enum EActiveView {
    HOME(0, R.id.nav_home, R.layout.activity_home, R.string.home_title),
    PHOTORESISTOR(1, R.id.nav_ldr_photoresitor, R.layout.activity_photoresistor, R.string.photoresistor_title),
    RGB_LED(1, R.id.nav_rgb_led, R.layout.activity_rgb_led, R.string.rgbled_title),
    RGB_LED2(1, R.id.nav_rgb_led2, R.layout.activity_rgb_led, R.string.rgbled_title),
    TEMPERATUR_SENSOR(1,R.id.nav_temp,R.layout.activity_temperature_sensor,R.string.temperature_sensor_title),
    BUZZER(1,R.id.nav_buzzer,R.layout.activity_buzzer,R.string.buzzer_title),
    RELAY(1,R.id.nav_relay,R.layout.activity_relay,R.string.relay_title),
    MATRIX(1,R.id.nav_8matrix,R.layout.activity_matrix,R.string.matrix_title),
    CONSOLE(1,R.id.nav_console,R.layout.activity_console,R.string.console_title),
    SETTINGS(1, R.id.nav_settings, R.layout.activity_settings, R.string.settings_title),
    IMPRINT(1, R.id.nav_impressum, R.layout.activity_imprint, R.string.imprint_title);

    int position;
    int menuItemId;
    int layoutResId;
    int titleResId;

    EActiveView(int position, int menuItemId, int layoutResId, int titleResId) {
        this.menuItemId = menuItemId;
        this.position = position;
        this.layoutResId = layoutResId;
        this.titleResId = titleResId;
    }

    public int getPosition() {
        return position;
    }

    public int getLayoutResId() {
        return layoutResId;
    }

    public int getTitleResId() {
        return titleResId;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public static EActiveView findByMenuItemId(int menuItemId) {
        for (EActiveView activeView : values()) {
            if (activeView.getMenuItemId() == menuItemId) {
                return activeView;
            }
        }
        return null;
    }
}
