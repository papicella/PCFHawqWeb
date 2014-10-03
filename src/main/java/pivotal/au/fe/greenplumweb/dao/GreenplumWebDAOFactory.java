package pivotal.au.fe.greenplumweb.dao;

import pivotal.au.fe.greenplumweb.dao.sessions.SessionDAO;
import pivotal.au.fe.greenplumweb.dao.sessions.SessionDAOImpl;
import pivotal.au.fe.greenplumweb.dao.tables.TableDAO;
import pivotal.au.fe.greenplumweb.dao.tables.TableDAOImpl;
import pivotal.au.fe.greenplumweb.dao.views.ViewDAO;
import pivotal.au.fe.greenplumweb.dao.views.ViewDAOImpl;

public class GreenplumWebDAOFactory
{
    public static TableDAO getTableDAO()
    {
        return new TableDAOImpl();
    }

    public static ViewDAO getViewDAO()
    {
        return new ViewDAOImpl();
    }

    public static SessionDAO getSessionDAO()
    {
        return new SessionDAOImpl();
    }
}
