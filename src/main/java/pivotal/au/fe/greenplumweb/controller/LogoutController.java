package pivotal.au.fe.greenplumweb.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pivotal.au.fe.greenplumweb.beans.Login;
import pivotal.au.fe.greenplumweb.utils.ConnectionManager;

@Controller
public class LogoutController 
{
	protected static Logger logger = Logger.getLogger("controller");

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout
      (Model model, HttpSession session, HttpServletResponse response, HttpServletRequest request) throws Exception 
    {
    	logger.debug("Received request to logout of PivotalHD*Web");

    	// remove connection from list
    	ConnectionManager cm = ConnectionManager.getInstance();
    	cm.removeConnection(session.getId());
    	
    	session.invalidate();

        model.addAttribute("loginAttribute", new Login());
        // This will resolve to /WEB-INF/jsp/loginpage.jsp
        return "loginpage";

    	//response.sendRedirect(request.getContextPath() + "/PCFHawq-Web/login");
    	//return null;
    }
}
