package wittyapp.draegerit.de.wittyapp.util;

public interface IActionView {

    void save();

    void clearData();

    void update();

    int getUpdateInterval();

    void setUpdateInterval(int intervall);

    void addChartEntry(String entryValue);
}