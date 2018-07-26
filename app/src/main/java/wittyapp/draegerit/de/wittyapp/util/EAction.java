package wittyapp.draegerit.de.wittyapp.util;

public enum EAction {
    CONNECT("/greeting"), PHOTORESISTOR("/photoresistor"), RGB_LED("/rgbled?red=%d&green=%d&blue=%d"), TEMP(""), BUZZER("/buzzer?freq=%d&duration=%d");

    String actionSite;

    EAction(String actionSite) {
        this.actionSite = actionSite;
    }

    public String getActionSite() {
        return actionSite;
    }
}
