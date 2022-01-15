<%--
  Created by IntelliJ IDEA.
  User: yanxing
  Date: 2021/10/16
  Time: 14:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<a href="${pageContext.request.contextPath}">返回主页</a>
<a href="${pageContext.request.contextPath}/addGood">添加商品</a>
<a href="${pageContext.request.contextPath}/showGoods">查看所有商品</a>
<h2>商品列表</h2>
<hr>
<table border="1px">
    <tr>
        <td>商品名称</td>
        <td>商品价格</td>
        <td>商品描述</td>
    </tr>

    <%--JSTL标签--%>
    <c:forEach items="${goodList}" var="aGood" varStatus="s">
        <tr>
            <td>${aGood.name}</td>
            <td>${aGood.price}</td>
            <td>${aGood.description}</td>
        </tr>
    </c:forEach>

</table>


</body>
</html>
