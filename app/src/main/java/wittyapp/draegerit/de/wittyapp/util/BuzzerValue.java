package wittyapp.draegerit.de.wittyapp.util;

public class BuzzerValue {

    private int frequenz;
    private int duration;

    public BuzzerValue(int frequenz, int duration) {
        this.frequenz = frequenz;
        this.duration = duration;
    }

    public int getFrequenz() {
        return frequenz;
    }

    public void setFrequenz(int frequenz) {
        this.frequenz = frequenz;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
