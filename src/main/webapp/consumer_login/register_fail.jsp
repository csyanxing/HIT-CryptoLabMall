<%--
  Created by IntelliJ IDEA.
  User: yanxing
  Date: 2021/11/24
  Time: 21:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script>
        window.alert('注册失败！失败原因：${failReason}');
        window.location.href = "${pageContext.request.contextPath}/consumerRegister";
    </script>
</head>
<body>

</body>
</html>
