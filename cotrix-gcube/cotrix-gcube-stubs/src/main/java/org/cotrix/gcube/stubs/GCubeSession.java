/**
 * 
 */
package org.cotrix.gcube.stubs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GCubeSession {
	
	@XmlElement
	protected GCubeUser user;
	
	@XmlElement
	protected String scope;
	
	public GCubeSession(){}

	/**
	 * @return the user
	 */
	public GCubeUser getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(GCubeUser user) {
		this.user = user;
	}

	/**
	 * @return the scope
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * @param scope the scope to set
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserSession [user=");
		builder.append(user);
		builder.append(", scope=");
		builder.append(scope);
		builder.append("]");
		return builder.toString();
	}
}
