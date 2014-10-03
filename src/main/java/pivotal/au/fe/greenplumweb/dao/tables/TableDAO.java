package pivotal.au.fe.greenplumweb.dao.tables;

import pivotal.au.fe.greenplumweb.main.GreenplumException;
import pivotal.au.fe.greenplumweb.main.Result;

import java.util.*;

public interface TableDAO
{
    public List<Table> retrieveTableList(String schema, String search, String type, String userKey) throws GreenplumException;

    public Result simpletableCommand (String schemaName, String tableName, String type, String userKey) throws GreenplumException;

    public javax.servlet.jsp.jstl.sql.Result getTableStructure (String schema, String tableName, String userKey) throws GreenplumException;

}
