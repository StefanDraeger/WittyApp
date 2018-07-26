package wittyapp.draegerit.de.wittyapp.util.console;

public class ConsoleEntry {

    private long timestamp;
    private boolean isSendData;
    private String value;

    public ConsoleEntry(long timestamp, String value, boolean isSendData) {
        this.timestamp = timestamp;
        this.value = value;
        this.isSendData = isSendData;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSendData() {
        return isSendData;
    }

    public void setSendData(boolean sendData) {
        isSendData = sendData;
    }
}
