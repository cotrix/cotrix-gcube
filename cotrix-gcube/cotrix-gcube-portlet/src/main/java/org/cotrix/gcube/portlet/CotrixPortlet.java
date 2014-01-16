/**
 * 
 */
package org.cotrix.gcube.portlet;

import java.io.IOException;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.gcube.portal.custom.scopemanager.scopehelper.ScopeHelper;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CotrixPortlet extends GenericPortlet {

	/**
	 * 
	 */
	public static final String VIEW_JSP = "/view.jsp";

	/**
	 * @param request .
	 * @param response .
	 * @throws IOException .
	 * @throws PortletException .
	 */
	public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		System.out.println("doView");
		
		System.out.println("ScopeHelper.setContext");
		ScopeHelper.setContext(request);

		System.out.println("getRequestedSessionId "+request.getRequestedSessionId());
		System.out.println("request.getPortletSession().getId() "+request.getPortletSession().getId());
		
		
		System.out.println("dispatching");
		PortletRequestDispatcher rd = getPortletContext().getRequestDispatcher(VIEW_JSP);
		rd.include(request,response);
	}
}