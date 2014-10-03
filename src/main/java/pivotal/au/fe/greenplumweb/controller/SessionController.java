package pivotal.au.fe.greenplumweb.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pivotal.au.fe.greenplumweb.dao.GreenplumWebDAOFactory;
import pivotal.au.fe.greenplumweb.dao.GreenplumWebDAOUtil;
import pivotal.au.fe.greenplumweb.dao.sessions.Session;
import pivotal.au.fe.greenplumweb.dao.sessions.SessionDAO;
import pivotal.au.fe.greenplumweb.dao.views.View;
import pivotal.au.fe.greenplumweb.dao.views.ViewDAO;
import pivotal.au.fe.greenplumweb.main.Result;
import pivotal.au.fe.greenplumweb.main.UserPref;
import pivotal.au.fe.greenplumweb.utils.AdminUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class SessionController
{
    protected static Logger logger = Logger.getLogger("controller");

    @RequestMapping(value = "/sessions", method = RequestMethod.GET)
    public String showSessions
            (Model model, HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception
    {
        int startAtIndex = 0, endAtIndex = 0;
        String schema = null;

        if (session.getAttribute("user_key") == null)
        {
            logger.debug("user_key is null new Login required");
            response.sendRedirect(request.getContextPath() + "/PCFHawq-Web/login");
            return null;
        }
        else
        {
            Connection conn = AdminUtil.getConnection((String) session.getAttribute("user_key"));
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

        logger.debug("Received request to show user sessions");

        SessionDAO sessionDAO = GreenplumWebDAOFactory.getSessionDAO();

        Result result = new Result();

        String sessionAction = request.getParameter("sessionAction");

        if (sessionAction != null)
        {
            logger.debug("sessionAction = " + sessionAction);

            if (sessionAction.equals("DESTROY"))
            {
                result = null;
                result =
                        sessionDAO.killSession
                                ((String) request.getParameter("pid"),
                                 (String) session.getAttribute("user_key"));

                model.addAttribute("result", result);
            }
        }

        List<Session> sessions = sessionDAO.retrieveSessionList
            ((String) session.getAttribute("user_key"));

        model.addAttribute("sessions", sessions);
        model.addAttribute("records", sessions.size());

        // This will resolve to /WEB-INF/jsp/sessions.jsp
        return "sessions";
    }

}
