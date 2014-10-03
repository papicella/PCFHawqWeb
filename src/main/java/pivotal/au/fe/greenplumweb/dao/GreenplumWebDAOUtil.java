package pivotal.au.fe.greenplumweb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import pivotal.au.fe.greenplumweb.main.Result;
import pivotal.au.fe.greenplumweb.main.GreenplumException;
import pivotal.au.fe.greenplumweb.utils.AdminUtil;
import pivotal.au.fe.greenplumweb.utils.JDBCUtil;

public class GreenplumWebDAOUtil
{
    static public Result runCommand (String command, String userKey) throws GreenplumException
    {
        Result res = new Result();
        Connection        conn    = null;
        Statement         stmt    = null;

        res.setCommand(command);

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmt = conn.createStatement();

            stmt.execute(command);
            // no need to commit it's auto commit already as it's DDL statement.
            res.setCommand(command);
            res.setMessage("SUCCESS");
        }
        catch (SQLException se)
        {
            // we don't want to stop it running we just need the error
            res.setMessage(se.getMessage());
        }
        catch (Exception ex)
        {
            throw new GreenplumException(ex);
        }
        finally
        {
            JDBCUtil.close(stmt);
        }

        return res;
    }

    static public Result runStoredCommand (String command, String userKey) throws GreenplumException
    {
        Result res = new Result();
        Connection        			conn    = null;
        PreparedStatement         	stmt    = null;

        res.setCommand(command);

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmt = conn.prepareCall(command);
            stmt.execute();

            // no need to commit it's auto commit already as it's DDL statement.
            res.setCommand(command);
            res.setMessage("SUCCESS");
        }
        catch (SQLException se)
        {
            // we don't want to stop it running we just need the error
            res.setMessage(se.getMessage());
        }
        catch (Exception ex)
        {
            throw new GreenplumException(ex);
        }
        finally
        {
            JDBCUtil.close(stmt);
        }

        return res;
    }

    static public List<String> getAllSchemas (String userKey) throws GreenplumException
    {
        List<String> schemas = new ArrayList<String>();
        Connection        conn    = null;
        Statement         stmt    = null;
        ResultSet 		rset = null;
        String          sql = "select schema_name from information_schema.schemata order by 1";

        /*
        SELECT n.nspname AS "Name",
          pg_catalog.pg_get_userbyid(n.nspowner) AS "Owner"
        FROM pg_catalog.pg_namespace n
        WHERE	(n.nspname !~ '^pg_temp_' OR
		 n.nspname = (pg_catalog.current_schemas(true))[1])
         */

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmt = conn.createStatement();
            rset = stmt.executeQuery(sql);
            boolean foundPublic = false, foundInformation = false;

            while (rset.next())
            {
                String schema = rset.getString(1);
                if (schema.equals("public"))
                {
                    foundPublic = true;
                }

                if (schema.equals("information_schema"))
                {
                    foundInformation = true;
                }

                schemas.add(schema);
            }

            if (foundPublic == false)
            {
                schemas.add("public");
            }

            if (foundInformation == false)
            {
                schemas.add("information_schema");
            }
        }
        catch (Exception ex)
        {
            throw new GreenplumException(ex);
        }
        finally
        {
            JDBCUtil.close(stmt);
        }

        return schemas;

    }
}
