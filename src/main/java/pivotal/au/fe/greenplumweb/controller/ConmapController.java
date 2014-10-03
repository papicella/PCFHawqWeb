package pivotal.au.fe.greenplumweb.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pivotal.au.fe.greenplumweb.utils.AdminUtil;
import pivotal.au.fe.greenplumweb.utils.ConnectionManager;

import java.sql.Connection;

@Controller
public class ConmapController
{
    protected static Logger logger = Logger.getLogger("controller");

    @RequestMapping(value = "/viewconmap", method = RequestMethod.GET)
    public String worksheet
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

        logger.debug("Received request to show connection map");

        ConnectionManager cm = ConnectionManager.getInstance();

        String conMapAction = request.getParameter("conMapAction");
        String key = request.getParameter("key");

        logger.debug("conMapAction = " + conMapAction);
        logger.debug("key = " + key);

        if (conMapAction != null)
        {
            if (conMapAction.equalsIgnoreCase("DELETE"))
            {
                // remove this connection from Map and close it.
                cm.removeConnection(key);
                logger.debug("Connection closed for key " + key);
                model.addAttribute("saved", "Successfully closed connection with key " + key);
            }
        }

        model.addAttribute("conmap", cm.getConnectionMap());
        model.addAttribute("conmapsize", cm.getConnectionListSize());

        // This will resolve to /WEB-INF/jsp/conmap.jsp
        return "conmap";
    }
}

