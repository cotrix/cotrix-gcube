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

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ServiceServlet extends HttpServlet {

	private static final long serialVersionUID = 4766451165326531972L;

	/** 
	 * {@inheritDoc}
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String responseBody = UserSessionProvider.getUserSession(session);
		response.setStatus(HttpServletResponse.SC_OK);
		response.getOutputStream().write(responseBody.getBytes());
		response.getOutputStream().close();
		response.flushBuffer();
	}
	
	

}
