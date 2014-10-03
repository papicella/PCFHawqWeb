package pivotal.au.fe.greenplumweb.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;

import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;

import pivotal.au.fe.greenplumweb.beans.CommandResult;

public class QueryUtil
{
    static public String runExplainPlan (Connection conn, String query) throws SQLException
    {
        Statement stmt = null;
        String sql = "explain analyze %s";
        ResultSet rset = null;
        String result = null;
        try
        {
            stmt = conn.createStatement();
            rset = stmt.executeQuery(String.format(sql, query));
            // should only return one row
            rset.next();

            result = rset.getString(1);

        }
        finally
        {
            if (stmt != null)
            {
                try
                {
                    stmt.close();
                }
                catch (SQLException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (rset != null)
            {
                try
                {
                    stmt.close();
                }
                catch (SQLException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }

        return result;

    }

    static public Result runQuery (Connection conn, String query, int maxrows) throws SQLException
    {
        Statement stmt  = null;
        ResultSet rset  = null;
        Result    res   = null;

        try
        {
            stmt = conn.createStatement();
            rset = stmt.executeQuery(query);

	      /*
	       * Convert the ResultSet to a
	       * Result object that can be used with JSTL tags
	       */
            if (maxrows == -1)
            {
                res = ResultSupport.toResult(rset);
            }
            else
            {
                res = ResultSupport.toResult(rset, maxrows);
            }
        }
        finally
        {
            JDBCUtil.close(stmt);
            JDBCUtil.close(rset);
        }

        return res;
    }

    static public List<Result> runStoredprocWithResultSet
            (Connection conn, String query, int maxrows, int resultsets) throws SQLException
    {
        PreparedStatement pstmt = null;
        ResultSet rset  = null;
        Result    res   = null;
        List<Result> results = new ArrayList<Result>();

        try
        {

            for (int i = 1; i <= resultsets; i++)
            {
                if (i == 1)
                {
                    pstmt = conn.prepareCall(query);
                    pstmt.execute();
                }
                else
                {
                    pstmt.getMoreResults();
                }

                rset = pstmt.getResultSet();
                res = null;

    	      /*
    	       * Convert the ResultSet to a
    	       * Result object that can be used with JSTL tags
    	       */
                if (maxrows == -1)
                {
                    res = ResultSupport.toResult(rset);
                }
                else
                {
                    res = ResultSupport.toResult(rset, maxrows);
                }

                results.add(res);

                rset.close();
                rset = null;
            }


        }
        finally
        {
            JDBCUtil.close(pstmt);
            JDBCUtil.close(rset);
        }

        return results;
    }

    static public int runQueryCount (Connection conn, String query) throws SQLException
    {
        Statement stmt  = null;
        ResultSet rset  = null;
        int count = 0;

        try
        {
            stmt = conn.createStatement();
            rset = stmt.executeQuery("select count(*) from (" + query + ") as \"Count\"");
            rset.next();
            count = rset.getInt(1);
        }
        catch (SQLException se)
        {
            // do nothing if we can't get count.
        }
        finally
        {
            JDBCUtil.close(stmt);
            JDBCUtil.close(rset);
        }

        return count;
    }

    static public CommandResult runCommitOrRollback (Connection conn, boolean commit, String elapsedTime) throws SQLException
    {
        CommandResult res = new CommandResult();

        try
        {
            long start = System.currentTimeMillis();

            if (commit)
            {
                conn.commit();
                res.setCommand("commit");
            }
            else
            {
                conn.rollback();
                res.setCommand("rollback");
            }

            long end = System.currentTimeMillis();
            double timeTaken = new Double(end - start).doubleValue();
            DecimalFormat df = new DecimalFormat("#.##");

            if (elapsedTime.equals("Y"))
            {
                res.setElapsedTime("" + df.format(timeTaken));
            }

            res.setRows(0);
            res.setMessage("SUCCESS");

        }
        catch (SQLException se)
        {
            // we don't want to stop it running we just need the error
            res.setMessage(se.getMessage());
            res.setRows(-1);
        }

        return res;

    }

    static public CommandResult runCommand (Connection conn, String command, String elapsedTime) throws SQLException
    {
        CommandResult res = new CommandResult();

        Statement         stmt    = null;

        res.setCommand(command);

        try
        {
            stmt = conn.createStatement();

            long start = System.currentTimeMillis();
            int rowsAffected = stmt.executeUpdate(command);
            long end = System.currentTimeMillis();

            double timeTaken = new Double(end - start).doubleValue();
            DecimalFormat df = new DecimalFormat("#.##");

            res.setRows(rowsAffected);

            // no need to commit it's auto commit already as it's DDL statement.
            res.setCommand(command);
            res.setMessage("SUCCESS");

            if (elapsedTime.equals("Y"))
            {
                res.setElapsedTime("" + df.format(timeTaken));
            }
        }
        catch (SQLException se)
        {
            // we don't want to stop it running we just need the error
            res.setMessage(se.getMessage());
            res.setRows(-1);
        }
        finally
        {
            JDBCUtil.close(stmt);
        }

        return res;
    }

    static public Map<String, String> populateSchemaMap(Connection conn, Map<String, String> schemaMap, String schema) throws SQLException
    {
        String sql = pivotal.au.fe.greenplumweb.dao.tables.Constants.USER_TABLES_COUNT +
                     "union " +
                     pivotal.au.fe.greenplumweb.dao.tables.Constants.USER_TABLES_EXTERNAL_COUNT +
                     "union " +
                     pivotal.au.fe.greenplumweb.dao.views.Constants.USER_VIEWS_COUNT;

        PreparedStatement pstmt = null;
        ResultSet rset = null;
        Map<String, String> schemaMapLocal = schemaMap;

        try
        {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, schema);
            pstmt.setString(2, schema);
            pstmt.setString(3, schema);

            rset = pstmt.executeQuery();
            while (rset.next())
            {
                schemaMapLocal.put(rset.getString(1).trim(), rset.getString(2));
            }

            //System.out.println(schemaMapLocal);
        }
        finally
        {
            JDBCUtil.close(pstmt);
        }

        return schemaMapLocal;
    }
}
