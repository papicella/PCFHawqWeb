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

import java.sql.Connection;

@Controller
public class HomeController
{
    protected static Logger logger = Logger.getLogger("controller");

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String login(Model model, HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception
    {
        logger.debug("Received request to show home page");

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

        // This will resolve to /WEB-INF/jsp/main.jsp
        return "main";
    }


}
