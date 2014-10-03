package pivotal.au.fe.greenplumweb.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pivotal.au.fe.greenplumweb.beans.Login;
import pivotal.au.fe.greenplumweb.main.UserPref;
import pivotal.au.fe.greenplumweb.utils.AdminUtil;
import pivotal.au.fe.greenplumweb.utils.ConnectionManager;
import pivotal.au.fe.greenplumweb.utils.QueryUtil;
import pivotal.au.fe.greenplumweb.utils.GreenplumJDBCConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.Map;

@Controller
public class AutoLoginController
{
    protected static Logger logger = Logger.getLogger("controller");

    @RequestMapping(value = "/autologin", method = RequestMethod.GET)
    public String autologin
            (Model model,
             HttpSession session,
             HttpServletRequest request) throws Exception
    {
        logger.debug("Received request to auto login");
        ConnectionManager cm = ConnectionManager.getInstance();
        Connection conn;

        try
        {
            String username = fixRequestParam(request.getParameter("username"));
            String passwd = fixRequestParam(request.getParameter("passwd"));
            String url = fixRequestParam(request.getParameter("url"));

            logger.debug ("username = " + username);
            logger.debug ("passwd = " + passwd);
            logger.debug ("url = " + url);

            if (username.trim().equals(""))
            {
                conn = AdminUtil.getNewConnection(url);
            }
            else
            {
                conn = AdminUtil.getNewConnection
                        (url,
                         username,
                         passwd);
            }

            GreenplumJDBCConnection newConn =
                    new GreenplumJDBCConnection
                            (conn,
                             url,
                             new java.util.Date().toString(),
                             username);

            cm.addConnection(newConn, session.getId());

            session.setAttribute("user_key", session.getId());
            session.setAttribute("user", username);
            session.setAttribute("schema", "public");
            session.setAttribute("url", url);
            session.setAttribute("prefs", new UserPref());
            session.setAttribute("history", new LinkedList());
            session.setAttribute("connectedAt", new java.util.Date().toString());

            Map<String, String> schemaMap = AdminUtil.getSchemaMap();

            // get schema count now
            schemaMap = QueryUtil.populateSchemaMap
                    (conn, schemaMap, username);

            session.setAttribute("schemaMap", schemaMap);

            // This will resolve to /WEB-INF/jsp/main.jsp
            return "main";
        }
        catch (Exception ex)
        {
            model.addAttribute("error", ex.getMessage());
            // This will resolve to /WEB-INF/jsp/loginpage.jsp
            return "loginpage";
        }

    }

    private String fixRequestParam (String s)
    {
        if (s == null)
        {
            return "";
        }
        else
        {
            return s;
        }
    }
}
