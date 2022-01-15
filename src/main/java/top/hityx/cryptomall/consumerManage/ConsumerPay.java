package top.hityx.cryptomall.consumerManage;

import top.hityx.cryptomall.aesUtils.AESUtils;
import top.hityx.cryptomall.models.Order;
import top.hityx.cryptomall.rsaUtils.RSAUtils;
import top.hityx.cryptomall.utils.JdbcUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/consumerPay")
public class ConsumerPay extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userType = (String) req.getSession().getAttribute("userType");
        String consumerUsername = (String) req.getSession().getAttribute("consumerUsername");
        if (!(userType != null && ("consumer".equals(userType)) && consumerUsername != null && consumerUsername.length() != 0)) {
            resp.sendRedirect("consumerManage/unLogin.jsp");
            return;
        }
        String privExpString = "2303722540491704065766921887141457384619000005979787508545997584207950755550516424081976521287821935155547204744173637074752948410357161621094984108336243356162204537009908501946626618771701046743389006792224757451220508543776805903196892186284813238731109560453407126388304170053799666037932142861228815579480232995282360079268908441898965198222910411172465718433992653786065511572926091009006565885986718312465012488713275463568189978444587866905474342164244161952179576214149641966257586781926597608762637626937741466548943072274088909289347054577410578455531521403161683217816915703196771486427463763037437674113";
        String modulusString = "28867889892199772343817736083669157287911166996538687179269415616871217718262752368080400626317396972139406722240708920261583934984622810929579727248189747769178278918167949042461963425323321510596842320868457730225743493008317500664974134457466119546218112287463660199256269673578559983389667083498346632243627965274407049256412061228174333521289841241580679432832064492586086773749252589130598103889094230223804587824090511984909898578984539057083340762186324904157710792905689686517370506668459158914440777340979040775220401940386218716835451246077107660074953512949678022810109429100853469753848536162139171336157";
        String orderIDStr = getParams(req, privExpString, modulusString).get("orderID");

        int orderID = Integer.valueOf(orderIDStr);
//        System.out.println(orderID);
        Order order = getOrderByID(orderID);
        if (order != null) {
//            System.out.println(order);
        }
        req.setAttribute("order", order);
        req.getRequestDispatcher("consumerManage/consumer_pay_details.jsp").forward(req, resp);
        if (true)
            return;

        String consumer = consumerUsername;
        String seller = null;
        int orderNum = orderID;
        int money = -1;
        Connection conn = JdbcUtils.getConnection();
        PreparedStatement prep = null;
        ResultSet rs = null;

        try {
            prep = conn.prepareStatement("SELECT * FROM mall_orders WHERE order_id=?");
            prep.setInt(1, Integer.valueOf(orderID));
            rs = prep.executeQuery();
            if (rs.next()) {
                seller = rs.getString("seller_username");
                money = (int) (rs.getDouble("order_money"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        boolean b = false;
        if (seller != null && money != -1) {
            resp.sendRedirect("http://172.20.168.158:8080/javaxuexi_war/judge" + "?consumer=" + consumer + "&seller=" + seller + "&orderNum=" + orderNum + "&money=" + money);
        }
    }

    private Order getOrderByID(int orderID) {
        Connection conn = JdbcUtils.getConnection();
        PreparedStatement prep = null;
        ResultSet rs = null;
        try {
            prep = conn.prepareStatement("SELECT * FROM mall_orders WHERE order_id=?");
            prep.setInt(1, orderID);
            rs = prep.executeQuery();
            if (rs.next()) {
                return new Order(rs.getInt("order_id"),
                        rs.getString("consumer_username"),
                        rs.getString("seller_username"),
                        rs.getInt("good_id"),
                        rs.getString("good_name"),
                        rs.getDouble("good_price"),
                        rs.getInt("good_num"),
                        rs.getDouble("order_money"),
                        new Date(rs.getTimestamp("order_time").getTime()),
                        rs.getBoolean("has_payed"),
                        rs.getBoolean("has_on_load"),
                        rs.getBoolean("has_confirm_delivery"),
                        rs.getString("order_info"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, String> getParams(HttpServletRequest req, String privExpString, String modulusString) {
        Map<String, String> params = new HashMap<>();
        RSAPrivateKey rsaPrivateKey = null;
        try {
            rsaPrivateKey = RSAUtils.generateRSAPrivateKey(new BigInteger(privExpString), new BigInteger(modulusString));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        if (rsaPrivateKey == null) {
            return params;
        }
        String orderID = null;
        String encryptedOrderID = req.getParameter("encryptedOrderID");
        String encryptedBigIntegerSymKeyStr = req.getParameter("encryptedBigIntegerSymKey");
        BigInteger encryptedBigIntegerSymKey = new BigInteger(encryptedBigIntegerSymKeyStr, 16);
        String symKey = RSAUtils.decrypt(rsaPrivateKey, encryptedBigIntegerSymKey);
//        System.out.println("解密得到的对称密钥: " + symKey);
        String decryptedOrderID = AESUtils.AESDecrypt(symKey, encryptedOrderID);
//        System.out.println("解密得到的用户名: " + decryptedUsername);
//        System.out.println("解密得到的密码: " + decryptedPassword);
        orderID = decryptedOrderID;
        params.put("orderID", orderID);
        return params;
    }
}
