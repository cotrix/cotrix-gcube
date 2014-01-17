<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page import="org.cotrix.gcube.portlet.CotrixUrlProvider" %>

<portlet:defineObjects />

<script>
function resizeCotrixContainer(){
var cotrixContainer = document.getElementById("cotrixContainer");
var height = window.innerHeight - cotrixContainer.offsetTop;
cotrixContainer.style.height = height+"px";
};

window.onresize=resizeCotrixContainer;
</script>

<iframe id="cotrixContainer" src="<%= CotrixUrlProvider.getCotrixUrl(session) %>" style="width:100%;border:none" onload="resizeCotrixContainer()"></iframe>