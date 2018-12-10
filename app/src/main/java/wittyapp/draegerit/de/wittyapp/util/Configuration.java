package wittyapp.draegerit.de.wittyapp.util;

public class Configuration {

    private boolean autoConnect;

    private String greeting = "Hello from ESP8266!";

    public boolean isAutoConnect() {
        return autoConnect;
    }

    public void setAutoConnect(boolean autoConnect) {
        this.autoConnect = autoConnect;
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }
}
