/**
 * 
 */
package org.cotrix.gcube.portlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.cotrix.gcube.stubs.PortalSession;
import org.cotrix.gcube.stubs.SessionContext;
import org.gcube.portal.custom.scopemanager.scopehelper.ScopeHelper;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ServiceServlet extends HttpServlet {

	private static final long serialVersionUID = 4766451165326531972L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		HttpSession session = request.getSession();
		
		String id = session.getId();
		String username = (String) session.getAttribute(ScopeHelper.USERNAME_ATTRIBUTE);
		
		PortalSession pSession = SessionProvider.sessionFor(id,username);
		
		String responseBody = SessionContext.serialize(pSession);
		
		response.setStatus(HttpServletResponse.SC_OK);
		
		response.getOutputStream().write(responseBody.getBytes());
		
		response.getOutputStream().close();
		
		response.flushBuffer();
	}
	
	

}
