<%--
  Created by IntelliJ IDEA.
  User: yanxing
  Date: 2021/10/16
  Time: 10:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<a href="${pageContext.request.contextPath}">返回主页</a>
<a href="${pageContext.request.contextPath}/addGood">添加商品</a>
<a href="${pageContext.request.contextPath}/showGoods">查看所有商品</a>
<form action="${pageContext.request.contextPath}/addGood" method="post">
    <table border="1px">
        <tr>
            <td><label>商品名称</label></td>
            <td><input type="text" name="goodName"></td>
        </tr>
        <tr>
            <td><label>商品价格</label></td>
            <td><input type="text" name="goodPrice"></td>
        </tr>
        <tr>
            <td><label>商品描述</label></td>
            <td><input type="text" name="goodDescription"></td>
        </tr>
        <tr><td><input type="submit" value="提交"></td></tr>
    </table>
</form>
</body>
</html>
