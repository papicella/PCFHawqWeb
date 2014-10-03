package pivotal.au.fe.greenplumweb.dao.views;

import org.apache.log4j.Logger;
import pivotal.au.fe.greenplumweb.dao.GreenplumWebDAOUtil;
import pivotal.au.fe.greenplumweb.main.GreenplumException;
import pivotal.au.fe.greenplumweb.main.Result;
import pivotal.au.fe.greenplumweb.utils.AdminUtil;
import pivotal.au.fe.greenplumweb.utils.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ViewDAOImpl implements ViewDAO
{
    protected static Logger logger = Logger.getLogger("controller");

    public List<View> retrieveViewList(String schema, String search, String userKey) throws GreenplumException
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet         rset = null;
        List<View>        views = null;
        String            srch = null;

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmt = conn.prepareStatement(Constants.USER_VIEWS);
            if (search == null)
                srch = "%";
            else
                srch = "%" + search + "%";

            stmt.setString(1, schema);
            stmt.setString(2, srch);
            rset = stmt.executeQuery();

            views = makeViewListFromResultSet(rset);
        }
        catch (SQLException se)
        {
            logger.debug("Error retrieving all views with search string = " + search);
            throw new GreenplumException(se);
        }
        catch (Exception ex)
        {
            logger.debug("Error retrieving all views with search string = " + search);
            throw new GreenplumException(ex);
        }
        finally
        {
            // close all resources
            JDBCUtil.close(rset);
            JDBCUtil.close(stmt);
        }

        return views;
    }

    public Result simpleviewCommand (String schemaName, String viewName, String type, String userKey) throws GreenplumException
    {
        String            command = null;
        Result            res     = null;

        if (type != null)
        {
            if (type.equalsIgnoreCase("DROP"))
            {
                if (schemaName.equalsIgnoreCase("public"))
                {
                    command = String.format(Constants.DROP_VIEW_PUBLIC, viewName);
                }
                else
                {
                    command = String.format(Constants.DROP_VIEW, schemaName, viewName);
                }
            }
        }

        res = GreenplumWebDAOUtil.runCommand(command, userKey);

        return res;
    }

    public String getViewDefinition(String schemaName, String viewName, String userKey) throws GreenplumException
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet         rset = null;
        String            def = null;

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmt = conn.prepareStatement(Constants.USER_VIEW_DEF);
            stmt.setString(1, schemaName);
            stmt.setString(2, viewName);
            rset = stmt.executeQuery();

            rset.next();

            def = rset.getString(1);

        }
        catch (SQLException se)
        {
            logger.debug("Error retrieving view definition for view = " + viewName);
            throw new GreenplumException(se);
        }
        catch (Exception ex)
        {
            logger.debug("Error retrievingview definition for view = " + viewName);
            throw new GreenplumException(ex);
        }
        finally
        {
            // close all resources
            JDBCUtil.close(rset);
            JDBCUtil.close(stmt);
        }

        return def;

    }

    private List<View> makeViewListFromResultSet (ResultSet rset) throws SQLException
    {
        List<View> views = new ArrayList<View>();

        while (rset.next())
        {
            View view = new View(rset.getString(1),
                    rset.getString(2),
                    rset.getString(3),
                    rset.getString(4),
                    rset.getString(5));
            views.add(view);
        }

        return views;

    }
}
