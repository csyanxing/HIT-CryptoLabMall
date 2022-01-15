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
    <title>买家后台-银行信息</title>

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
                <li><a href="${pageContext.request.contextPath}/consumerCart" style="font-size: large;">购物车</a></li>
                <li><a href="${pageContext.request.contextPath}/consumerOrderManage" style="font-size: large;">订单管理</a></li>
                <li class="active"><a href="${pageContext.request.contextPath}/consumerBankManage" style="font-size: large;">银行信息</a></li>
            </ul>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

            <h1>银行信息</h1>
            <br>
            <table style="align-items: center; font-weight: bold;" border="3px">
                <tr>
                    <td style="font-size: 20px; width: 150px;" align="center" valign="middle">银行账号</td>
                    <td align="center" valign="middle" style="width: 300px;">${consumerBankCardNum}</td>
                </tr>
                <tr>
                    <td style="font-size: 20px; width: 150px;" align="center" valign="middle">账户余额</td>
                    <td align="center" valign="middle" style="width: 300px;">￥${consumerBankBalance}</td>
                </tr>
            </table>
            <br>
            <hr>
            <form action="${pageContext.request.contextPath}/consumerBankManage" method="post">
                <table style="align-items: center; font-weight: bold;" border="3px">
                    <h1>银行充值</h1>
                    <tr>
                        <td style="font-size: 20px; width: 150px;" align="center" valign="middle">充值金额</td>
                        <td align="center" valign="middle" style="width: 300px;">
                            <input type="Number" value="100" id="deltaBalance" name="deltaBalance"
                                   step="1">
                        </td>
                    </tr>
                </table>

                <br>
                <input type="submit" value="充值"
                       style="width: 110px; height: 45px; font-size: 20px; font-weight: bold; margin-left: 160px;">
            </form>
        </div>
    </div>
</div>

</body>

</html>