package top.hityx.cryptomall.payment;

import top.hityx.cryptomall.aesUtils.AESUtils;
import top.hityx.cryptomall.rsaUtils.RSAUtils;
import top.hityx.cryptomall.shaUtils.SHA256;
import top.hityx.cryptomall.utils.JdbcUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/successPay")
public class ServletPaySuccess extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        doPost(request, response);
//        response.getOutputStream().print("<script>window.alert('支付成功！')</script>");
        System.out.println("aaaa");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String enc2 = req.getParameter("enc2");
        String hash2 = req.getParameter("hash2");
        String priv = req.getParameter("priv");
        List<String> decry = Decry(priv, hash2, enc2);
        String orderID = decry.get(1);
        System.out.println(orderID);
        if(orderID.equals("false")){
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().println("<script>window.alert('检测到攻击！');</script>");
            return;
        }

        Connection conn = JdbcUtils.getConnection();
        PreparedStatement prep = null;
        try {
            prep = conn.prepareStatement("UPDATE mall_orders SET has_payed = 1 WHERE (order_id = ?)");
            prep.setInt(1, Integer.valueOf(orderID));
            prep.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().println("<script>window.alert('支付成功！');\nwindow.location.href='" + req.getContextPath() + "/consumerCart" + "'</script>");
    }

    private List<String> Decry(String Enc_priv, String hash, String enc) {
        List<String> result = new ArrayList<>();
        //先解密用公钥加密的私钥
        String privexpstr = "2303722540491704065766921887141457384619000005979787508545997584207950755550516424081976521287821935155547204744173637074752948410357161621094984108336243356162204537009908501946626618771701046743389006792224757451220508543776805903196892186284813238731109560453407126388304170053799666037932142861228815579480232995282360079268908441898965198222910411172465718433992653786065511572926091009006565885986718312465012488713275463568189978444587866905474342164244161952179576214149641966257586781926597608762637626937741466548943072274088909289347054577410578455531521403161683217816915703196771486427463763037437674113";
        BigInteger private_exponent = new BigInteger(privexpstr);
        String module = "28867889892199772343817736083669157287911166996538687179269415616871217718262752368080400626317396972139406722240708920261583934984622810929579727248189747769178278918167949042461963425323321510596842320868457730225743493008317500664974134457466119546218112287463660199256269673578559983389667083498346632243627965274407049256412061228174333521289841241580679432832064492586086773749252589130598103889094230223804587824090511984909898578984539057083340762186324904157710792905689686517370506668459158914440777340979040775220401940386218716835451246077107660074953512949678022810109429100853469753848536162139171336157";
        BigInteger modulus = new BigInteger(module);
        RSAPrivateKey deckey = null;
        try {
            deckey = RSAUtils.generateRSAPrivateKey(private_exponent, modulus);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        BigInteger cipherPassword = new BigInteger(Enc_priv);
        String privkey = RSAUtils.decrypt(deckey, cipherPassword);//得到解密后的私钥
        System.out.println("解密私钥结果是");
        System.out.println(privkey);
        //然后计算mac，看是否被篡改，如果被篡改，停止解密
        String tocompare = SHA256.sha256(enc);
        if (!hash.equals(tocompare)) {
            result.add("false");
            result.add("0");
            return result;
        }
        //然后开始用私钥解密
        String decpwd = AESUtils.AESDecrypt(privkey, enc);
        System.out.println("解密密码的结果是");
        System.out.println(decpwd);
        result.add("true");
        result.add(decpwd);
        return result;
    }

}
