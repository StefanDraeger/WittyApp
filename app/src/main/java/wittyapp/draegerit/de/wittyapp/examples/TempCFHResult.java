package wittyapp.draegerit.de.wittyapp.examples;

public class TempCFHResult {

    private float tempC;
    private float tempF;
    private float humi;

    public TempCFHResult(float tempC, float tempF, float humi) {
        this.tempC = tempC;
        this.tempF = tempF;
        this.humi = humi;
    }

    public float getHumi() {
        return humi;
    }

    public void setHumi(float humi) {
        this.humi = humi;
    }

    public float getTempF() {
        return tempF;
    }

    public void setTempF(float tempF) {
        this.tempF = tempF;
    }

    public float getTempC() {
        return tempC;
    }

    public void setTempC(float tempC) {
        this.tempC = tempC;
    }
}
