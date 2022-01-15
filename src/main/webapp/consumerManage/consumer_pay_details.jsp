<%--
  Created by IntelliJ IDEA.
  User: yanxing
  Date: 2021/12/13
  Time: 23:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>开始支付</title>
    <script src="js/jsbn.js"></script>
    <script src="js/RSAUtils.js"></script>
    <script src="js/aeslib.js"></script>
    <script src="js/aes.js"></script>
    <%--下面这个pubKeyMall.js中写入了电商的公钥--%>
    <script src="js/pubKeyMall.js"></script>
    <%--下面这个pubKeyBank.js中写入了银行的公钥--%>
    <script src="js/pubKeyBank.js"></script>
    <script src="js/sha_dev.js"></script>
    <script>
        var orderID = "${order.orderID}";
        var goodID = "${order.goodID}";
        var goodNum = "${order.goodNum}";
        var consumerUsername = "${order.consumerUsername}";
        var sellerUsername = "${order.sellerUsername}";
        var orderTime = "${order.orderTime.getTime()}";
        var money = "${order.orderMoney}";
        var mallRsaPublicKey = rsaPublicKey; // 电商的公钥
        // var bankRsaPublicKey = bankRsaPublicKey;

        function startPay() {
            /**
             * OI用symmetricKey1加密
             * PI用symmetricKey2加密
             * symmetricKey1用mallRsaPublicKey加密
             * symmetricKey2用bankRsaPublicKey加密
             */

            var symmetricKey1 = AESGenerateKey(32);
            var symmetricKey2 = AESGenerateKey(32);
            console.log("对称密钥1(用于商家OI):", symmetricKey1);
            console.log("对称密钥2(用于银行PI):", symmetricKey2);
            var encryptedBigIntegerSymKey1 = RSAEncrypt(mallRsaPublicKey, symmetricKey1);
            var encryptedBigIntegerSymKey2 = RSAEncrypt(bankRsaPublicKey, symmetricKey2);

            var bankCardNum = document.getElementById("bankCardNum").value;
            // var privKey = document.getElementById("privKey").value;

            // * OI包括orderID goodID goodNum consumerUsername sellerUsername orderTime

            //     * PI包括bankCardNum orderID money

            /**下面开始生成OI和PI*/
            var OI = "orderID=" + orderID + "&goodID=" + goodID + "&goodNum=" + goodNum + "&consumerUsername=" + consumerUsername + "&sellerUsername=" + sellerUsername + "&orderTime=" + orderTime;
            var PI = "bankCardNum=" + bankCardNum + "&orderID=" + orderID + "&money=" + money;
            console.log("OI: ", OI);
            console.log("PI: ", PI);
            /**下面开始加密OI和PI*/
            var encryptedOI = AESEncrypt(symmetricKey1, OI);
            var encryptedPI = AESEncrypt(symmetricKey2, PI);
            console.log("encryptedOI: ", encryptedOI);
            console.log("encryptedPI: ", encryptedPI);

            /**下面开始签名*/
                // 先对OI和PI做Hash生成OIMD PIMD
            var shaObj1 = new jsSHA("SHA-256", "TEXT");
            shaObj1.update(OI);
            var OIMD = shaObj1.getHash("HEX").toUpperCase();
            console.log("OIMD:", OIMD);
            var shaObj2 = new jsSHA("SHA-256", "TEXT");
            shaObj2.update(PI);
            var PIMD = shaObj2.getHash("HEX").toUpperCase();
            console.log("PIMD:", PIMD);
            var PIMDOIMD = PIMD + OIMD;
            var shaObj3 = new jsSHA("SHA-256", "TEXT");
            shaObj3.update(PIMDOIMD);
            var POMD = shaObj3.getHash("HEX").toUpperCase();
            console.log("POMD:", POMD);
            var consumerPrivModulusString = document.getElementById("privKeyModulus").value;
            var consumerPrivExponentString = document.getElementById("privKeyExponent").value;
            var consumerPrivModulus = new BigInteger(consumerPrivModulusString, 10);
            var consumerPrivExponent = new BigInteger(consumerPrivExponentString, 10);
            var consumerRsaPrivKey = new RSAPrivateKey(consumerPrivExponent, consumerPrivModulus);
            var DS = RSAEncrypt(consumerRsaPrivKey, POMD);
            console.log("DS: ", DS.toString(16));
            var params = {
                "encryptedOI": encryptedOI,
                "encryptedPI": encryptedPI,
                "encryptedBigIntegerSymKey1": encryptedBigIntegerSymKey1.toString(16),
                "encryptedBigIntegerSymKey2": encryptedBigIntegerSymKey2.toString(16),
                "OIMD": OIMD,
                "PIMD": PIMD,
                "DS": DS,
                "consumerModulusString": consumerPrivModulusString,
                // "symKey2":symmetricKey2,
                // "PI":PI
                "consumerUsername": consumerUsername,
                "sellerUsername": sellerUsername
            };
            httpPost("${pageContext.request.contextPath}/consumerPayJudge", params);
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
<%--<form action="${pageContext.request.contextPath}/consumerPay" method="post">--%>
<h1>支付订单</h1>
<table border="1px">
    <tr>
        <td>订单编号</td>
        <td>${order.orderID}</td>
    </tr>
    <tr>
        <td>商品ID</td>
        <td>${order.goodID}</td>
    </tr>
    <tr>
        <td>商品数量</td>
        <td>${order.goodNum}</td>
    </tr>
    <tr>
        <td>买家名称</td>
        <td>${order.consumerUsername}</td>
    </tr>
    <tr>
        <td>卖家名称</td>
        <td>${order.sellerUsername}</td>
    </tr>
    <tr>
        <td>购买时间</td>
        <td>
            <fmt:formatDate value="${order.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
            (毫秒值${order.orderTime.getTime()})
        </td>
    </tr>
    <tr>
        <td>支付银行卡号</td>
        <td><input type="text" id="bankCardNum" name="bankCardNum"></td>
    </tr>
    <tr>
        <td>您的mod</td>
        <td><input type="text" id="privKeyModulus" name="privKeyModulus"
                   value="28867889892199772343817736083669157287911166996538687179269415616871217718262752368080400626317396972139406722240708920261583934984622810929579727248189747769178278918167949042461963425323321510596842320868457730225743493008317500664974134457466119546218112287463660199256269673578559983389667083498346632243627965274407049256412061228174333521289841241580679432832064492586086773749252589130598103889094230223804587824090511984909898578984539057083340762186324904157710792905689686517370506668459158914440777340979040775220401940386218716835451246077107660074953512949678022810109429100853469753848536162139171336157">
        </td>
    </tr>
    <tr>
        <td>您的公钥exp</td>
        <td><input type="text" id="pubKeyModulus" name="pubKeyModulus" value="65537"></td>
    </tr>
    <tr>
        <td>您的私钥exp(用于签名，不会上传)</td>
        <td><input type="text" id="privKeyExponent" name="privKeyExponent"
                   value="2303722540491704065766921887141457384619000005979787508545997584207950755550516424081976521287821935155547204744173637074752948410357161621094984108336243356162204537009908501946626618771701046743389006792224757451220508543776805903196892186284813238731109560453407126388304170053799666037932142861228815579480232995282360079268908441898965198222910411172465718433992653786065511572926091009006565885986718312465012488713275463568189978444587866905474342164244161952179576214149641966257586781926597608762637626937741466548943072274088909289347054577410578455531521403161683217816915703196771486427463763037437674113">
        </td>
    </tr>
</table>
<br>
<button onclick="startPay()">开始支付</button>
<%--</form>--%>
</body>
</html>
