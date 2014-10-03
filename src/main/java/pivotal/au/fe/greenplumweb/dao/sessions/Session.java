package pivotal.au.fe.greenplumweb.dao.sessions;

public class Session
{
    private String procpid, usename, sessId, currentQuery, waiting, queryStart;

    public Session()
    {
    }

    public Session(String procpid, String usename, String sessId, String currentQuery, String waiting, String queryStart) {
        this.procpid = procpid;
        this.usename = usename;
        this.sessId = sessId;
        this.currentQuery = currentQuery;
        this.waiting = waiting;
        this.queryStart = queryStart;
    }

    public String getProcpid() {
        return procpid;
    }

    public void setProcpid(String procpid) {
        this.procpid = procpid;
    }

    public String getUsename() {
        return usename;
    }

    public void setUsename(String usename) {
        this.usename = usename;
    }

    public String getSessId() {
        return sessId;
    }

    public void setSessId(String sessId) {
        this.sessId = sessId;
    }

    public String getCurrentQuery() {
        return currentQuery;
    }

    public void setCurrentQuery(String currentQuery) {
        this.currentQuery = currentQuery;
    }

    public String getWaiting() {
        return waiting;
    }

    public void setWaiting(String waiting) {
        this.waiting = waiting;
    }

    public String getQueryStart() {
        return queryStart;
    }

    public void setQueryStart(String queryStart) {
        this.queryStart = queryStart;
    }

    @Override
    public String toString() {
        return "Session{" +
                "procpid='" + procpid + '\'' +
                ", usename='" + usename + '\'' +
                ", sessId='" + sessId + '\'' +
                ", currentQuery='" + currentQuery + '\'' +
                ", waiting='" + waiting + '\'' +
                ", queryStart='" + queryStart + '\'' +
                '}';
    }
}
