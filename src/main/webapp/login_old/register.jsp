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
    <title>Register</title>
    <script src="js/jsbn.js"></script>
    <script src="js/RSAUtils.js"></script>
    <script>
        <c:if test="${successful eq 2}">
        window.alert("注册失败！用户名已被注册！");
        </c:if>
        <c:if test="${successful eq 1}">
        window.alert("注册成功！");
        </c:if>
        <c:if test="${successful eq -1}">
        window.alert("注册失败！请重新注册！");
        </c:if>
        var publicModulusString = '<%=request.getAttribute("publicModulus")%>';
        var publicExponentString = '<%=request.getAttribute("publicExponent")%>';
        var publicModulus = new BigInteger(publicModulusString, 16);
        var publicExponent = new BigInteger(publicExponentString, 16);
        var rsaPublicKey = new RSAPublicKey(publicExponent, publicModulus);
        console.log("(十进制)获取到的m:", publicModulus.toString());
        console.log("(十进制)获取到的e:", publicExponent.toString());

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
            return true;
        }

        function registerSubmit() {
            var username = registerForm.username.value;
            var password = registerForm.password.value;
            if (!checkUsername(username)) {
                return;
            }
            if (!checkPassword(password)) {
                return;
            }
            console.log("提交的用户名:", username);
            console.log("提交的密码:", password);
            var encryptedBigIntegerUsername = RSAEncrypt(rsaPublicKey, username);
            var encryptedBigIntegerPassword = RSAEncrypt(rsaPublicKey, password);
            console.log("(十进制)加密后的用户名:", encryptedBigIntegerUsername.toString());
            console.log("(十进制)加密后的密码:", encryptedBigIntegerPassword.toString());
            var params = {
                "username": encryptedBigIntegerUsername.toString(16),
                "password": encryptedBigIntegerPassword.toString(16),
                "publicExponent": publicExponentString,
            };
            console.log("即将提交post请求...");
            console.log("参数为：", params);
            httpPost("${pageContext.request.contextPath}/register", params);
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
<h1>Register</h1>
<form id="registerForm" action="/register" method="post">
    <table>
        <tr>
            <td>Username:</td>
            <td><input type="text" name="username"></td>
        </tr>
        <tr>
            <td>Password:</td>
            <td><input type="password" name="password"></td>
        </tr>
    </table>
    <button type="button" onclick="registerSubmit()">Submit</button>
</form>

</body>
</html>
