package top.hityx.cryptomall.consumerManage;

import top.hityx.cryptomall.aesUtils.AESUtils;
import top.hityx.cryptomall.rsaUtils.RSAUtils;
import top.hityx.cryptomall.shaUtils.SHACoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

@WebServlet("/consumerPayJudge")
public class ConsumerPayJudge extends HttpServlet {
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
        // 拿取req中的参数
        String encryptedOI = req.getParameter("encryptedOI");
        String encryptedPI = req.getParameter("encryptedPI");
        String encryptedBigIntegerSymKey1Str = req.getParameter("encryptedBigIntegerSymKey1");
        String encryptedBigIntegerSymKey2Str = req.getParameter("encryptedBigIntegerSymKey2");
        String OIMD = req.getParameter("OIMD");
        String PIMD = req.getParameter("PIMD");
        String DS = req.getParameter("DS");
        // 先把这些参数设置好，马上发给银行用
        req.setAttribute("encryptedOI", encryptedOI);
        req.setAttribute("encryptedPI", encryptedPI);
        req.setAttribute("encryptedBigIntegerSymKey1Str", encryptedBigIntegerSymKey1Str);
        req.setAttribute("encryptedBigIntegerSymKey2Str", encryptedBigIntegerSymKey2Str);
        req.setAttribute("OIMD", OIMD);
        req.setAttribute("PIMD", PIMD);
        req.setAttribute("DS", DS);
        req.setAttribute("sellerUsername", req.getParameter("sellerUsername"));
        req.setAttribute("consumerUsername", req.getParameter("consumerUsername"));

        System.out.println("电商接收到的参数如下:");
        System.out.println("encryptedOI:" + encryptedOI);
        System.out.println("encryptedPI:" + encryptedPI);
        System.out.println("encryptedBigIntegerSymKey1Str:" + encryptedBigIntegerSymKey1Str);
        System.out.println("encryptedBigIntegerSymKey2Str:" + encryptedBigIntegerSymKey2Str);
        System.out.println("OIMD:" + OIMD);
        System.out.println("PIMD:" + PIMD);
        System.out.println("DS:" + DS);

        String consumerModulusString = req.getParameter("consumerModulusString");
        req.setAttribute("consumerModulusString", consumerModulusString);
        System.out.println("consumerModulusString:" + consumerModulusString);

        if (encryptedOI == null || encryptedPI == null || encryptedBigIntegerSymKey1Str == null || encryptedBigIntegerSymKey2Str == null || OIMD == null || PIMD == null || DS == null || consumerModulusString == null) {
            System.out.println("检测到错误！");
            resp.getWriter().println("<script>window.alert('ERROR!');\nwindow.location.href='" + req.getContextPath() + "'</script>");
            return;
        }
        // 电商解密出SymKey1
        RSAPrivateKey rsaPrivateKey = null;
        String privExpString = "2303722540491704065766921887141457384619000005979787508545997584207950755550516424081976521287821935155547204744173637074752948410357161621094984108336243356162204537009908501946626618771701046743389006792224757451220508543776805903196892186284813238731109560453407126388304170053799666037932142861228815579480232995282360079268908441898965198222910411172465718433992653786065511572926091009006565885986718312465012488713275463568189978444587866905474342164244161952179576214149641966257586781926597608762637626937741466548943072274088909289347054577410578455531521403161683217816915703196771486427463763037437674113";
        String modulusString = "28867889892199772343817736083669157287911166996538687179269415616871217718262752368080400626317396972139406722240708920261583934984622810929579727248189747769178278918167949042461963425323321510596842320868457730225743493008317500664974134457466119546218112287463660199256269673578559983389667083498346632243627965274407049256412061228174333521289841241580679432832064492586086773749252589130598103889094230223804587824090511984909898578984539057083340762186324904157710792905689686517370506668459158914440777340979040775220401940386218716835451246077107660074953512949678022810109429100853469753848536162139171336157";
        try {
            rsaPrivateKey = RSAUtils.generateRSAPrivateKey(new BigInteger(privExpString), new BigInteger(modulusString));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        if (rsaPrivateKey == null) {
            System.out.println("检测到错误！");
            resp.getWriter().println("<script>window.alert('ERROR!');\nwindow.location.href='" + req.getContextPath() + "'</script>");
            return;
        }
        String symKey1 = RSAUtils.decrypt(rsaPrivateKey, new BigInteger(encryptedBigIntegerSymKey1Str, 16));
        // 电商恢复出OI明文
        String OI = AESUtils.AESDecrypt(symKey1, encryptedOI);
        System.out.println("电商解密出的OI:" + OI);
        // 电商验证签名
        String OIMD2 = null;
        try {
            OIMD2 = SHACoder.sha256(OI);
            System.out.println("电商计算出的OIMD:" + OIMD2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (OIMD2 == null) {
            System.out.println("检测到错误！");
            resp.getWriter().println("<script>window.alert('ERROR!');\nwindow.location.href='" + req.getContextPath() + "'</script>");
            return;
        }
        if (!OIMD.equals(OIMD2)) {
            System.out.println("OIMD:" + OIMD);
            System.out.println("OIMD2:" + OIMD2);
            System.out.println("检测到OIMD被篡改！");
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().println("<script>window.alert('ERROR!');\nwindow.location.href='" + req.getContextPath() + "'</script>");
            return;
        }
        String consumerExpString = "65537";
        try {
            rsaPrivateKey = RSAUtils.generateRSAPrivateKey(new BigInteger(consumerExpString), new BigInteger(consumerModulusString));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        if (rsaPrivateKey == null) {
            System.out.println("检测到错误！");
            resp.getWriter().println("<script>window.alert('ERROR!');\nwindow.location.href='" + req.getContextPath() + "'</script>");
            return;
        }
        String POMD = RSAUtils.decrypt(rsaPrivateKey, new BigInteger(DS));
        System.out.println("电商用顾客公钥对DS解密，解密得到POMD:" + POMD);
        try {
            String POMD2 = SHACoder.sha256(PIMD + OIMD2);
            System.out.println("电商用收到的PIMD和计算出的OIMD计算出的POMD:" + POMD2);
            if (!POMD.equals(POMD2)) {
                System.out.println("检测到POMD被篡改！");
                resp.getWriter().println("<script>window.alert('ERROR!');\nwindow.location.href='" + req.getContextPath() + "'</script>");
                return;
            } else {
                System.out.println("电商验签通过！");
                // TODO 在这里跳转到银行
//                req.setAttribute("symKey2", req.getParameter("symKey2"));
//                req.setAttribute("PI", req.getParameter("PI"));
                req.getRequestDispatcher("consumerManage/consumer_pay_redirect.jsp").forward(req, resp);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("检测到错误！");
        resp.getWriter().println("<script>window.alert('ERROR!');\nwindow.location.href='" + req.getContextPath() + "'</script>");
        return;
    }

//    private Map<String, String>
}
