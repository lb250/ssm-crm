<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path=request.getContextPath();
	String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<base href="<%=basePath%>">
<head>
	<meta charset="UTF-8">
</head>
<body>
<script type="text/javascript">
	window.location.href = "settings/qx/user/toLogin.do";
</script>
</body>
</html>