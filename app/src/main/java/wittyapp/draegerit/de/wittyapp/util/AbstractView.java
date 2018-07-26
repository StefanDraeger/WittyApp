package wittyapp.draegerit.de.wittyapp.util;

import android.content.Context;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public abstract class AbstractView {

    private EActiveView activeView;

    private Context ctx;
    private View view;

    private int updateInterval = 1000;

    protected AbstractView(Context context, View view) {
        this.ctx = context;
        this.view = view;
    }

    protected AbstractView(Context context, View view, EActiveView activeViewType) {
        this(context, view);
        this.activeView = activeViewType;
        initialize();
    }

    public abstract void clearData();

    public abstract void initialize();

    public abstract void update();

    public EActiveView getActiveView() {
        return activeView;
    }

    public void setActiveView(EActiveView activeView) {
        this.activeView = activeView;
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

}
