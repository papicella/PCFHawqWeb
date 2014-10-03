package pivotal.au.fe.greenplumweb.controller;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pivotal.au.fe.greenplumweb.main.UserPref;
import pivotal.au.fe.greenplumweb.utils.AdminUtil;
import pivotal.au.fe.greenplumweb.utils.ConnectionManager;

@Controller
public class PrefsController
{
    protected static Logger logger = Logger.getLogger("controller");

    @RequestMapping(value = "/preferences", method = RequestMethod.GET)
    public String showPreferences
            (Model model, HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception
    {
        if (session.getAttribute("user_key") == null)
        {
            logger.debug("user_key is null new Login required");
            response.sendRedirect(request.getContextPath() + "/PCFHawq-Web/login");
            return null;
        }
        else
        {
            Connection conn = AdminUtil.getConnection((String)session.getAttribute("user_key"));
            if (conn == null )
            {
                response.sendRedirect(request.getContextPath() + "/PCFHawq-Web/login");
                return null;
            }
            else
            {
                if (conn.isClosed())
                {
                    response.sendRedirect(request.getContextPath() + "/PCFHawq-Web/login");
                    return null;
                }
            }

        }

        logger.debug("Received request to show preferences");

        model.addAttribute("userPref", (UserPref) session.getAttribute("prefs"));

        // This will resolve to /WEB-INF/jsp/preferences.jsp
        return "preferences";
    }

    @RequestMapping(value = "/preferences", method = RequestMethod.POST)
    public String updatePreferences
            (Model model, HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception
    {
        if (session.getAttribute("user_key") == null)
        {
            logger.debug("user_key is null new Login required");
            response.sendRedirect(request.getContextPath() + "/PCFHawq-Web/login");
            return null;
        }
        else
        {
            Connection conn = AdminUtil.getConnection((String)session.getAttribute("user_key"));
            if (conn == null )
            {
                response.sendRedirect(request.getContextPath() + "/PCFHawq-Web/login");
                return null;
            }
            else
            {
                if (conn.isClosed())
                {
                    response.sendRedirect(request.getContextPath() + "/PCFHawq-Web/login");
                    return null;
                }
            }

        }

        logger.debug("Received request to update preferences");

        UserPref userPrefs = (UserPref) session.getAttribute("prefs");

        userPrefs.setRecordsToDisplay
                (Integer.parseInt(request.getParameter("recordsToDisplay")));
        userPrefs.setMaxRecordsinSQLQueryWindow
                (Integer.parseInt(request.getParameter("maxRecordsinSQLQueryWindow")));
        userPrefs.setAutoCommit
                ((String)request.getParameter("autoCommit"));

        ConnectionManager cm = ConnectionManager.getInstance();
        Connection conn = AdminUtil.getConnection((String)session.getAttribute("user_key"));

        if (request.getParameter("autoCommit").equals("Y"))
        {
            conn.setAutoCommit(true);
        }
        else
        {
            conn.setAutoCommit(false);
        }

        cm.updateConnection(conn, (String)session.getAttribute("user_key"));

        model.addAttribute("saved", "Successfully saved application preferences");
        session.setAttribute("userPref", userPrefs);

        // This will resolve to /WEB-INF/jsp/preferences.jsp
        return "preferences";
    }
}

