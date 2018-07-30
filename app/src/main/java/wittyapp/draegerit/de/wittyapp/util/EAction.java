package wittyapp.draegerit.de.wittyapp.util;

public enum EAction {
    CONNECT("/greeting"),
    PHOTORESISTOR("/photoresistor"),
    RGB_LED("/rgbled?red=%d&green=%d&blue=%d"),
    TEMP(""),
    BUZZER("/buzzer?freq=%d&duration=%d"),
    SHT30("/sht30"),
    DS18B20("/ds18b20"),
    DHT11("/dht?type=dht11"),
    DHT22("/dht?type=dht22"),
    RELAY("/relay?state=%d");

    String actionSite;

    EAction(String actionSite) {
        this.actionSite = actionSite;
    }

    public String getActionSite() {
        return actionSite;
    }
}
