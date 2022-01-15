package top.hityx.cryptomall.sellerLogin;

import top.hityx.cryptomall.aesUtils.AESUtils;
import top.hityx.cryptomall.rsaUtils.RSAUtils;
import top.hityx.cryptomall.shaUtils.SHACoder;
import top.hityx.cryptomall.utils.JdbcUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@WebServlet("/sellerRegister")
public class SellerRegister extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("卖家访问了注册页面");
        req.getRequestDispatcher("/seller_login/seller_register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("卖家提交了注册请求...");
        String privExpString = "2303722540491704065766921887141457384619000005979787508545997584207950755550516424081976521287821935155547204744173637074752948410357161621094984108336243356162204537009908501946626618771701046743389006792224757451220508543776805903196892186284813238731109560453407126388304170053799666037932142861228815579480232995282360079268908441898965198222910411172465718433992653786065511572926091009006565885986718312465012488713275463568189978444587866905474342164244161952179576214149641966257586781926597608762637626937741466548943072274088909289347054577410578455531521403161683217816915703196771486427463763037437674113";
        String modulusString = "28867889892199772343817736083669157287911166996538687179269415616871217718262752368080400626317396972139406722240708920261583934984622810929579727248189747769178278918167949042461963425323321510596842320868457730225743493008317500664974134457466119546218112287463660199256269673578559983389667083498346632243627965274407049256412061228174333521289841241580679432832064492586086773749252589130598103889094230223804587824090511984909898578984539057083340762186324904157710792905689686517370506668459158914440777340979040775220401940386218716835451246077107660074953512949678022810109429100853469753848536162139171336157";
        String username = null;
        String password = null;
        String checkCode = null;
        Map<String, String> params = getParams(req, privExpString, modulusString);
        username = params.get("username");
        password = params.get("password");
        checkCode = params.get("checkCode");
        if (username == null || password == null || checkCode == null) {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().println("<script>window.alert('注册出错！');\nwindow.location.href='" + req.getContextPath() + "'</script>");
            System.out.println("-------------------注册出错！-----------------------");
        }
        System.out.println("即将注册......");
        System.out.println("用户名: " + username);
        System.out.println("密码: " + password);
        System.out.println("验证码: "+checkCode);
        if(checkCode==null || !(checkCode.equals(req.getSession().getAttribute("checkCode")))){
            System.out.println("-------------------验证码错误！-----------------------");
            req.setAttribute("failReason", "验证码错误!");
            req.getRequestDispatcher("/seller_login/register_fail.jsp").forward(req, resp);
            return;
        }
        if (!check(username, password)) {
            System.out.println("-------------------用户名或密码格式错误！-----------------------");
            req.setAttribute("failReason", "格式错误");
            req.getRequestDispatcher("/seller_login/register_fail.jsp").forward(req, resp);
            return;
        }
        // 检查用户名是否重复
        Connection connection = JdbcUtils.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM mall_sellers where seller_username=?");
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                JdbcUtils.close(rs, preparedStatement, connection);
                System.out.println("-------------------提交的用户名已被注册！-----------------------");
                req.setAttribute("failReason", "提交的用户名已被注册!");
                req.getRequestDispatcher("/seller_login/register_fail.jsp").forward(req, resp);
                return;
            } else {
                JdbcUtils.close(rs, preparedStatement, connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sha256Password = null;
        try {
            sha256Password = SHACoder.sha256(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("提交密码的Hash值为: " + sha256Password);
        Connection conn = JdbcUtils.getConnection();
        System.out.println("即将开始写入数据库......");
        PreparedStatement pre = null;
        try {
            pre = conn.prepareStatement("INSERT INTO mall_sellers VALUES (null, ?, ?, null)");
            pre.setString(1, username);
            pre.setString(2, sha256Password);
            int execute = pre.executeUpdate();
            if (execute != 1) {
                System.out.println("-------------------数据库错误！-----------------------");
                req.setAttribute("failReason", "数据库错误!");
                req.getRequestDispatcher("/seller_login/register_fail.jsp").forward(req, resp);
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JdbcUtils.close(pre, conn);
        String contextPath = req.getContextPath();
        resp.sendRedirect(contextPath+"/seller_login/register_success.jsp");
        System.out.println("-------------------完成注册请求-----------------------");
    }

    private static boolean check(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        if (username.length() == 0 || password.length() == 0) {
            return false;
        }
        if (username.length() > 20 || password.length() > 20) {
            return false;
        }
        username = username.toLowerCase(Locale.ROOT);
        password = password.toLowerCase(Locale.ROOT);
        if (username.matches("[a-zA-Z0-9]*[^a-zA-Z0-9][a-zA-Z0-9]*")) { // 如果存在非字母数字的字符
            return false;
        }
        return true; //合法
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
        String username = null;
        String password = null;
        String checkCode = req.getParameter("checkcode");
        String encryptedUsername = req.getParameter("encryptedUsername");
        String encryptedPassword = req.getParameter("encryptedPassword");
        String encryptedBigIntegerSymKeyStr = req.getParameter("encryptedBigIntegerSymKey");
        BigInteger encryptedBigIntegerSymKey = new BigInteger(encryptedBigIntegerSymKeyStr, 16);
        String symKey = RSAUtils.decrypt(rsaPrivateKey, encryptedBigIntegerSymKey);
//        System.out.println("解密得到的对称密钥: " + symKey);
        String decryptedUsername = AESUtils.AESDecrypt(symKey, encryptedUsername);
        String decryptedPassword = AESUtils.AESDecrypt(symKey, encryptedPassword);
//        System.out.println("解密得到的用户名: " + decryptedUsername);
//        System.out.println("解密得到的密码: " + decryptedPassword);
        username = decryptedUsername;
        password = decryptedPassword;
        params.put("username", username);
        params.put("password", password);
        params.put("checkCode", checkCode);
        return params;
    }
}
