package pivotal.au.fe.greenplumweb.dao.sessions;

import org.apache.log4j.Logger;
import pivotal.au.fe.greenplumweb.dao.GreenplumWebDAOUtil;
import pivotal.au.fe.greenplumweb.main.GreenplumException;
import pivotal.au.fe.greenplumweb.main.Result;
import pivotal.au.fe.greenplumweb.utils.AdminUtil;
import pivotal.au.fe.greenplumweb.utils.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SessionDAOImpl implements SessionDAO
{
    protected static Logger logger = Logger.getLogger("controller");

    public List<Session> retrieveSessionList(String userKey) throws GreenplumException
    {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;
        List<Session> sessions = null;

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmt = conn.createStatement();
            rset = stmt.executeQuery(Constants.USER_SESSIONS);

            sessions = makeSessionListFromResultSet(rset);
        }
        catch (SQLException se)
        {
            logger.debug("Error retrieving all greenplum user sessions");
            throw new GreenplumException(se);
        }
        catch (Exception ex)
        {
            logger.debug("Error retrieving all greenplum user sessions");
            throw new GreenplumException(ex);
        }
        finally
        {
            // close all resources
            JDBCUtil.close(rset);
            JDBCUtil.close(stmt);
        }

        return sessions;
    }

    public Result killSession (String pid, String userKey) throws GreenplumException
    {
        String            command = null;
        Result            res     = null;

        command = String.format(Constants.KILL_SESSION, pid);

        res = GreenplumWebDAOUtil.runCommand(command, userKey);

        return res;
    }

    private List<Session> makeSessionListFromResultSet (ResultSet rset) throws SQLException
    {
        List<Session> sessions = new ArrayList<Session>();

        while (rset.next())
        {
            Session session = new Session(rset.getString(1),
                    rset.getString(2),
                    rset.getString(3),
                    rset.getString(4),
                    rset.getString(5),
                    rset.getString(6));

            sessions.add(session);
        }

        return sessions;

    }
}
