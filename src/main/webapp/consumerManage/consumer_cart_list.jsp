<%--
  Created by IntelliJ IDEA.
  User: yanxing
  Date: 2021/11/26
  Time: 21:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="http://www.hityx.top/wp-content/uploads/2020/12/cropped-Conan-1-32x32.jpeg">
    <link rel="canonical" href="https://getbootstrap.com/docs/3.4/examples/dashboard/">
    <title>买家后台-购物车</title>

    <!-- Bootstrap core CSS -->
    <link href="https://cdn.jsdelivr.net/npm/@bootcss/v3.bootcss.com@1.0.17/dist/css/bootstrap.min.css"
          rel="stylesheet">

    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <link href="https://cdn.jsdelivr.net/npm/@bootcss/v3.bootcss.com@1.0.17/assets/css/ie10-viewport-bug-workaround.css"
          rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="https://cdn.jsdelivr.net/npm/@bootcss/v3.bootcss.com@1.0.17/examples/dashboard/dashboard.css"
          rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]>
    <script src="https://cdn.jsdelivr.net/npm/@bootcss/v3.bootcss.com@1.0.17/assets/js/ie8-responsive-file-warning.js"></script>
    <![endif]-->
    <script
            src="https://cdn.jsdelivr.net/npm/@bootcss/v3.bootcss.com@1.0.17/assets/js/ie-emulation-modes-warning.js"></script>
</head>

<body>
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand">买家后台-欢迎您，${consumerUsername}!</a>
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">返回主页</a>
            <a class="navbar-brand" href="${pageContext.request.contextPath}/logout">退出登录</a>
        </div>
    </div>
</nav>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
            <ul class="nav nav-sidebar">
                <li class="active"><a href="${pageContext.request.contextPath}/consumerCart" style="font-size: large;">购物车</a></li>
                <li><a href="${pageContext.request.contextPath}/consumerOrderManage" style="font-size: large;">订单管理</a></li>
                <li><a href="${pageContext.request.contextPath}/consumerCertManage" style="font-size: large;">证书信息</a></li>
            </ul>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <table style="align-items: center;" border="3px">
                <tr>
                    <td align="center" valign="middle" style="font-size:large; font-weight: bold; width: 90px;">
                        商品名称
                    </td>
                    <td align="center" valign="middle" style="font-size:large; font-weight: bold; width: 100px;">
                        商品图片
                    </td>
                    <td align="center" valign="middle" style="font-size:large; font-weight: bold; width: 700px;">
                        商品描述
                    </td>
                    <td align="center" valign="middle" style="font-size:large; font-weight: bold; width: 100px;">
                        商品价格
                    </td>
                    <td align="center" valign="middle" style="font-size:large; font-weight: bold; width: 100px;">
                        操作
                    </td>
                </tr>
                <c:forEach items="${goodList}" var="aGood" varStatus="s">
                    <tr>
                        <td align="center" valign="middle" style="font-size:large; font-weight: bold; width: 90px;">
                                ${aGood.goodName}
                        </td>
                        <td align="center" valign="middle"
                            style="font-size:large; font-weight: bold; width: 100px; height: 100px;">
                            <img src="image/goods/${aGood.goodImageName}"
                                 style="width: 80px; height: 80px;">
                        </td>
                        <td align="center" valign="middle" style="font-size:large; font-weight: bold; width: 700px;">
                                ${aGood.goodDescription}
                        </td>
                        <td align="center" valign="middle"
                            style="font-size:large; font-weight: bold; color: coral; width: 100px;">
                            ￥${aGood.goodPrice}
                        </td>
                        <td align="center" valign="middle" style="font-size:large; font-weight: bold; width: 100px;">
                            <a href="${pageContext.request.contextPath}/consumerGenerateOrder?goodID=${aGood.goodID}"><button>结算</button></a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</div>

</body>

</html>