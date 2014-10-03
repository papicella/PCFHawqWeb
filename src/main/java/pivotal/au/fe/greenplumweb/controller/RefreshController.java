package pivotal.au.fe.greenplumweb.controller;

import java.sql.Connection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pivotal.au.fe.greenplumweb.utils.AdminUtil;
import pivotal.au.fe.greenplumweb.utils.QueryUtil;

@Controller
public class RefreshController
{
    protected static Logger logger = Logger.getLogger("controller");

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public String showTables
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

        logger.debug("Received request refresh schema object list");

        Map schemaMap = (Map) session.getAttribute("schemaMap");
        Connection conn = AdminUtil.getConnection((String)session.getAttribute("user_key"));

        // get schema count now
        schemaMap = QueryUtil.populateSchemaMap
                (conn, schemaMap, (String) session.getAttribute("schema"));

        session.setAttribute("schemaMap", schemaMap);

        // This will resolve to /WEB-INF/jsp/main.jsp
        return "main";
    }


}

