package wittyapp.draegerit.de.wittyapp.examples;

public class DS18b20Result {

    private float tempC;

    public DS18b20Result(float tempC) {
        this.tempC = tempC;
    }


    public float getTempC() {
        return tempC;
    }

    public void setTempC(float tempC) {
        this.tempC = tempC;
    }
}
