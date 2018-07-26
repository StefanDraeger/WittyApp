package wittyapp.draegerit.de.wittyapp;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import wittyapp.draegerit.de.wittyapp.R;
import wittyapp.draegerit.de.wittyapp.util.AbstractView;
import wittyapp.draegerit.de.wittyapp.util.EActiveView;
import wittyapp.draegerit.de.wittyapp.util.console.ConsoleEntry;
import wittyapp.draegerit.de.wittyapp.util.console.ConsoleUtil;

public class ConsoleView extends AbstractView {

    private static DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm:SS");

    private static List<TableRow> rows = new ArrayList<>();

    public ConsoleView(Context ctx, ViewSwitcher view) {
        super(ctx, view, EActiveView.CONSOLE);
    }

    @Override
    public void clearData() {
        ConsoleUtil.clear();
        TableLayout tableLayout = this.getView().findViewById(R.id.consoleTableView);
        for (TableRow row : rows) {
            tableLayout.removeView(row);
        }
        rows.clear();
        update();
    }

    @Override
    public void initialize() {
        update();
    }

    @Override
    public void update() {
        TableLayout tableLayout = this.getView().findViewById(R.id.consoleTableView);
        for (TableRow row : rows) {
            tableLayout.removeView(row);
        }
        TableRow emptyTblRow = this.getView().findViewById(R.id.emptyTblRow);
        emptyTblRow.setVisibility(View.GONE);
        if (ConsoleUtil.getEntries().isEmpty()) {
            emptyTblRow.setVisibility(View.VISIBLE);
        } else {
            for (ConsoleEntry entry : ConsoleUtil.getEntries()) {
                TableRow row = new TableRow(getCtx());
                TextView timestampTextView = new TextView(getCtx());
                timestampTextView.setText(formatTimestamp(entry.getTimestamp()));
                row.addView(timestampTextView);
                ImageView img = new ImageView(getCtx());
                int image = R.drawable.ic_keyboard_arrow_right_black_24dp;
                if (!entry.isSendData()) {
                    image = R.drawable.ic_keyboard_arrow_left_black_24dp;
                }
                img.setImageDrawable(getCtx().getDrawable(image));
                row.addView(img);
                TextView valueTextView = new TextView(getCtx());
                valueTextView.setText(entry.getValue());
                row.addView(valueTextView);
                rows.add(row);
                tableLayout.addView(row);
            }
        }
    }

    private String formatTimestamp(long timestamp) {
        return dateFormat.format(new Date(timestamp));
    }
}
