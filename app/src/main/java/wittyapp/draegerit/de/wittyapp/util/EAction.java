package wittyapp.draegerit.de.wittyapp.util;

public enum EAction {
    CONNECT("/greeting"), PHOTORESISTOR("/photoresistor"), RGB_LED("/rgbled?red=%d&green=%d&blue=%d"), TEMP(""), BUZZER("/buzzer?freq=%d&duration=%d"), SHT30("/sht30"), DS18B20(""), DHT11(""), DHT22("");

    String actionSite;

    EAction(String actionSite) {
        this.actionSite = actionSite;
    }

    public String getActionSite() {
        return actionSite;
    }
}
