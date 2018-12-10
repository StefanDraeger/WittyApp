package wittyapp.draegerit.de.wittyapp.util;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class AbstractView {

    private static final String DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm:ss,SSS";
    private static DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);

    private EActiveView activeViewType;

    private Context ctx;
    private View view;

    private int updateInterval = 1000;

    protected AbstractView(Context context, View view) {
        this.ctx = context;
        this.view = view;
    }

    protected AbstractView(Context context, View view, EActiveView activeViewType) {
        this(context, view);
        this.activeViewType = activeViewType;
        initialize();
    }

    public abstract void clearData();

    public abstract void initialize();

    public abstract void update();

    public EActiveView getActiveViewType() {
        return activeViewType;
    }

    public void setActiveViewType(EActiveView activeViewType) {
        this.activeViewType = activeViewType;
    }

    public Context getCtx() {
        return ctx;
    }

    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(int updateInterval) {
        this.updateInterval = updateInterval;
    }

    public void save() {
        Log.i("ESP-App", "Not implemented!");
    }

    protected List<String> getLabels(int counter) {
        List<String> labels = new ArrayList<>();
        for (int i = 0; i <= counter; i++) {
            labels.add(String.valueOf(i));
        }
        return labels;
    }

    protected String formatDateTime(long timestamp) {
        return dateFormat.format(new Date(timestamp));
    }

}
