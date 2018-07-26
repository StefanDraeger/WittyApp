package wittyapp.draegerit.de.wittyapp.util.console;

import java.util.ArrayList;
import java.util.List;

public final class ConsoleUtil {

    private static List<ConsoleEntry> consoleEntries = new ArrayList<>();

    private ConsoleUtil(){
        //Empty
    }

    public static void addEntry(ConsoleEntry e) {
        consoleEntries.add(e);
    }

    public static List<ConsoleEntry> getEntries() {
        return consoleEntries;
    }

    public static void clear() {
        consoleEntries.clear();
    }

}
