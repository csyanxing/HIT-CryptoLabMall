<%--
  Created by IntelliJ IDEA.
  User: yanxing
  Date: 2021/12/13
  Time: 9:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script>
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

        <%--window.location.href="http://172.20.168.158:8080/javaxuexi_war/judge?consumer=${consumer}&seller=${seller}&orderNum=${orderNum}&money=${money}";--%>

        function myFun() {
            <%--var PI = "${PI}";--%>
            <%--var symKey2 = "${symKey2}";--%>
            var encryptedOI = "${encryptedOI}";
            var encryptedPI = "${encryptedPI}";
            var encryptedBigIntegerSymKey1Str = "${encryptedBigIntegerSymKey1Str}";
            var encryptedBigIntegerSymKey2Str = "${encryptedBigIntegerSymKey2Str}";
            var OIMD = "${OIMD}";
            var PIMD = "${PIMD}";
            var DS = "${DS}";
            var consumerModulusString = "${consumerModulusString}";
            var consumerUsername = "${consumerUsername}";
            var sellerUsername = "${sellerUsername}";
            // TODO 在这里跳转到银行
            var bankURL = "http://192.168.43.53:8080/javaxuexi_war/judge";
            var params = {
                "encryptedOI": encryptedOI,
                "encryptedPI": encryptedPI,
                "encryptedBigIntegerSymKey1Str": encryptedBigIntegerSymKey1Str, // 16进制
                "encryptedBigIntegerSymKey2Str": encryptedBigIntegerSymKey2Str, // 16进制
                "OIMD": OIMD,
                "PIMD": PIMD,
                "DS": DS,
                "consumerModulusString": consumerModulusString,
                // "PI":PI,
                // "symKey2":symKey2
                "sellerUsername": sellerUsername,
                "consumerUsername": consumerUsername
            }
            console.log(params);
            httpPost(bankURL, params);
        }
    </script>
</head>
<body onload="myFun()">

</body>
</html>
