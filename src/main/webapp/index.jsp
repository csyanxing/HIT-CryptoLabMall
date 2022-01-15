<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>密码学商城</title>
</head>
<body style="margin-left: 30px;">
<br>
<hr style="width: 350px;" align="left">
<h1>${msg}</h1>
<a href="hello-servlet">Welcome!</a>
<h1>欢迎来到密码学商城！</h1>
请选择操作: <br>
<hr style="width: 350px; " align="left">
<%--EL表达式--%>
<%--<a href="${pageContext.request.contextPath}/addGood">添加商品</a>--%>
>  <a href="${pageContext.request.contextPath}/showGoods" style="font-size: 20px">商品列表</a>
<hr style="width: 350px;" align="left">
<table style="font-size: 20px">
    <tr>
        <td>
            卖家操作：<br>
            > <a href="${pageContext.request.contextPath}/sellerRegister">卖家注册</a><br>
            > <a href="${pageContext.request.contextPath}/sellerLogin">卖家登录</a><br>
            > <a href="${pageContext.request.contextPath}/sellerManage">卖家后台</a><br>
        </td>
        <td style="border-right: 1px black solid">&nbsp;&nbsp;&nbsp;</td>
        <td>&nbsp;&nbsp;&nbsp;</td>
        <td>
            买家操作：<br>
            > <a href="${pageContext.request.contextPath}/consumerRegister">买家注册</a><br>
            > <a href="${pageContext.request.contextPath}/consumerLogin">买家登录</a><br>
            > <a href="${pageContext.request.contextPath}/consumerCart">买家后台</a><br>
        </td>
    </tr>
</table>
<hr style="width: 350px;" align="left">

<%--卖家操作：<br>--%>
<%--> <a href="${pageContext.request.contextPath}/sellerRegister">卖家注册</a><br>--%>
<%--> <a href="${pageContext.request.contextPath}/sellerLogin">卖家登录</a><br>--%>
<%--> <a href="${pageContext.request.contextPath}/sellerManage">卖家后台</a><br>--%>
<%--<hr>--%>
<%----%>
<%--买家操作：<br>--%>
<%--> <a href="${pageContext.request.contextPath}/consumerRegister">买家注册</a><br>--%>
<%--> <a href="${pageContext.request.contextPath}/consumerLogin">买家登录</a><br>--%>
<%--> <a href="${pageContext.request.contextPath}/consumerCart">买家后台</a><br>--%>
<%--<img src="temp/uploadTemp/94df3351-deda-49e2-bbb0-d937ccbf2d79.jpeg" alt="">--%>
<%--<hr>--%>
<div>
    <a href="${pageContext.request.contextPath}/downloadSellerCert">下载电商证书</a>
    <br>
    <a href="${pageContext.request.contextPath}/verifyCert/vrfyCertificate.jsp">验证证书</a>
</div>
<hr style="width: 350px;" align="left">


</body>
</html>