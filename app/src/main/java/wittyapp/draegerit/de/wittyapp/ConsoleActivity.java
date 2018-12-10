package wittyapp.draegerit.de.wittyapp;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import wittyapp.draegerit.de.wittyapp.frames.OnFragmentInteractionListener;
import wittyapp.draegerit.de.wittyapp.util.EActiveView;
import wittyapp.draegerit.de.wittyapp.util.IActionView;
import wittyapp.draegerit.de.wittyapp.util.console.ConsoleEntry;
import wittyapp.draegerit.de.wittyapp.util.console.ConsoleUtil;

public class ConsoleActivity extends Fragment implements OnFragmentInteractionListener, IActionView {

    private static DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm:SS");

    private static List<TableRow> rows = new ArrayList<>();

    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_console, container, false);
        update();
        return view;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void save() {

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
    public void update() {
        TableLayout tableLayout = view.findViewById(R.id.consoleTableView);
        for (TableRow row : rows) {
            tableLayout.removeView(row);
        }
        TableRow emptyTblRow = view.findViewById(R.id.emptyTblRow);
        emptyTblRow.setVisibility(View.GONE);
        if (ConsoleUtil.getEntries().isEmpty()) {
            emptyTblRow.setVisibility(View.VISIBLE);
        } else {
            for (ConsoleEntry entry : ConsoleUtil.getEntries()) {
                TableRow row = new TableRow(getContext());
                TextView timestampTextView = new TextView(getContext());
                timestampTextView.setText(formatTimestamp(entry.getTimestamp()));
                row.addView(timestampTextView);
                ImageView img = new ImageView(getContext());
                int image = R.drawable.ic_keyboard_arrow_right_black_24dp;
                if (!entry.isSendData()) {
                    image = R.drawable.ic_keyboard_arrow_left_black_24dp;
                }
                img.setImageDrawable(getContext().getDrawable(image));
                row.addView(img);
                TextView valueTextView = new TextView(getContext());
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

    @Override
    public int getUpdateInterval() {
        return 0;
    }

    @Override
    public void setUpdateInterval(int intervall) {

    }

    @Override
    public void addChartEntry(String entryValue) {

    }
}
