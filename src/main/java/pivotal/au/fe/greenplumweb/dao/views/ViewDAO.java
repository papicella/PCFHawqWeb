package pivotal.au.fe.greenplumweb.dao.views;

import pivotal.au.fe.greenplumweb.main.GreenplumException;
import pivotal.au.fe.greenplumweb.main.Result;

import java.util.List;

public interface ViewDAO
{
    public List<View> retrieveViewList(String schema, String search, String userKey) throws GreenplumException;

    public Result simpleviewCommand (String schemaName, String viewName, String type, String userKey) throws GreenplumException;

    public String getViewDefinition(String schemaName, String viewName, String userKey) throws GreenplumException;
}

