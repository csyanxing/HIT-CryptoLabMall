<%--
  Created by IntelliJ IDEA.
  User: yanxing
  Date: 2021/11/25
  Time: 8:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="http://www.hityx.top/wp-content/uploads/2020/12/cropped-Conan-1-32x32.jpeg">
    <link rel="canonical" href="https://getbootstrap.com/docs/3.4/examples/dashboard/">
    <title>卖家后台-商品列表</title>

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
    <script src="js/jsbn.js"></script>
    <script src="js/RSAUtils.js"></script>
    <script src="js/aeslib.js"></script>
    <script src="js/aes.js"></script>
    <%--下面这个pubKeyMall.js中写入了电商的公钥--%>
    <script src="js/pubKeyMall.js"></script>

    <script>
        function pay(orderID){
            console.log(orderID);
            var symmetricKey = AESGenerateKey(32);
            var encryptedBigIntegerSymKey = RSAEncrypt(rsaPublicKey, symmetricKey);
            var encryptedOrderID = AESEncrypt(symmetricKey, orderID);
            var params = {
                "encryptedBigIntegerSymKey": encryptedBigIntegerSymKey.toString(16),
                "encryptedOrderID": encryptedOrderID
            };
            httpPost("${pageContext.request.contextPath}/consumerPay", params);

            return;
        }
        function httpPost(URL, PARAMS) {
            var temp = document.createElement("form");
            temp.action = URL;
            temp.method = "post";
            temp.style.display = "none";

            for (var x in PARAMS) {
                var opt = document.createElement("textarea");
                opt.name = x;
                opt.value = PARAMS[x];
                temp.appendChild(opt);
            }
            document.body.appendChild(temp);
            temp.submit();
            return temp;
        }
    </script>
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
                <li class="active"><a href="${pageContext.request.contextPath}/consumerOrderManage"
                                      style="font-size: large;">订单管理</a></li>
                <li><a href="${pageContext.request.contextPath}/consumerCertManage" style="font-size: large;">证书信息</a>
                </li>
            </ul>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <table style="align-items: center;" border="3px">
                <h1>订单管理</h1>
                <tr>
                    <td align="center" valign="middle" style="font-size:large; font-weight: bold; width: 100px;">
                        订单编号
                    </td>
                    <td align="center" valign="middle" style="font-size:large; font-weight: bold; width: 100px;">
                        商品名称
                    </td>
                    <td align="center" valign="middle" style="font-size:large; font-weight: bold; width: 100px;">
                        单价
                    </td>
                    <td align="center" valign="middle" style="font-size:large; font-weight: bold; width: 100px;">
                        数量
                    </td>
                    <td align="center" valign="middle" style="font-size:large; font-weight: bold; width: 100px;">
                        订单金额
                    </td>
                    <td align="center" valign="middle" style="font-size:large; font-weight: bold; width: 100px;">
                        卖家ID
                    </td>
                    <td align="center" valign="middle" style="font-size:large; font-weight: bold; width: 100px;">
                        支付状态
                    </td>
                    <td align="center" valign="middle" style="font-size:large; font-weight: bold; width: 100px;">
                        发货状态
                    </td>
                    <td align="center" valign="middle" style="font-size:large; font-weight: bold; width: 100px;">
                        订单完成
                    </td>
                    <td align="center" valign="middle" style="font-size:large; font-weight: bold; width: 100px;">
                        订单时间
                    </td>
                    <td align="center" valign="middle" style="font-size:large; font-weight: bold; width: 100px;">
                        操作
                    </td>
                </tr>
                <c:forEach items="${orderList}" var="aOrder" varStatus="s">
                    <tr>
                        <td align="center" valign="middle"
                            style="font-size:large; font-weight: bold; width: 100px; height: 100px;">
                                ${aOrder.orderID}
                        </td>
                        <td align="center" valign="middle" style="font-size:large; font-weight: bold; width: 100px;">
                                ${aOrder.goodName}
                        </td>
                        <td align="center" valign="middle" style="font-size:large; font-weight: bold; width: 100px;">
                            ￥${aOrder.goodPrice}
                        </td>
                        <td align="center" valign="middle" style="font-size:large; font-weight: bold; width: 100px;">
                                ${aOrder.goodNum}
                        </td>
                        <td align="center" valign="middle"
                            style="font-size:large; font-weight: bold; color: coral; width: 100px;">
                            ￥ ${aOrder.orderMoney}
                        </td>
                        <td align="center" valign="middle" style="font-size:large; font-weight: bold; width: 100px;">
                                ${aOrder.sellerUsername}
                        </td>
                        <td align="center" valign="middle" style="font-size:large; font-weight: bold; width: 100px;">
                            <c:if test="${aOrder.hasPayed}">
                                已支付
                            </c:if>
                            <c:if test="${!aOrder.hasPayed}">
                                未支付
                            </c:if>
                        </td>
                        <td align="center" valign="middle" style="font-size:large; font-weight: bold; width: 100px;">
                            <c:if test="${aOrder.hasOnLoad}">
                                已发货
                            </c:if>
                            <c:if test="${!aOrder.hasOnLoad}">
                                未发货
                            </c:if>
                        </td>
                        <td align="center" valign="middle" style="font-size:large; font-weight: bold; width: 100px;">
                            <c:if test="${aOrder.hasConfirmDelivery}">
                                已完成
                            </c:if>
                            <c:if test="${!aOrder.hasConfirmDelivery}">
                                未完成
                            </c:if>
                        </td>
                        <td align="center" valign="middle" style="font-size:large; font-weight: bold; width: 100px;">
                            <fmt:formatDate value="${aOrder.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>
                        <td align="center" valign="middle" style="font-size:large; font-weight: bold; width: 120px;">
                            <c:if test="${aOrder.hasPayed && !aOrder.hasConfirmDelivery}">
                                <a href="${pageContext.request.contextPath}/consumerConfirmDelivery?orderID=${aOrder.orderID}">
                                    <button>确认收货</button>
                                </a>
                            </c:if>
                            <c:if test="${!aOrder.hasPayed}">
<%--                            <a href="${pageContext.request.contextPath}/consumerPay?orderID=${aOrder.orderID}">--%>
                                <button onclick="pay(${aOrder.orderID})">开始支付</button>
<%--                            </a>--%>
                            </c:if>
                            <c:if test="${aOrder.hasConfirmDelivery}">
                                订单已完成
                            </c:if>

                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</div>

</body>

</html>