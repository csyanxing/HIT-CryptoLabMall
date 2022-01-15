<%--
  Created by IntelliJ IDEA.
  User: yanxing
  Date: 2021/11/24
  Time: 22:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script>
        window.alert('登录成功！点击跳转到主页。');
        window.location.href = "${pageContext.request.contextPath}/home";
    </script>
</head>
<body>

</body>
</html>
