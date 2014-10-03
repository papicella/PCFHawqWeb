package pivotal.au.fe.greenplumweb.controller;

import java.sql.Connection;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.Model;
import pivotal.au.fe.greenplumweb.beans.Login;
import pivotal.au.fe.greenplumweb.main.UserPref;
import pivotal.au.fe.greenplumweb.utils.*;
import pivotal.au.fe.greenplumweb.utils.AdminUtil;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.LinkedList;

@Controller
public class LoginController 
{
	protected static Logger logger = Logger.getLogger("controller");
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, HttpSession session) throws Exception
    {
    	logger.debug("Received request to show login page");

        String vcapServices = null;
        vcapServices = System.getenv().get("VCAP_SERVICES");

        if (vcapServices != null)
        {
            if (vcapServices.length() > 0)
            {
                try
                {
                    // we have a bound application to HAWQ possibly get connect details.
                    logger.debug("PCFHawqWeb bound to PHD service...");

                    JSONObject HawqXDURI = (JSONObject) getPCFObject(vcapServices.trim());
                    String username= "", password = "";

                    String pcfurl = (String) HawqXDURI.get("uri");

                    int qtnMarkStartPos = pcfurl.indexOf("?");
                    String s = pcfurl.substring(qtnMarkStartPos + 1);

                    // get user
                    int userStartPos = s.indexOf("=");
                    username = s.substring(userStartPos + 1, s.indexOf("&"));

                    // get password
                    String s2 = s.substring(s.indexOf("&") + 1);

                    int passwordStartPos = s2.indexOf("=");
                    password = s2.substring(passwordStartPos + 1);

                    logger.debug("\n---- Ready to Connect to PHD service ----");
                    logger.debug("fulljdbcurl = " + pcfurl);
                    logger.debug("username = " + username);
                    logger.debug("password = " + password);

                    logger.debug("----");

                    ConnectionManager cm = ConnectionManager.getInstance();
                    Connection conn;

                    conn = AdminUtil.getNewConnection(pcfurl);

                    GreenplumJDBCConnection newConn =
                            new GreenplumJDBCConnection
                                    (conn,
                                     pcfurl,
                                     new java.util.Date().toString(),
                                     username);

                    cm.addConnection(newConn, session.getId());

                    session.setAttribute("user_key", session.getId());
                    session.setAttribute("user", username);
                    session.setAttribute("schema", "public");
                    session.setAttribute("url", pcfurl);
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
        }

    	// Create new QueryWindow and add to model
    	// This is the formBackingObject
    	model.addAttribute("loginAttribute", new Login());
    	// This will resolve to /WEB-INF/jsp/loginpage.jsp
    	return "loginpage";
    }

	@RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login
    (@ModelAttribute("loginAttribute") Login loginAttribute, 
     Model model, 
     HttpSession session) throws Exception
    {
    	logger.debug("Received request to login");
    	ConnectionManager cm = ConnectionManager.getInstance();
    	Connection conn;

        logger.debug("url {" + loginAttribute.getUrl() + "}");
        logger.debug("user {" + loginAttribute.getUsername() + "}");
        //logger.debug("password {" + loginAttribute.getPassword() + "}");

    	try
    	{
    		conn = AdminUtil.getNewConnection
                    (loginAttribute.getUrl(),
                            loginAttribute.getUsername(),
                            loginAttribute.getPassword());
    		
	    	GreenplumJDBCConnection newConn =
	    			new GreenplumJDBCConnection
	    			  (conn,
	    			   loginAttribute.getUrl(),
	    			   new java.util.Date().toString(),
	    			   loginAttribute.getUsername().toUpperCase());
	    	
	    	cm.addConnection(newConn, session.getId());
	    	
	    	session.setAttribute("user_key", session.getId());
            session.setAttribute("user", loginAttribute.getUsername());
	    	session.setAttribute("schema", "public");
	    	session.setAttribute("url", loginAttribute.getUrl());
	    	session.setAttribute("prefs", new UserPref());
            session.setAttribute("history", new LinkedList());
            session.setAttribute("connectedAt", new java.util.Date().toString());

	    	Map<String, String> schemaMap = AdminUtil.getSchemaMap();

            schemaMap = QueryUtil.populateSchemaMap
                    (conn, schemaMap, "public");

            session.setAttribute("schemaMap", schemaMap);
	    	
	    	logger.info(loginAttribute);
	    	
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

    private Object getPCFObject (String jsonString)
    {
        Object obj = JSONValue.parse(jsonString);
        JSONObject initialJSON = (JSONObject) obj;

        Object phdObject = initialJSON.get("p-hd");
        JSONArray phdObjectArray = (JSONArray) phdObject;

        //System.out.println(phdObjectArray.get(0));

        JSONObject pHdJson = (JSONObject) phdObjectArray.get(0);

        //System.out.println(pHdJson.get("credentials"));

        Object credObject = pHdJson.get("credentials");
        JSONObject JSONCred = (JSONObject) credObject;

        logger.debug(JSONCred.get("hawq"));

        Object uriObject = JSONCred.get("hawq");

        return uriObject;
    }
}
