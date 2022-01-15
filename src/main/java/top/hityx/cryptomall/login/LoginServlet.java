package top.hityx.cryptomall.login;

import top.hityx.cryptomall.utils.JdbcUtils;
import top.hityx.cryptomall.rsaUtils.RSAUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    private static Map<BigInteger, BigInteger> pubExp2privateExp = new HashMap<>();
    private static Map<BigInteger, BigInteger> pubExp2modulus = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //生成临时的RSA密钥对，获取公私密钥
            KeyPair keyPair = RSAUtils.generateKeyPair(1024);
            RSAPublicKey publickey = RSAUtils.getPublicKey(keyPair);
            RSAPrivateKey privateKey = RSAUtils.getPrivateKey(keyPair);
            //获取e和m
            BigInteger publicExponent = publickey.getPublicExponent();
            BigInteger privateExponent = privateKey.getPrivateExponent();
            BigInteger publicModulus = publickey.getModulus();
            //将公钥对应的私钥和mod在服务端保存
            pubExp2privateExp.put(publicExponent, privateExponent);
            pubExp2modulus.put(publicExponent, publicModulus);
            //公钥发送到前端
            request.setAttribute("publicExponent", publicExponent.toString(16));
            request.setAttribute("publicModulus", publicModulus.toString(16));
//            request.setAttribute("successful", 0);
            request.getRequestDispatcher("login/seller_login.jsp").forward(request, response);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doPost");
        String encryptedUsername = request.getParameter("username");
        String encryptedPassword = request.getParameter("password");
        System.out.println("加密的username:" + encryptedUsername);
        System.out.println("加密的password:" + encryptedPassword);
        String publicExpString = request.getParameter("publicExponent");
        BigInteger usernameBigInteger = new BigInteger(encryptedUsername, 16);
        BigInteger passwordBigInteger = new BigInteger(encryptedPassword, 16);
        BigInteger publicExponent = new BigInteger(publicExpString, 16);
        BigInteger privateExponent = pubExp2privateExp.get(publicExponent);
        BigInteger modulus = pubExp2modulus.get(publicExponent);
        RSAPrivateKey rsaPrivateKey = null;
        try {
            rsaPrivateKey = RSAUtils.generateRSAPrivateKey(privateExponent, modulus);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        String username = RSAUtils.decrypt(rsaPrivateKey, usernameBigInteger);
        String password = RSAUtils.decrypt(rsaPrivateKey, passwordBigInteger);
        System.out.println("解密的username:" + username);
        System.out.println("解密的password:" + password);
        if(verify(username, password)){
            System.out.println("验证成功！");
            request.setAttribute("verify", 1);
        }else{
            request.setAttribute("verify", -1);
            System.out.println("验证失败！");
        }
        doGet(request, response);

    }
    private static Boolean verify(String username, String password){
        String passwordHash = sha256(password);
        Connection conn = JdbcUtils.getConnection();
        PreparedStatement preparedStatement = null;
        String sql = "SELECT * FROM user WHERE username = ? AND passwordHash = ?";
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, passwordHash);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String sha256(String message) {
        String encodeStr = null;
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
        StringBuilder sb = new StringBuilder("");
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
