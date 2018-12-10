package wittyapp.draegerit.de.wittyapp.examples.adapter;

import wittyapp.draegerit.de.wittyapp.R;
import wittyapp.draegerit.de.wittyapp.util.EAction;

public enum ETempSensor {
    SHT30(R.string.sht30, R.drawable.sht30, EAction.SHT30),
    DS18B20(R.string.ds18b20, R.drawable.ds18b20, EAction.DS18B20),
    DHT11(R.string.dht11, R.drawable.dht11, EAction.DHT11),
    DHT22(R.string.dht22, R.drawable.dht22, EAction.DHT22);

    private int lblResId;
    private int drawableResId;
    private EAction action;

    ETempSensor(int lblResId, int drawableResId, EAction action) {
        this.lblResId = lblResId;
        this.drawableResId = drawableResId;
        this.action = action;
    }

    public int getLblResId() {
        return lblResId;
    }

    public void setLblResId(int lblResId) {
        this.lblResId = lblResId;
    }

    public int getDrawableResId() {
        return drawableResId;
    }

    public void setDrawableResId(int drawableResId) {
        this.drawableResId = drawableResId;
    }

    public EAction getAction() {
        return action;
    }

    public void setAction(EAction action) {
        this.action = action;
    }
}
