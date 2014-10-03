package pivotal.au.fe.greenplumweb.dao.sessions;

public interface Constants
{
    public static final String USER_SESSIONS =
            "select procpid, usename, sess_id, current_query, waiting, query_start " +
            "from pg_stat_activity " +
            "order by usename, query_start";

    public static final String KILL_SESSION =
            "select pg_terminate_backend(%s)";
}
