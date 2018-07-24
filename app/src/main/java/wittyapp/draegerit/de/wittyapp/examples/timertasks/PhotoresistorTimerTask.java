package wittyapp.draegerit.de.wittyapp.examples.timertasks;

import android.util.Log;

import java.util.TimerTask;

public class PhotoresistorTimerTask extends TimerTask {
    @Override
    public void run() {
        Log.i("WittyApp", "Hallo Welt!");
    }
}
