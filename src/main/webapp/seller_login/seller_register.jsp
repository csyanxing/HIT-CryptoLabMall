<%--
  Created by IntelliJ IDEA.
  User: yanxing
  Date: 2021/10/22
  Time: 21:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>卖家注册</title>
    <script src="js/jsbn.js"></script>
    <script src="js/RSAUtils.js"></script>
    <script src="js/aeslib.js"></script>
    <script src="js/aes.js"></script>
    <%--下面这个pubKeyMall.js中写入了电商的公钥--%>
    <script src="js/pubKeyMall.js"></script>
    <style>
        * {
            margin: 0px;
            padding: 0px;
            box-sizing: border-box;
        }

        .rg_layout {
            width: 900px;
            height: 350px;
            border: 8px solid #eeeeee;
            background-color: white;
            /* 让div水平居中 */
            margin: auto;
            margin-top: 15px;
            /* padding: 15px; */
        }

        .rg_left {
            /* border: 1px solid red; */
            float: left;
            margin: 15px;
        }

        .rg_left p:first-child {
            color: #fed026;
            size: 20px;
        }

        .rg_left p:last-child {
            color: #a6a6a6;
            size: 20px;
        }

        .rg_center {
            width: 450px;

            /* border: 1px solid red; */
            float: left;
        }

        .rg_right {
            /* border: 1px solid red; */
            float: right;
        }

        .rg_right p:first-child {
            /* color: #a6a6a6; */
            font-size: 15px;
        }

        .rg_right p a {
            color: pink;
        }

        .td_left {
            width: 100px;
            text-align: right;
            height: 45px;
            color: #a6a6a6;
        }

        .td_right {
            padding-left: 50px;
            color: #a6a6a6;
        }

        #username, #password, #password2, #email, #name, #tel, #birthday, #card {
            width: 251px;
            height: 32px;
            border: 1px solid #a6a6a6;
            /* 设置边框圆角 */
            border-radius: 5px;
            padding-left: 10px;

        }

        #checkcode {
            width: 150px;
            height: 32px;
            border: 1px solid #a6a6a6;
            border-radius: 5px;
            padding-left: 10px;

        }

        #img_check {
            height: 32px;
            /* 验证码图片垂直居中 */
            vertical-align: middle;
        }

        #btn_sub {
            width: 150px;
            height: 40px;
            background-color: #ffd026;
            border: 1px solid #a6a6a6;
        }
    </style>
    <script>
        function checkUsername(username) {
            if (username.length <= 0) {
                window.alert("用户名不能为空！");
                return false;
            }
            for (var i = 0; i < username.length; i++) {
                var xiaoxie = username.toLowerCase().charAt(i);
                if ((!(xiaoxie >= '0' && xiaoxie <= '9')) && (!(xiaoxie >= 'a' && xiaoxie <= 'z'))) {
                    window.alert("用户名只能由字母和数字组成！");
                    return false;
                }
            }
            if (username.length > 20) {
                window.alert("用户名长度最长不超过20个字符！");
                return false;
            }
            return true;
        }

        function checkPassword(password) {
            if (password.length <= 0) {
                window.alert("密码不能为空！");
                return false;
            }
            if (password.length < 6) {
                window.alert("密码长度不能小于6位！");
                return false;
            }
            if (password.length > 20) {
                window.alert("密码长度最长不超过20个字符！");
                return false;
            }
            return true;
        }

        function registerSubmit() {
            var username = registerForm.username.value;
            var password = registerForm.password.value;
            var password2 = registerForm.password2.value;
            var checkcode = registerForm.checkcode.value;
            if (password != password2) {
                window.alert("两次输入的密码不一样！");
                return;
            }
            if (!checkUsername(username)) {
                return;
            }
            if (!checkPassword(password)) {
                return;
            }
            console.log("提交的用户名:", username);
            console.log("提交的密码:", password);
            var symmetricKey = AESGenerateKey(32);
            console.log("对称密钥:", symmetricKey);
            var encryptedUsername = AESEncrypt(symmetricKey, username);
            var encryptedPassword = AESEncrypt(symmetricKey, password);
            var encryptedBigIntegerSymKey = RSAEncrypt(rsaPublicKey, symmetricKey);
            console.log("加密的对称密钥:", encryptedBigIntegerSymKey);
            console.log('加密的用户名:', encryptedUsername);
            console.log('加密的密码:', encryptedPassword);

            var params = {
                // "username": username,
                // "password": password,
                "checkcode": checkcode,
                "encryptedBigIntegerSymKey": encryptedBigIntegerSymKey.toString(16),
                "encryptedUsername": encryptedUsername,
                "encryptedPassword": encryptedPassword
            };
            console.log("即将提交post请求...");
            console.log("参数为：", params);
            httpPost("${pageContext.request.contextPath}/sellerRegister", params);
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
<%--<h1>Register</h1>--%>
<%--<form id="registerForm" action="/register" method="post">--%>
<%--    <table>--%>
<%--        <tr>--%>
<%--            <td>Username:</td>--%>
<%--            <td><input type="text" name="username"></td>--%>
<%--        </tr>--%>
<%--        <tr>--%>
<%--            <td>Password:</td>--%>
<%--            <td><input type="password" name="password"></td>--%>
<%--        </tr>--%>
<%--    </table>--%>
<%--    <button type="button" onclick="registerSubmit()">Submit</button>--%>
<%--</form>--%>
<div class="rg_layout">
    <div class="rg_left">
        <p>卖家注册</p>
        <p>SELLER REGISTER</p>
    </div>
    <div class="rg_center">
        <div class="rg_form">
            <!-- 定义表单 form-->
            <form action="/sellerRegister" method="post" id="registerForm">
                <table>
                    <tr>
                        <td class="td_left">用户名</td>
                        <td class="td_right">
                            <input type="text" name="username" id="username" placeholder="请输入用户名">
                        </td>
                    </tr>
                    <tr>
                        <td class="td_left">密码</td>
                        <td class="td_right"><input type="password" name="password" id="password" placeholder="请输入密码">
                        </td>
                    </tr>
                    <tr>
                        <td class="td_left">重复密码</td>
                        <td class="td_right"><input type="password" name="password2" id="password2" placeholder="请输入密码">
                        </td>
                    </tr>
                    <tr>
                        <td class="td_left">银行卡号</td>
                        <td class="td_right"><input type="text" name="card" id="card" placeholder="">
                        </td>
                    </tr>
                    <tr>
                        <td class="td_left">验证码</td>
                        <td class="td_right"><input type="text" name="checkcode" id="checkcode" placeholder="请输入验证码">
                            <img id="img_check" src="${pageContext.request.contextPath}/checkCode">
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" align="center"><input type="button" id="btn_sub" value="注册"
                                                              onclick="registerSubmit()"></td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
    <div class="rg_right">
        <p>已有账号?&nbsp; <a href="${pageContext.request.contextPath}/sellerLogin">&nbsp;立即登录&nbsp;</a></p>
    </div>
    <br>
    <br>
    <div class="rg_right">
        <a href="${pageContext.request.contextPath}">&nbsp;返回主页&nbsp;</a>
    </div>
</div>
<div>
    <a href="${pageContext.request.contextPath}/downloadSellerCert">下载电商证书</a>
    <br>
    <a href="${pageContext.request.contextPath}/verifyCert/vrfyCertificate.jsp">验证证书</a>
</div>
</html>
</body>
</html>
