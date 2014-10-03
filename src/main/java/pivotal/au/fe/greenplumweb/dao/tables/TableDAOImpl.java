package pivotal.au.fe.greenplumweb.dao.tables;

import org.apache.log4j.Logger;
import pivotal.au.fe.greenplumweb.dao.GreenplumWebDAOUtil;
import pivotal.au.fe.greenplumweb.main.GreenplumException;
import pivotal.au.fe.greenplumweb.main.Result;
import pivotal.au.fe.greenplumweb.utils.AdminUtil;
import pivotal.au.fe.greenplumweb.utils.JDBCUtil;

import javax.servlet.jsp.jstl.sql.ResultSupport;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TableDAOImpl implements TableDAO
{
    protected static Logger logger = Logger.getLogger("controller");

    public List<Table> retrieveTableList(String schema, String search, String type, String userKey) throws GreenplumException
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rset = null;
        List<Table>       tbls = null;
        String            srch = null;

        try
        {
            conn = AdminUtil.getConnection(userKey);

            if (type.equalsIgnoreCase("external"))
            {
                stmt = conn.prepareStatement(Constants.USER_TABLES_EXTERNAL);
            }
            else
            {
                stmt = conn.prepareStatement(Constants.USER_TABLES);
            }

            if (search == null)
                srch = "%";
            else
                srch = "%" + search + "%";

            stmt.setString(1, schema);
            stmt.setString(2, srch);
            rset = stmt.executeQuery();

            tbls = makeTableListFromResultSet(rset);
        }
        catch (SQLException se)
        {
            logger.debug("Error retrieving all tables with search string = " + search);
            throw new GreenplumException(se);
        }
        catch (Exception ex)
        {
            logger.debug("Error retrieving all tables with search string = " + search);
            throw new GreenplumException(ex);
        }
        finally
        {
            // close all resources
            JDBCUtil.close(rset);
            JDBCUtil.close(stmt);
        }

        return tbls;
    }

    public Result simpletableCommand(String schemaName, String tableName, String type, String userKey) throws GreenplumException
    {
        String            command = null;
        Result            res     = null;

        if (type != null)
        {
            if (type.equalsIgnoreCase("DROP"))
            {
                if (schemaName.equalsIgnoreCase("public"))
                {
                    command = String.format(Constants.DROP_TABLE_PUBLIC, tableName);
                }
                else
                {
                    command = String.format(Constants.DROP_TABLE, schemaName, tableName);
                }
            }
            else if (type.equalsIgnoreCase("EMPTY"))
            {
                if (schemaName.equalsIgnoreCase("public"))
                {
                    command = String.format(Constants.TRUNCATE_TABLE_PUBLIC, tableName);
                }
                else
                {
                    command = String.format(Constants.TRUNCATE_TABLE, schemaName, tableName);
                }
            }

        }

        res = GreenplumWebDAOUtil.runCommand(command, userKey);

        return res;
    }

    public javax.servlet.jsp.jstl.sql.Result getTableStructure (String schema, String tableName, String userKey) throws GreenplumException
    {
        Connection        conn = null;
        PreparedStatement stmt = null;
        ResultSet         rset = null;
        javax.servlet.jsp.jstl.sql.Result res = null;

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmt = conn.prepareStatement(Constants.USER_TAB_COLUMNS);
            stmt.setString(1, schema);
            stmt.setString(2, tableName);
            rset = stmt.executeQuery();

            res = ResultSupport.toResult(rset);

        }
        catch (SQLException se)
        {
            logger.debug("Error retrieving table structure for table " + tableName);
            throw new GreenplumException(se);
        }
        catch (Exception ex)
        {
            logger.debug("Error retrieving table structure for table  " + tableName);
            throw new GreenplumException(ex);
        }
        finally
        {
            // close all resources
            JDBCUtil.close(rset);
            JDBCUtil.close(stmt);
        }

        return res;
    }

    private List<Table> makeTableListFromResultSet (ResultSet rset) throws SQLException
    {
        List<Table> tbls = new ArrayList<Table>();

        while (rset.next())
        {
            Table table = new Table(rset.getString(1),
                    rset.getString(2),
                    rset.getString(3),
                    rset.getString(4),
                    rset.getString(5));

            tbls.add(table);
        }

        return tbls;

    }
}
