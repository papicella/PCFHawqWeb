package pivotal.au.fe.greenplumweb.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pivotal.au.fe.greenplumweb.reports.GenericQuery;
import pivotal.au.fe.greenplumweb.reports.ParameterQuery;
import pivotal.au.fe.greenplumweb.reports.Query;
import pivotal.au.fe.greenplumweb.reports.QueryBeanReader;
import pivotal.au.fe.greenplumweb.reports.QueryList;
import pivotal.au.fe.greenplumweb.utils.AdminUtil;
import pivotal.au.fe.greenplumweb.utils.ConnectionManager;

@Controller
public class ReportsController
{
    protected static Logger logger = Logger.getLogger("controller");

    @RequestMapping(value = "/displayqueryreports", method = RequestMethod.GET)
    public String showQueryReports
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

        logger.debug("Received request to show all query reports");

        QueryBeanReader reader = QueryBeanReader.getInstance();

        List<String> qlBeanNames = Arrays.asList(reader.getQueryListBeans());

        List queries = new ArrayList();
        List headerNames = new ArrayList();
        int i = 0;

        for (String beanName: qlBeanNames)
        {
            i++;
            QueryList ql = reader.getQueryListBean(beanName);
            headerNames.add(ql.getDescription());
            queries.add(ql.getQueryList());
        }

        model.addAttribute("queries", queries);
        model.addAttribute("headerNames", headerNames);

        // This will resolve to /WEB-INF/jsp/reports.jsp
        return "reports";
    }

    @RequestMapping(value = "/executequeryreport", method = RequestMethod.GET)
    public String executeQuery
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

        logger.debug("Received request to execute a query");

        javax.servlet.jsp.jstl.sql.Result queryResult = null;
        QueryBeanReader reader = QueryBeanReader.getInstance();
        ConnectionManager cm = ConnectionManager.getInstance();

        String beanId = request.getParameter("beanId");

        if (beanId != null)
        {

            Query qb = reader.getQueryBean(beanId);

            if (qb instanceof GenericQuery)
            {
                logger.debug("Generic Query will be run");
                GenericQuery genericQuery = (GenericQuery) qb;

                queryResult =
                        genericQuery.invoke(cm.getConnection((String)session.getAttribute("user_key")));
                model.addAttribute("queryResults", queryResult);
                model.addAttribute("queryDescription", genericQuery.getQueryDescription());
                model.addAttribute("querySQL", genericQuery.getQuery().trim());
                model.addAttribute("beanId", beanId);
            }
            else if (qb instanceof ParameterQuery)
            {
                ParameterQuery paramQuery = (ParameterQuery) qb;
                model.addAttribute("queryDescription", paramQuery.getQueryDescription());
                model.addAttribute("querySQL", paramQuery.getQuery().trim());
                model.addAttribute("beanId", beanId);
                model.addAttribute("paramMap", paramQuery.getParamMap());

                if (request.getParameter("pSubmit") != null)
                {
                    // execute pressed, get param values
                    Map paramValues = new HashMap();
                    Set<String> keys = paramQuery.getParamMap().keySet();
                    for (String param: keys)
                    {
                        paramValues.put(param, (String) request.getParameter(param));
                    }

                    paramQuery.setParamValues(paramValues);
                    queryResult =
                            paramQuery.invoke(cm.getConnection((String)session.getAttribute("user_key")));
                    request.setAttribute("queryResults", queryResult);
                }
            }
            else
            {
                // do nothing here
            }
        }

        // This will resolve to /WEB-INF/jsp/displayquery.jsp
        return "displayquery";
    }

}

