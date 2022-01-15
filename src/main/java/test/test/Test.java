package test.test;

import top.hityx.cryptomall.rsaUtils.RSAUtils;
import top.hityx.cryptomall.shaUtils.SHACoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws Exception {
//        System.out.println(sendPost("https://www.baidu.com", ""));
        Date date = new Date(1639405317000L);
        System.out.println(date);
        Map<String, String> map = new HashMap<>();
        String aaa = map.get("aaa");
        System.out.println(aaa==null);
        String OI = "orderID=10&goodID=8&goodNum=1&consumerUsername=qq&sellerUsername=333333&orderTime=1639405317000";
        String s = SHACoder.sha256(OI);
        System.out.println(s);

        String pubExpString = "65537";
        String pubModString = "28867889892199772343817736083669157287911166996538687179269415616871217718262752368080400626317396972139406722240708920261583934984622810929579727248189747769178278918167949042461963425323321510596842320868457730225743493008317500664974134457466119546218112287463660199256269673578559983389667083498346632243627965274407049256412061228174333521289841241580679432832064492586086773749252589130598103889094230223804587824090511984909898578984539057083340762186324904157710792905689686517370506668459158914440777340979040775220401940386218716835451246077107660074953512949678022810109429100853469753848536162139171336157";
        RSAPrivateKey rsaPrivateKey = RSAUtils.generateRSAPrivateKey(new BigInteger(pubExpString), new BigInteger(pubModString));
        String encryptedDS = "14df6a5c2d7e428e429d7440a9ef56c153388816303b382a51f1a152252ec39b6e0185dd2310c2faa58012361bb45da2cb8568df1aec89a0ed4e7e7bd3f3ef374cce3275563129d14cc389fdb5b55b5e3a5a8dbe9fc8e1326bf82d63f502ba8a21b344b5190bd9492a1c325af90a2545dd89cab7d9d98f88b18e4b386a252ec3e8fc4134946a70e293062d33173c9420ef6fbd4928229861eb132c9d9102aec1c5abd52f668fb08d2bdd054180e0f73344ca5e105db444904558ee82c42246afa788a808523b933602aec8d15f12737e29166a7c101ae8223dd49f7e255a95137bcb44eceecfb9fe017606736c28f3c2ab063f68aa37381db633f23abcfa6f97";
        BigInteger bigIntegerEncryptedDS = new BigInteger(encryptedDS, 16);
        String DS = RSAUtils.decrypt(rsaPrivateKey, bigIntegerEncryptedDS);
        System.out.println(DS);
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                line = new String(line.getBytes("GBK"),
                        "UTF-8");
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    private static String sha256(String message) {
        String encodeStr = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(message.getBytes(StandardCharsets.UTF_8));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        String temp = null;
        for (byte aByte : bytes) {
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                // 1得到一位的进行补0操作
                sb.append("0");
            }
            sb.append(temp);
        }
        return sb.toString();
    }
}
