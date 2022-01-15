<!--
功能: 上传保存在本地的证书文件,进行证书验证
TODO 版本要求: 需要HTML5(支持前端文件读取)
@author Yangwh
-->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<!--TODO 依赖路径根据自己的目录进行更改-->
<script src="js/sha256-min.js"></script>
<script src="js/RSAUtils.js"></script>
<script src="js/jsbn.js"></script>

<!--
功能: 上传保存在本地的证书文件,进行证书验证
TODO 版本要求: 需要HTML5(支持前端文件读取)
@author Yangwh
-->
<html lang="en">
<!--TODO 依赖路径根据自己的目录进行更改-->
<%--<script src="./labjs/sha256-min.js"></script>--%>
<%--<script src="./labjs/RSAUtils.js"></script>--%>
<%--<script src="./labjs/jsbn.js"></script>--%>

<head>
  <meta charset="UTF-8">
<%--  <meta name="viewport" content="width=device-width, initial-scale=1.0">--%>
<%--  <meta http-equiv="X-UA-Compatible" content="ie=edge">--%>
  <title>证书验证</title>
  <style>
    body {
      /*background: url('earth_view_1.jpeg');*/
      /*background: url('earth_view_2.jpeg');*/
      background-repeat: no-repeat;
      background-size: 100% auto;
    }

    #login-box {
      width: 30%;
      height: auto;
      margin: 0 auto;
      margin-top: 15%;
      text-align: center;
      background: #00000090;
      padding: 20px 50px;
    }

    #login-box h1 {
      color: #fff;
    }

    #login-box .form .item {
      margin-top: 15px;
    }

    #login-box .form .item i {
      font-size: 18px;
      color: #fff;
    }

    #login-box .form .item input {
      width: 180px;
      font-size: 18px;
      border: 0;
      border-bottom: 2px solid #fff;
      padding: 5px 10px;
      background: #ffffff00;
      color: #fff;
    }
    #login-box .form .item p {
      color: white;
    }

    #login-box button {
      margin-top: 15px;
      width: 180px;
      height: 30px;
      font-size: 20px;
      font-weight: 400;
      color: #fff;
      border: 0;
      border-radius: 15px;
      /*在这里可以设置不同Login提交按钮的样式*/
      /*background: #1abc9c;*/
      /*background-image: linear-gradient(to right, #43e97b 0%, #38f9d7 100%);*/
      background-image: linear-gradient(to right, #e4afcb 0%, #b8cbb8 0%, #b8cbb8 0%, #e2c58b 30%, #c2ce9c 64%, #7edbdc 100%);
    }
  </style>
</head>
<body>
<div id="login-box">
  <form action="">
    <h1>Test certification</h1>
    <div class="form">
      <div class="item">
        <i class="fa fa-user-circle-o" aria-hidden="true"></i>
        <input type="file" id="test-image-file" name="test">
      </div>
      <div class="item">
        <i class="fa fa-key" aria-hidden="true"></i>
        <p id="test-file-info"></p>
      </div>
    </div>
  </form>
</div>
<!--<form method="post" action="http://localhost/test" enctype="multipart/form-data">-->
<!--    <p>-->
<!--        <input type="file" id="test-image-file" name="test">-->
<!--    </p>-->
<!--    <p id="test-file-info"></p>-->
<!--</form>-->

