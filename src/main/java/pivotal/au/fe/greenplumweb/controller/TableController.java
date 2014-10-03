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
import pivotal.au.fe.greenplumweb.dao.tables.Table;
import pivotal.au.fe.greenplumweb.dao.tables.TableDAO;
import pivotal.au.fe.greenplumweb.main.Result;
import pivotal.au.fe.greenplumweb.main.UserPref;
import pivotal.au.fe.greenplumweb.utils.AdminUtil;

@Controller
public class TableController
{
    protected static Logger logger = Logger.getLogger("controller");

    @RequestMapping(value = "/tables", method = RequestMethod.GET)
    public String showTables
            (Model model, HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception
    {
        int startAtIndex = 0, endAtIndex = 0;
        javax.servlet.jsp.jstl.sql.Result dataLocationResult, memoryUsageResult, expirationEvictionResult,
                allTableInfoResult, tableStructure, tableTriggersResult = null;
        String schema = null;

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

        logger.debug("Received request to show tables");

        TableDAO tableDAO = GreenplumWebDAOFactory.getTableDAO();
        Result result = new Result();

        String tabAction = request.getParameter("tabAction");
        String selectedSchema = request.getParameter("selectedSchema");
        logger.debug("selectedSchema = " + selectedSchema);

        String type = request.getParameter("type");
        logger.debug("table type = " + type);

        if (selectedSchema != null)
        {
            schema = selectedSchema;
        }
        else
        {
            schema = (String) session.getAttribute("schema");
        }

        logger.debug("schema = " + schema);

        if (tabAction != null)
        {
            logger.debug("tabAction = " + tabAction);
            result = null;

            if (tabAction.equalsIgnoreCase("STRUCTURE"))
            {
                tableStructure =
                        tableDAO.getTableStructure
                                (schema,
                                (String)request.getParameter("tabName"),
                                (String)session.getAttribute("user_key"));

                model.addAttribute("tableStructure", tableStructure);
                model.addAttribute("tablename", (String)request.getParameter("tabName"));
            }
            else
            {
                result =
                        tableDAO.simpletableCommand
                                (schema,
                                (String)request.getParameter("tabName"),
                                tabAction,
                                (String)session.getAttribute("user_key"));
                model.addAttribute("result", result);
            }
        }

        List<Table> tbls = tableDAO.retrieveTableList
                (schema,
                 null,
                 type,
                 (String)session.getAttribute("user_key"));


        model.addAttribute("records", tbls.size());
        model.addAttribute("estimatedrecords", tbls.size());

        UserPref userPref = (UserPref) session.getAttribute("prefs");

        if (tbls.size() <= userPref.getRecordsToDisplay())
        {
            model.addAttribute("tables", tbls);
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
                if (endAtIndex > tbls.size())
                {
                    endAtIndex = tbls.size();
                }
            }
            else
            {
                endAtIndex = userPref.getRecordsToDisplay();
            }

            List subList = tbls.subList(startAtIndex, endAtIndex);
            model.addAttribute("tables", subList);
        }

        model.addAttribute("startAtIndex", startAtIndex);
        model.addAttribute("endAtIndex", endAtIndex);
        model.addAttribute("type", type);
        model.addAttribute("schemas",
                GreenplumWebDAOUtil.getAllSchemas
                        ((String) session.getAttribute("user_key")));

        model.addAttribute("chosenSchema", schema);

        // This will resolve to /WEB-INF/jsp/tables.jsp
        return "tables";
    }

    @RequestMapping(value = "/tables", method = RequestMethod.POST)
    public String performTableAction
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

        int startAtIndex = 0, endAtIndex = 0;
        String schema = null;
        Result result = new Result();
        List<Table> tbls = null;

        logger.debug("Received request to perform an action on the tables");

        TableDAO tableDAO = GreenplumWebDAOFactory.getTableDAO();

        String selectedSchema = request.getParameter("selectedSchema");
        logger.debug("selectedSchema = " + selectedSchema);

        String type = request.getParameter("type");
        logger.debug("table type = " + type);

        if (selectedSchema != null)
        {
            schema = selectedSchema;
        }
        else
        {
            schema = (String) session.getAttribute("schema");
        }

        logger.debug("schema = " + schema);

        if (request.getParameter("search") != null)
        {
            tbls = tableDAO.retrieveTableList
                    (schema,
                    (String)request.getParameter("search"),
                    type,
                    (String)session.getAttribute("user_key"));

            model.addAttribute("search", (String)request.getParameter("search"));
        }
        else
        {
            String[] tableList  = request.getParameterValues("selected_tbl[]");
            String   commandStr = request.getParameter("submit_mult");

            logger.debug("tableList = " + Arrays.toString(tableList));
            logger.debug("command = " + commandStr);

            // start actions now if tableList is not null

            if (tableList != null)
            {
                List al = new ArrayList<Result>();
                for (String table: tableList)
                {
                    result = null;
                    result = tableDAO.simpletableCommand
                                    (schema,
                                    table,
                                    commandStr,
                                    (String)session.getAttribute("user_key"));

                    al.add(result);
                }

                model.addAttribute("arrayresult", al);
            }

            tbls = tableDAO.retrieveTableList
                    (schema,
                    null,
                    type,
                    (String)session.getAttribute("user_key"));
        }

        model.addAttribute("records", tbls.size());
        model.addAttribute("estimatedrecords", tbls.size());

        UserPref userPref = (UserPref) session.getAttribute("prefs");

        if (tbls.size() <= userPref.getRecordsToDisplay())
        {
            model.addAttribute("tables", tbls);
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
                if (endAtIndex > tbls.size())
                {
                    endAtIndex = tbls.size();
                }
            }
            else
            {
                endAtIndex = userPref.getRecordsToDisplay();

            }

            List subList = tbls.subList(startAtIndex, endAtIndex);
            model.addAttribute("tables", subList);
        }

        model.addAttribute("startAtIndex", startAtIndex);
        model.addAttribute("endAtIndex", endAtIndex);
        model.addAttribute("type", type);
        model.addAttribute("schemas",
                GreenplumWebDAOUtil.getAllSchemas
                        ((String) session.getAttribute("user_key")));

        model.addAttribute("chosenSchema", schema);

        // This will resolve to /WEB-INF/jsp/tables.jsp
        return "tables";
    }
}

