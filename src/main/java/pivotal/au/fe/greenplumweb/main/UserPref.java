package pivotal.au.fe.greenplumweb.main;

public class UserPref
{
    private int recordsToDisplay;
    private int maxRecordsinSQLQueryWindow;
    private String autoCommit;
    private int historySize;

    public UserPref()
    {
        recordsToDisplay = 20;
        maxRecordsinSQLQueryWindow = 5000;
        autoCommit = "N";
        historySize = 50;
    }

    public UserPref(int recordsToDisplay, int maxRecordsinSQLQueryWindow, String autoCommit, int historySize) {
        this.recordsToDisplay = recordsToDisplay;
        this.maxRecordsinSQLQueryWindow = maxRecordsinSQLQueryWindow;
        this.autoCommit = autoCommit;
        this.historySize = historySize;
    }

    public int getRecordsToDisplay() {
        return recordsToDisplay;
    }

    public void setRecordsToDisplay(int recordsToDisplay) {
        this.recordsToDisplay = recordsToDisplay;
    }

    public int getMaxRecordsinSQLQueryWindow() {
        return maxRecordsinSQLQueryWindow;
    }

    public void setMaxRecordsinSQLQueryWindow(int maxRecordsinSQLQueryWindow) {
        this.maxRecordsinSQLQueryWindow = maxRecordsinSQLQueryWindow;
    }

    public String getAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(String autoCommit) {
        this.autoCommit = autoCommit;
    }


    public int getHistorySize() {
        return historySize;
    }

    public void setHistorySize(int historySize) {
        this.historySize = historySize;
    }

    @Override
    public String toString() {
        return "UserPref [recordsToDisplay=" + recordsToDisplay
                + ", maxRecordsinSQLQueryWindow=" + maxRecordsinSQLQueryWindow
                + ", autoCommit=" + autoCommit + ", historySize=" + historySize
                + "]";
    }



}