<script>
  // 文件相关:
  // 文件信息
  var info = document.getElementById('test-file-info');
  // 文件
  var fileInput = document.getElementById('test-image-file');
  // 文件内容存储
  var cerLines = "";

  // 证书信息:
  // 证书序列号
  var serial_number = "";
  // 过期时间
  var end_time = "";
  // 公钥
  var pubkey = "";
  // 签名
  var sign = "";

  // CA公钥
  var publicModulusString = '20829697634030730332474154626744922639335745659421446210770814434230716694716687632663224366308203602348881779308835218127326220187449014899443080316258756073632168367463335883641221155261296710929063441628427681395815540411241489791907612790528532554651833368226879549134700128650140664490336489826247139817990869289482610357718636376105392043102197459973251894394173530003473572199014470427752771295372422264160617363568401947544576309262371364261794989543531753965916171655240382527101846777709363951179502238921891134770118962417297046764933457154998075106754795354170302475202288671318201455960526686496032962961';
  var publicExponentString = '65537';
  // TODO 前端证书验证-CA公钥读取: 暂时直接写在代码中,以后可以改成从文件读取
  var publicModulus = new BigInteger(publicModulusString);
  var publicExponent = new BigInteger(publicExponentString);
  var rsaPublicKey = new RSAPublicKey(publicExponent, publicModulus);

  // 当上传文件时,进行文件处理
  document.getElementById('test-image-file').addEventListener('change', function selectedFileChanged() {

    // 文件是否为空
    if (this.files.length === 0) {
      console.log('请选择文件！');
      return;
    }

    // 文件基本信息
    // TODO: 文件信息的展示可以删除
    var file = fileInput.files[0];
    // info.innerHTML = '文件: ' + file.name + '<br>' +
    //         '大小: ' + file.size + '<br>' +
    //         '修改: ' + file.lastModifiedDate;

    // 根据后缀.cert判断文件格式
    var fileNameRegex = new RegExp("\w*\.cert");
    if(!fileNameRegex.test(file.name)){
      alert("文件格式错误,应为.cert格式");
      return;
    }

    // 读文件,放入cerLines中
    const reader = new FileReader();
    reader.readAsText(this.files[0]);
    reader.onload = function fileReadCompleted() {
      // ------------------------------------文件读取-------------------------------------
      // 当读取完成时，内容只在`reader.result`中
      cerLines = reader.result;
      console.log(typeof(cerLines));
      console.log(cerLines);
      // 获取index
      var serial_number_start = cerLines.indexOf("Serial Number: ");
      var serial_number_end = cerLines.indexOf("Yangwh CA: www.yangwhCA.com");
      var end_time_start = cerLines.indexOf("Valid Time To: ");
      var end_time_end = cerLines.indexOf("User: ");
      var pubkey_start = cerLines.indexOf("Public Key: ");
      var pubkey_end = cerLines.indexOf("Sign: ");
      var sign_start = cerLines.indexOf("Sign: ");
      console.log("serial_number_start = "+serial_number_start);
      console.log("serial_number_end = "+serial_number_end);
      console.log("end_time_start = "+end_time_start);
      console.log("end_time_end = "+end_time_end);
      console.log("pubkey_start = "+pubkey_start);
      console.log("pubkey_end = "+pubkey_end);
      console.log("sign_start = "+sign_start);
      // 赋值
      serial_number = cerLines.substring(serial_number_start+15,serial_number_end-1);
      end_time = cerLines.substring(end_time_start+15,end_time_end-1);
      pubkey = cerLines.substring(pubkey_start+12,pubkey_end-1);
      sign = cerLines.slice(sign_start+6,-1);
      // 打印
      console.log("serial_number = "+serial_number);
      console.log("end_time = "+end_time);
      console.log("pubkey = "+pubkey);
      console.log("sign = "+sign);

      // ---------------------------------证书有效期-------------------------------


      // ---------------------------------CA公钥读取-------------------------------
      // 获取公钥
      var modulus = rsaPublicKey.modulus;
      var exponent = rsaPublicKey.exponent;
      console.log("modulus = "+modulus);
      console.log("exponent = "+exponent);

      // ---------------------------------证书验证---------------------------------
      // 要验证的明文
      var strToSign = serial_number + end_time + pubkey;
      console.log("strToSign = "+strToSign);
      // 明文做sha256,然后全部小写
      var strAfterSHA256 = hex_sha256(strToSign);
      console.log("strAfterSHA256 = "+strAfterSHA256);
      // 全部小写
      var strSHA256LowCase = strAfterSHA256.toLowerCase();
      console.log("strSHA256LowCase = "+strSHA256LowCase);
      // str2BigInteger
      var decInt1 = str2BigInteger(strSHA256LowCase);
      console.log("---decInt1 = "+decInt1);

      // 将字符串格式,16进制的sign转换为10进制BigInteger
      var encInt = new BigInteger(sign, 16);
      console.log("encInt = "+encInt);

      // modPow
      var decInt2 = encInt.modPow(exponent,modulus);
      console.log("---decInt2 = "+decInt2);

      var decStr1 = new String(decInt1);
      var decStr2 = new String(decInt2);
      console.log("decStr1 = "+decStr1);
      console.log("decStr2 = "+decStr2);

      // 验证
      var cmp = decStr1.localeCompare(decStr2);
      if(cmp == 0){
        // ---------------证书时间验证--------------
        var year = end_time.substring(0,4);
        var month = end_time.substring(4,6);
        var date = end_time.substring(6,8);
        var end_date = new Date(year, month, date);
        console.log("end_date: ");
        console.log(end_date);
        var present_date = new Date();
        console.log("present_date: ");
        console.log(present_date);

        if(present_date < end_date){
          console.log("CA签名验证成功");
          alert("CA签名验证成功!");
        }
        else{
          console.log("证书过期!");
          alert("证 书 过 期 !");
        }

      }else{
        console.log("CA签名验证失败");
        alert("CA签名验证 失 败 !!!");
      }
    };
  });

</script>

</body>
</html>