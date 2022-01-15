<%--
  Created by IntelliJ IDEA.
  User: yanxing
  Date: 2021/11/26
  Time: 10:20
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>密码学商城</title>
    <style>
        body {
            text-align: center;
        }

        /* 黑色的顶部栏 */
        .tabBar {
            width: 100%;
            height: 40px;
            background-color: #000000;
            position: relative;
        }

        /* 欢迎用户的字体颜色 */
        .welcome {
            color: white;
            line-height: 40px;
            position: absolute;
            left: 20px;
            top: 0;
        }

        /* 登录按钮 */
        .button1 {
            height: 40px;
            position: absolute;
            top: 0;
            left: 180px;
            background-color: rgb(51, 51, 51);
            border: none;
            color: white;
            font-size: 13px;
        }

        /* 注册按钮 */
        .button2 {
            height: 40px;
            position: absolute;
            top: 0;
            right: 110px;
            background-color: rgb(51, 51, 51);
            border: none;
            color: white;
        }

        .tabBarRight {
            height: 40px;
            width: 250px;
            position: absolute;
            top: 0;
            right: 0px;
            background-color: rgb(51, 51, 51);
            border: none;
            color: white;
        }

        /* 搜索框整体效果 */
        .search {
            position: absolute;
            top: 0px;
            left: 300px;
        }

        /* 输入框 */
        .searchInput {
            border: 1px solid black;
            margin-top: 80px;
            height: 40px;
            width: 400px;
        }

        /* 类别整体框 */
        .category {
            width: 100%;
            height: 130px;
            border: 3px solid black;
            margin-top: 30px;
        }

        /* 手机售卖框 */
        .iphone {
            width: 100%;
            height: 157px;
            border: 3px solid black;
        }

        /* 放大镜图案 */
        .magnifier {
            width: 30px;
            position: absolute;
            left: 4px;
            top: 6px;
            color: white;
        }

        /* 放大镜外侧div */
        .searchBox {
            height: 44px;
            width: 40px;
            background-color: rgb(0, 157, 225);
            position: relative;
            margin-left: 600px;
            margin-top: -44px;
        }

        .svg1 {
            height: 30px;
            margin-right: 920px;
            margin-top: -50px;
        }

        .image {
            position: absolute;
            top: 20px;
            left: 40px;
        }

        .h1 {
            position: absolute;
            left: 250px;
            font-size: 30px;
        }

        .introduction {
            width: 1000px;
            height: 25px;
            position: absolute;
            left: 250px;
            top: 80px;
            text-align: left;
        }

        .price {
            position: absolute;
            right: 160px;
            top: 30px;
            font-size: 20px;
            color: red;
        }

        .pay {
            position: absolute;
            right: 140px;
            top: 80px;
            font-size: 20px;
            color: rgb(211, 216, 213);
        }

        .shuma {
            position: absolute;
            left: 60px;
            top: 35px;
            font-size: 35px;
        }

        .svg1 {
            position: absolute;
            height: 50px;
            left: 130px;
            top: 85px;
        }

        .shipin {
            position: absolute;
            left: 250px;
            top: 35px;
            font-size: 35px;
        }

        .svg2 {
            position: absolute;
            height: 50px;
            left: 330px;
            top: 35px;
        }

        .yiliao {
            position: absolute;
            left: 450px;
            top: 35px;
            font-size: 35px;
        }

        .svg3 {
            position: absolute;
            height: 50px;
            left: 530px;
            top: 35px;
        }

        .jiadian {
            position: absolute;
            left: 650px;
            top: 35px;
            font-size: 35px;
        }

        .svg4 {
            position: absolute;
            height: 50px;
            left: 730px;
            top: 35px;
        }

        .jiaju {
            position: absolute;
            left: 850px;
            top: 35px;
            font-size: 35px;
        }

        .svg5 {
            position: absolute;
            height: 50px;
            left: 930px;
            top: 35px;
        }

        .shuji {
            position: absolute;
            left: 1050px;
            top: 35px;
            font-size: 35px;
        }

        .svg6 {
            position: absolute;
            height: 50px;
            left: 1130px;
            top: 35px;
        }

        .wanju {
            position: absolute;
            left: 1250px;
            top: 35px;
            font-size: 35px;
        }

        .svg7 {
            position: absolute;
            height: 50px;
            left: 1330px;
            top: 35px;
        }

        .muma {
            position: absolute;
            left: 1450px;
            top: 35px;
            font-size: 35px;
        }

        .svg8 {
            position: absolute;
            height: 50px;
            left: 1530px;
            top: 35px;
        }

        .shoppingCart {
            position: absolute;
            left: 52px;
            top: 7px;
            color: dodgerblue;
            font-size: 16px;
        }

        .shoppingCartImg {
            position: absolute;
            left: 10px;
            /*top:1px;*/
            bottom: 2px;
        }

        .myOrders {
            position: absolute;
            left: 165px;
            top: 7px;
            font-size: 16px;
            color: dodgerblue;
        }

        .myOrdersImg {
            position: absolute;
            left: 125px;
            /*top:1px;*/
            bottom: 1px;
        }

        .hr1 {
            position: absolute;
            top: 140px;
            width: 100%;
            /*height: px;*/
            border: 1px solid black;
            /*size: 5px;*/
        }

        .goodsTable {
            position: absolute;
            top: 300px;
            left: 180px;
            text-align: center;
            border: 2px solid black;
        }

        .categoryTable {
            position: absolute;
            top: 160px;
            left: 200px;
            /*border: 1px solid black;*/
        }
    </style>

    <script>
        function addToCart() {
            window.location.href = "${pageContext.request.contextPath}/addToCart?goodID=${good.goodID}";
        }
    </script>
</head>
<body>
<div class="tabBar">
    <div class="welcome">欢迎您! ${username}</div>
    <%--    <div></div>--%>
    <button class="button1">退出登录</button>
    <%--    <button class="button2">注册</button>--%>
    <div class="tabBarRight">
        <div class="shoppingCartImg"><img src="image/icons/gouwuche.svg" alt="" style="height: 40px"></div>
        <a href="${pageContext.request.contextPath}/consumerManage">
            <div class="shoppingCart">买家后台</div>
        </a>
        <div class="myOrdersImg"><img src="image/icons/wodedingdan.svg" alt="" style="height: 36px"></div>
        <a href="${pageContext.request.contextPath}/sellerManage">
            <div class="myOrders">卖家后台</div>
        </a>
    </div>
</div>
<%--${good}--%>
<a href="${pageContext.request.contextPath}/showGoods" style="position:absolute; left: 30px; top: 60px">返回商品列表</a>
<table style="font-size: x-large; margin-left: 100px; margin-top: 50px" border="1px">
    <tr>
        <td>商品名称:</td>
        <td>${good.goodName}</td>
    </tr>
    <tr>
        <td>商品描述:</td>
        <td style="width: 400px">${good.goodDescription}</td>
    </tr>
    <tr>
        <td>商品图片:</td>
        <td><img src="image/goods/${good.goodImageName}" alt="" width="200px" height="200px"></td>
    </tr>
    <tr>
        <td>商品价格:</td>
        <td>￥ ${good.goodPrice}</td>
    </tr>
    <tr>
        <td>店铺名称:</td>
        <td>${good.goodSellerUsername}</td>
    </tr>
</table>
<button onclick="addToCart()"
        style="position: absolute; left: 100px; top:750px; width: 200px; height: 60px; font-size: 20px; background-color: orangered;">
    添加到购物车
</button>
</body>
</html>
