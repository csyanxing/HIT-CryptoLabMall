<%--
  Created by IntelliJ IDEA.
  User: yanxing
  Date: 2021/11/25
  Time: 8:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
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
    <title>卖家后台-添加商品</title>
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
    <script>
        function submit() {
            var goodName = goodForm.goodName.value;
            var goodDis = goodForm.goodName.value;
            var goodPrice = goodForm.goodName.value;
            var goodOnSell = goodForm.goodName.value;
            var symmetricKey = AESGenerateKey(32);
            console.log("对称密钥:", symmetricKey);
            var encryptedGoodName = AESEncrypt(symmetricKey, goodName);
            var encryptedGoodDis = AESEncrypt(symmetricKey, goodDis);
            var encryptedGoodPrice = AESEncrypt(symmetricKey, goodPrice);
            var encryptedGoodOnSell = AESEncrypt(symmetricKey, goodOnSell);
            var encryptedBigIntegerSymKey = RSAEncrypt(rsaPublicKey, symmetricKey);
            console.log("加密的对称密钥:", encryptedBigIntegerSymKey);
            var params = {
                "encryptedBigIntegerSymKey": encryptedBigIntegerSymKey.toString(16),
                "encryptedGoodName": encryptedGoodName,
                "encryptedGoodDis": encryptedGoodDis,
                "encryptedGoodOnSell": encryptedGoodOnSell,
                "encryptedGoodPrice": encryptedGoodPrice
            };
            console.log("即将提交post请求...");
            console.log("参数为：", params);
            httpPost("${pageContext.request.contextPath}/sellerAddGood", params);
        }

        function httpPost(URL, PARAMS) {
            var temp = document.createElement("form");
            temp.action = URL;
            temp.method = "post";
            temp.style.display = "none";
            temp.enctype = "multipart/form-data";
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

        function addGoodSubmit() {
            var goodName = addGoodForm.goodName.value;
            var goodDescription = addGoodForm.goodDescription.value;
            if (goodName.length <= 0) {
                window.alert("商品名称不能为空！");
                return;
            }
            if (goodName.length > 10) {
                window.alert("商品名称不能超过10字符！");
                return;
            }
            if (goodDescription.length <= 0) {
                window.alert("商品描述不能为空！");
                return;
            }
            if (addGoodForm.goodPic.value.length <= 0) {
                window.alert("请选择商品图片！");
                return;
            }
            var e = document.createElement("textarea");
            e.name = "goodOnSell";
            e.value = addGoodForm.goodOnSell.checked;
            e.style.display = "none";
            addGoodForm.appendChild(e);
            addGoodForm.submit();
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
            <a class="navbar-brand">卖家后台-欢迎您，${sellerUsername}!</a>
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">返回主页</a>
            <a class="navbar-brand" href="${pageContext.request.contextPath}/logout">退出登录</a>
        </div>
    </div>
</nav>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
            <ul class="nav nav-sidebar">
                <li><a href="${pageContext.request.contextPath}/sellerGoodList" style="font-size: large;">商品列表</a></li>
                <li class="active"><a href="${pageContext.request.contextPath}/sellerAddGood" style="font-size: large;">添加商品</a>
                </li>
                <li><a href="${pageContext.request.contextPath}/sellerOrderManage" style="font-size: large;">订单管理</a>
                </li>
                <li><a href="${pageContext.request.contextPath}/sellerBankManage" style="font-size: large;">银行信息</a>
                </li>
            </ul>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <form action="${pageContext.request.contextPath}/sellerAddGood" method="post" enctype="multipart/form-data"
                  id="addGoodForm">
                <h1>添加商品</h1>
                <br>
                <table style="align-items: center; font-weight: bold;" border="3px">
                    <tr>
                        <td style="font-size: 20px; width: 150px;" align="center" valign="middle">商品名称</td>
                        <td align="center" valign="middle">
                            <textarea style="width: 600px; height: 50px;" id="goodName" name="goodName"></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td style="font-size: 20px; width: 150px;" align="center" valign="middle">商品描述</td>
                        <td align="center" valign="middle">
                            <textarea style="width: 600px; height: 150px;" id="goodDescription"
                                      name="goodDescription"></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td style="font-size: 20px;" align="center" valign="middle">商品图片</td>
                        <td align="center" valign="middle" style="width: 100px; height: 200px;">
                            <input type="file" name="goodPic" id="goodPic">
                        </td>
                    </tr>
                    <tr style="height: 70px;">
                        <td style="font-size: 20px;" align="center" valign="middle">商品价格</td>
                        <td>
                            <input type="Number" step="0.01" value="100.00" name="price" id="goodPrice" min="0.0"
                                   style="margin-left: 20px;">
                        </td>
                    </tr>
                    <tr style="height: 70px;">
                        <td style="font-size: 20px;" align="center" valign="middle">是否上架</td>
                        <td style="font-size: 20px;">
                            <input type="checkbox" name="goodOnSell" id="goodOnSell"
                                   style="margin-left: 20px;">上架
                        </td>
                    </tr>
                </table>
                <br>
                <input type="button" value="提交"
                       style="width: 110px; height: 45px; font-size: 20px; font-weight: bold; margin-left: 160px;"
                       onclick="addGoodSubmit()">
            </form>
        </div>
    </div>
</div>
</body>
</html>