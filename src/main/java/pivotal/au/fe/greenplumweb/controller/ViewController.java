package pivotal.au.fe.greenplumweb.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pivotal.au.fe.greenplumweb.dao.GreenplumWebDAOFactory;
import pivotal.au.fe.greenplumweb.dao.GreenplumWebDAOUtil;
import pivotal.au.fe.greenplumweb.dao.views.View;
import pivotal.au.fe.greenplumweb.dao.views.ViewDAO;
import pivotal.au.fe.greenplumweb.main.Result;
import pivotal.au.fe.greenplumweb.main.UserPref;
import pivotal.au.fe.greenplumweb.utils.AdminUtil;

@Controller
public class ViewController
{
    protected static Logger logger = Logger.getLogger("controller");

    @RequestMapping(value = "/views", method = RequestMethod.GET)
    public String showViews
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

        logger.debug("Received request to show views");

        ViewDAO viewDAO = GreenplumWebDAOFactory.getViewDAO();
        Result result = new Result();

        String viewAction = request.getParameter("viewAction");
        String selectedSchema = request.getParameter("selectedSchema");
        logger.debug("selectedSchema = " + selectedSchema);

        if (selectedSchema != null)
        {
            schema = selectedSchema;
        }
        else
        {
            schema = (String)session.getAttribute("schema");
        }

        logger.debug("schema = " + schema);

        if (viewAction != null)
        {
            logger.debug("viewAction = " + viewAction);

            if (viewAction.equals("DEF"))
            {
                String def =
                        viewDAO.getViewDefinition
                                (schema,
                                 (String)request.getParameter("viewName"),
                                 (String)session.getAttribute("user_key"));

                model.addAttribute("viewName", (String)request.getParameter("viewName"));
                model.addAttribute("viewdef", def);
            }
            else
            {
                result = null;
                result =
                        viewDAO.simpleviewCommand
                                (schema,
                                (String)request.getParameter("viewName"),
                                viewAction,
                                (String)session.getAttribute("user_key"));

                model.addAttribute("result", result);
            }
        }

        List<View> views = viewDAO.retrieveViewList
                (schema,
                        null,
                        (String)session.getAttribute("user_key"));

        model.addAttribute("records", views.size());
        model.addAttribute("estimatedrecords", views.size());

        UserPref userPref = (UserPref) session.getAttribute("prefs");

        if (views.size() <= userPref.getRecordsToDisplay())
        {
            model.addAttribute("views", views);
        }
        else
        {
            if (request.getParameter("startAtIndex") != null)
            {
                startAtIndex = Integer.parseInt(request.getParameter("startAtIndex"));
            }

            if (request.getParameter("endAtIndex") != null)
            {
                endAtIndex = Integer.parseInt(request.getParameter("endAtIndex"));
                if (endAtIndex > views.size())
                {
                    endAtIndex = views.size();
                }
            }
            else
            {
                endAtIndex = userPref.getRecordsToDisplay();
            }

            List subList = views.subList(startAtIndex, endAtIndex);
            model.addAttribute("views", subList);
        }

        model.addAttribute("startAtIndex", startAtIndex);
        model.addAttribute("endAtIndex", endAtIndex);
        model.addAttribute("schemas",
                GreenplumWebDAOUtil.getAllSchemas
                        ((String) session.getAttribute("user_key")));

        model.addAttribute("chosenSchema", schema);

        // This will resolve to /WEB-INF/jsp/views.jsp
        return "views";
    }

    @RequestMapping(value = "/views", method = RequestMethod.POST)
    public String performViewAction
            (Model model, HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception
    {
        String schema = null;
        int startAtIndex = 0, endAtIndex = 0;

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

        Result result = new Result();
        List<View> views = null;

        logger.debug("Received request to perform an action on the views");

        String selectedSchema = request.getParameter("selectedSchema");
        logger.debug("selectedSchema = " + selectedSchema);

        if (selectedSchema != null)
        {
            schema = selectedSchema;
        }
        else
        {
            schema = (String)session.getAttribute("schema");
        }

        logger.debug("schema = " + schema);

        ViewDAO viewDAO = GreenplumWebDAOFactory.getViewDAO();
        if (request.getParameter("search") != null)
        {
            views = viewDAO.retrieveViewList
                    (schema,
                    (String)request.getParameter("search"),
                    (String)session.getAttribute("user_key"));

            model.addAttribute("search", (String)request.getParameter("search"));
        }
        else
        {
            String[] tableList  = request.getParameterValues("selected_view[]");
            String   commandStr = request.getParameter("submit_mult");

            logger.debug("tableList = " + Arrays.toString(tableList));
            logger.debug("command = " + commandStr);

            // start actions now if tableList is not null

            if (tableList != null)
            {
                List al = new ArrayList<Result>();
                for (String view: tableList)
                {
                    result = null;
                    result =
                            viewDAO.simpleviewCommand
                                    (schema,
                                    view,
                                    commandStr,
                                    (String)session.getAttribute("user_key"));
                    al.add(result);
                }

                model.addAttribute("arrayresult", al);
            }

            views = viewDAO.retrieveViewList
                    (schema,
                            null,
                            (String)session.getAttribute("user_key"));
        }

        model.addAttribute("records", views.size());
        model.addAttribute("estimatedrecords", views.size());

        UserPref userPref = (UserPref) session.getAttribute("prefs");

        if (views.size() <= userPref.getRecordsToDisplay())
        {
            model.addAttribute("views", views);
        }
        else
        {
            if (request.getParameter("startAtIndex") != null)
            {
                startAtIndex = Integer.parseInt(request.getParameter("startAtIndex"));
            }

            if (request.getParameter("endAtIndex") != null)
            {
                endAtIndex = Integer.parseInt(request.getParameter("endAtIndex"));
                if (endAtIndex > views.size())
                {
                    endAtIndex = views.size();
                }
            }
            else
            {
                endAtIndex = userPref.getRecordsToDisplay();
            }

            List subList = views.subList(startAtIndex, endAtIndex);
            model.addAttribute("views", subList);
        }

        model.addAttribute("startAtIndex", startAtIndex);
        model.addAttribute("endAtIndex", endAtIndex);

        model.addAttribute("schemas",
                GreenplumWebDAOUtil.getAllSchemas
                        ((String) session.getAttribute("user_key")));

        model.addAttribute("chosenSchema", schema);

        // This will resolve to /WEB-INF/jsp/views.jsp
        return "views";
    }
}
