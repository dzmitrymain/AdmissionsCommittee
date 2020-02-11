<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<%@include file="jspf/head.jspf" %>
<body>
<%@include file="jspf/header.jspf" %>

<div id="site_content">

    <div id="banner"></div>
    <%@include file="jspf/side_menu.jspf" %>

    <div id="content">

        <h3>Something_went_wrong</h3>
        <c:if test="${not empty error}">
            <p style="margin: unset;padding: 0% 10px 0% 0%">${error}</p>
            <c:remove var="error" scope="session"/></c:if>
    </div>

</div>

<%@ include file="jspf/footer.jspf" %>

</body>
</html>