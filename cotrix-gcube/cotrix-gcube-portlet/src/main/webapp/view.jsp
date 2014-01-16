<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<portlet:defineObjects />

<script>
function resizeCotrixContainer(){
var cotrixContainer = document.getElementById("cotrixContainer");
var height = window.innerHeight - cotrixContainer.offsetTop;
cotrixContainer.style.height = height+"px";
};

window.onresize=resizeCotrixContainer;
</script>

<iframe id="cotrixContainer" src="http://figisapps.fao.org/cotrix/?sessionId=<%= session.getId() %>" style="width:100%;border:none" onload="resizeCotrixContainer()"></iframe>

<!--<div id="cotrixContainer" style="width:100%;border:none;background-color:red" onload="resizeCotrixContainer()">THIS IS A TEST</div>--> 

